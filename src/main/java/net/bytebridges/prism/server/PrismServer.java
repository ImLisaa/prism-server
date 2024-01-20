package net.bytebridges.prism.server;

import net.bytebridges.prism.server.instance.InstanceProvider;
import net.bytebridges.prism.server.instance.type.InstanceType;
import net.bytebridges.prism.server.properties.ServerProperties;
import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.Instance;

public class PrismServer {

    private static PrismServer instance;
    private final InstanceProvider instanceProvider;
    private final ServerProperties serverProperties;

    public PrismServer(ExtensionBootstrap server) {
        instance = this;
        this.instanceProvider = new InstanceProvider();
        this.serverProperties = new ServerProperties();

        if (this.serverProperties.getProperty("create-default-instance", Boolean.TYPE)) {
            this.instanceProvider.createInstance("world", InstanceType.FLAT);
        }

        if (this.serverProperties.getProperty("enable-default-events", Boolean.TYPE)) {
            EventNode<Event> defaultNode = EventNode.all("default-node");


            defaultNode.addListener(AsyncPlayerConfigurationEvent.class, event -> {
                var player = event.getPlayer();

                event.setSpawningInstance(this.instanceProvider.getInstance("world").orElse(null));
            });

            MinecraftServer.getGlobalEventHandler().addChild(defaultNode);
        }

        if (this.serverProperties.getProperty("online-mode", Boolean.TYPE)) {
            MojangAuth.init();
        }

        if (this.serverProperties.getProperty("bungeecord-support", Boolean.TYPE)) {
            BungeeCordProxy.enable();
        }

        if (this.serverProperties.getProperty("velocity-support", Boolean.TYPE) && BungeeCordProxy.isEnabled()) {
            MinecraftServer.LOGGER.error("Bungeecord-Support and Velocity-Support conflict with each-other. Disabling Velocity-Support.");
            this.serverProperties.setProperty("velocity-support", false);
            this.serverProperties.updateDocument();
            this.serverProperties.reload();
        }

        if (this.serverProperties.getProperty("velocity-support", Boolean.TYPE)) {
            VelocityProxy.enable(this.serverProperties.getProperty("velocity-secret-key", String.class));
        }

        System.setProperty("minestom.chunk-view-distance", "10");
        System.setProperty("minestom.entity-view-distance", "32");

        MinecraftServer.setBrandName("Prism-Server 1.20.4");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            MinecraftServer.LOGGER.info("Prism-Server is shutting down.");
            for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
                instance.saveChunksToStorage().join();
            }
            MinecraftServer.stopCleanly();
        }));

        server.start(this.serverProperties.getProperty("address", String.class), this.serverProperties.getProperty("port", Integer.TYPE));
    }

    public static PrismServer getInstance() {
        return instance;
    }

    public InstanceProvider getInstanceProvider() {
        return instanceProvider;
    }

    public ServerProperties getServerProperties() {
        return serverProperties;
    }

    public void broadcastMessage(Component message) {
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }
}
