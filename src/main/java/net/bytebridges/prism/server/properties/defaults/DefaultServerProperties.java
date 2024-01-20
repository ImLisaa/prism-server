package net.bytebridges.prism.server.properties.defaults;

import net.bytebridges.prism.server.files.Document;

/**
 * @author Lisa Kapahnke
 * @created 19.01.2024 | 02:13
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class DefaultServerProperties {

    public static Document get() {
        return new Document()
                .addIfNotExists("server-name", "A Prism Server Instance")
                .addIfNotExists("address", "0.0.0.0")
                .addIfNotExists("port", 25565)
                .addIfNotExists("player-limit", 20)
                .addIfNotExists("motd", "A Prism Server Instance")
                .addIfNotExists("online-mode", true)
                .addIfNotExists("difficulty", "EASY")
                .addIfNotExists("enable-default-commands", false)
                .addIfNotExists("enable-default-events", false)
                .addIfNotExists("create-default-instance", false)
                .addIfNotExists("bungeecord-support", false)
                .addIfNotExists("velocity-support", false)
                .addIfNotExists("velocity-secret-key", "YOUR-VELOCITY-SECRET-KEY");
    }
}
