package net.bytebridges.prism.server.instance;

import net.bytebridges.prism.server.files.Document;
import net.bytebridges.prism.server.instance.type.InstanceType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.fakeplayer.FakePlayer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class InstanceProvider {

    private final Map<String, InstanceContainer> instances;

    public InstanceProvider() {
        this.instances = new LinkedHashMap<>();
        Path mapPath = Path.of("maps");
        createDirectoryIfNotExists(mapPath);
    }

    public void createInstance(String name, InstanceType type) {
        Path path = Path.of("maps", name);
        if (Files.notExists(path)) {
            createDirectory(path);
            UUID containerId = UUID.randomUUID();
            InstanceContainer instanceContainer = new InstanceContainer(containerId, DimensionType.OVERWORLD, new AnvilLoader(path));
            instanceContainer.setChunkSupplier(LightingChunk::new);
            instanceContainer.setGenerator(type.getGenerator());
            this.instances.put(name, instanceContainer);
            this.writeIdToFile(containerId, Path.of("maps/" + name, "id.json"));
            MinecraftServer.getInstanceManager().registerInstance(instanceContainer);
            return;
        }
        loadInstance(name);
    }

    public void loadInstance(String name) {
        if (!this.instances.containsKey(name)) {
            Path containerPath = Path.of("maps", name);
            Path idPath = Path.of("maps", name, "id.json");
            if (Files.notExists(idPath)) {
                writeIdToFile(UUID.randomUUID(), idPath);
            }
            Document document = new Document(idPath);
            UUID containerId = UUID.fromString(document.get("identifier", String.class));
            InstanceContainer instanceContainer = new InstanceContainer(containerId, DimensionType.OVERWORLD, new AnvilLoader(containerPath));
            instanceContainer.setChunkSupplier(LightingChunk::new);
            this.instances.put(name, instanceContainer);
            MinecraftServer.getInstanceManager().registerInstance(instanceContainer);
            MinecraftServer.LOGGER.info("Instance {} loaded.", name);
        }
    }

    public void unloadInstance(String name) {
        getInstance(name).ifPresentOrElse(instanceContainer -> {
            instanceContainer.saveChunksToStorage().join();
            MinecraftServer.getInstanceManager().unregisterInstance(instanceContainer);
            this.instances.remove(name);
            MinecraftServer.LOGGER.info("Instance {} unloaded.", name);
        }, () -> MinecraftServer.LOGGER.error("Instance {} is not loaded.", name));
    }

    public void loadAllInstances() {
        File[] files = Path.of("maps").toFile().listFiles(File::isDirectory);
        if (files == null || files.length == 0) {
            MinecraftServer.LOGGER.warn("No instances to load.");
            return;
        }
        for (File file : files) {
            loadInstance(file.getName());
        }
    }

    public Optional<InstanceContainer> getInstance(String name) {
        return Optional.ofNullable(instances.get(name));
    }

    public Set<InstanceContainer> getInstances() {
        return Set.copyOf(instances.values());
    }

    private void createDirectoryIfNotExists(Path folderPath) {
        if (Files.notExists(folderPath)) {
            createDirectory(folderPath);
        }
    }

    private void createDirectory(Path folderPath) {
        try {
            Files.createDirectory(folderPath);
        } catch (IOException exception) {
            MinecraftServer.LOGGER.error("Could not create Folder " + folderPath.getFileName());
            MinecraftServer.LOGGER.error(exception.getMessage());
        }
    }

    private void writeIdToFile(UUID containerIdentifier, Path path) {
        Document document = new Document();
        document.addIfNotExists("identifier", containerIdentifier.toString()).write(path);
    }
}
