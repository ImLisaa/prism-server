package net.phantara.prism.server.command.inject;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Lisa Kapahnke
 * @created 19.01.2024 | 01:52
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class CommandInjector {

    private CommandInjector(List<Class<?>> classSet) {
        for (Class<?> commandClass : classSet) {
            try {
                Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
                MinecraftServer.getCommandManager().register(command);
                MinecraftServer.LOGGER.info("Injected command {}.", command.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException exception) {
                MinecraftServer.LOGGER.error("An error occured whilst injecting commands.");
                MinecraftServer.LOGGER.error(exception.getMessage());
            }
        }
    }

    public static void inject(List<Class<?>> classSet) {
        new CommandInjector(classSet);
    }
}
