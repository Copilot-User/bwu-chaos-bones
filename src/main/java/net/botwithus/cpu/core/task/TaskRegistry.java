package net.botwithus.cpu.core.task;

import java.util.List;
import java.util.Optional;

import net.botwithus.cpu.core.EventScript;

public class TaskRegistry {
    private final List<Task> tasks;

    public TaskRegistry(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Optional<Task> computeNextTask(EventScript script) {
        return tasks.stream().filter(a -> a.applies(script)).findFirst();
    }

    public Optional<Task> findByClass(Class<? extends Task> task) {
        return tasks.stream().filter(a -> a.getClass().equals(task)).findFirst();
    }
}
