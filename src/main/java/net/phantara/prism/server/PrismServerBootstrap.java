package net.phantara.prism.server;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;

/**
 * @author Lisa Kapahnke
 * @created 21.01.2024 | 19:01
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class PrismServerBootstrap {


    public static void main(String[] args) {
        new PrismServer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            MinecraftServer.LOGGER.info("Prism-Server is shutting down.");
            for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
                instance.saveChunksToStorage().join();
            }
            MinecraftServer.stopCleanly();
        }));
    }
}
