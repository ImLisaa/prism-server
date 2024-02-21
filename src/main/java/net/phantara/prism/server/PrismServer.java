package net.phantara.prism.server;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extensions.ExtensionManager;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.phantara.prism.server.instance.InstanceProvider;
import net.phantara.prism.server.instance.type.InstanceType;
import net.phantara.prism.server.properties.ServerProperties;

import java.net.InetSocketAddress;

public class PrismServer {

    private static PrismServer instance;
    private final InstanceProvider instanceProvider;
    private final ServerProperties serverProperties;
    private final ExtensionManager extensionManager;

    public PrismServer() {
        instance = this;
        var server = MinecraftServer.init();

        this.extensionManager = new ExtensionManager(MinecraftServer.process());
        this.extensionManager.start();

        this.instanceProvider = new InstanceProvider();
        this.serverProperties = new ServerProperties();

        this.extensionManager.gotoPreInit();

        setupProperties();

        System.setProperty("minestom.chunk-view-distance", "10");
        System.setProperty("minestom.entity-view-distance", "32");

        MinecraftServer.setBrandName("Prism-Server 1.20.4");
        MinecraftServer.getSchedulerManager().buildShutdownTask(this.extensionManager::shutdown);

        this.extensionManager.gotoInit();

        var host = System.getenv("RC_HOST");
        var port = Integer.parseInt(System.getenv("RC_PORT"));

        server.start(new InetSocketAddress(host, port));

        this.extensionManager.gotoPostInit();
    }

    public static PrismServer getInstance() {
        return instance;
    }

    public InstanceProvider getInstanceProvider() {
        return this.instanceProvider;
    }

    public ServerProperties getServerProperties() {
        return this.serverProperties;
    }

    public ExtensionManager getExtensionManager() {
        return this.extensionManager;
    }

    public void broadcastMessage(Component message) {
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    private void setupProperties() {
        if (this.serverProperties.getProperty("create-default-instance", Boolean.TYPE)) {
            this.instanceProvider.createInstance("world", InstanceType.FLAT);
        }

        if (this.serverProperties.getProperty("enable-default-events", Boolean.TYPE)) {
            EventNode<Event> defaultNode = EventNode.all("default-node");


            defaultNode.addListener(AsyncPlayerConfigurationEvent.class, event -> {
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

        VelocityProxy.enable(System.getenv("PROXY_SECRET"));
    }
}