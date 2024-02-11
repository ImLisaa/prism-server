package net.phantara.prism.server.properties;

import net.phantara.prism.server.files.Document;
import net.phantara.prism.server.properties.defaults.DefaultServerProperties;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Lisa Kapahnke
 * @created 19.01.2024 | 02:13
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class ServerProperties {

    private final Document document;
    private final Path path;

    public ServerProperties() {
        Document loadedDocument;
        this.path = Path.of("prism-properties.json");

        if (Files.notExists(this.path)) {
            loadedDocument = DefaultServerProperties.get();
            loadedDocument.write(this.path);
        } else {
            loadedDocument = new Document(this.path);
        }
        this.document = loadedDocument;
    }

    public <T> T getProperty(String property, Class<T> clazz) {
        return this.document.get(property, clazz);
    }

    public void setProperty(String property, Object value) {
        this.document.set(property, value);
    }

    public void updateDocument() {
        this.document.write(this.path);
    }

    public void reload() {
        this.document.read(this.path);
    }
}
