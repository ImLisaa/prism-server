package net.phantara.prism.server.command.loader;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import net.phantara.prism.server.command.PrismCommandInject;
import net.minestom.server.MinecraftServer;

import java.util.List;

/**
 * @author Lisa Kapahnke
 * @created 19.01.2024 | 01:51
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public record PrismCommandLoader(String packageName) {

    public List<Class<?>> load() {
        try (ScanResult scanResult = new ClassGraph().verbose(false).enableAnnotationInfo().acceptPackages(this.packageName).scan()) {
            return scanResult.getClassesWithAnnotation(PrismCommandInject.class).loadClasses();
        } catch (Throwable throwable) {
            MinecraftServer.LOGGER.error("Error loading commands.");
            MinecraftServer.LOGGER.error(throwable.getMessage());
            return List.of();
        }
    }
}
