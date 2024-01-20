package net.bytebridges.prism.server.countdown;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Lisa Kapahnke
 * @created 19.01.2024 | 15:35
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class Countdown {

    private Task task;

    private final int maxValue;
    private int value;

    private final List<Predicate<Void>> predicates = new LinkedList<>();

    @Nullable
    private Runnable stopCallback;

    public Countdown(int maxValue) {
        this.maxValue = maxValue;
    }

    public void start() {
        this.task = MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (!checkAllPredicates()) {
                this.value = maxValue;
                return TaskSchedule.seconds(1);
            }
            if (this.value <= 0) {
                if (this.stopCallback != null) {
                    this.stopCallback.run();
                    return TaskSchedule.stop();
                }
            }
            this.value--;
            return TaskSchedule.seconds(1);
        });
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    public void reduce(int amount) {
        this.value -= amount;

        if (this.value <= 0) {
            if (this.stopCallback != null) {
                stopCallback.run();
                this.stop();
            }
        }
    }

    public Countdown setStopCallback(Runnable stopCallback) {
        this.stopCallback = stopCallback;
        return this;
    }

    public Countdown addPredicate(Predicate<Void> predicate) {
        this.predicates.add(predicate);
        return this;
    }

    private boolean checkAllPredicates() {
        return this.predicates.stream().allMatch(predicate -> predicate.test(null));
    }
}
