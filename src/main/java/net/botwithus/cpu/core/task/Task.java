package net.botwithus.cpu.core.task;

import net.botwithus.cpu.core.EventScript;

public interface Task {
    public boolean applies(EventScript script);

    public void apply(EventScript script);
}
