package net.botwithus.cpu.chaosbones.task;

import java.util.function.Predicate;

import net.botwithus.cpu.core.EventScript;
import net.botwithus.cpu.core.task.Task;
import net.botwithus.rs3.game.Client;

public class WaitTask implements Task {

    private final Predicate<EventScript> shouldContinueWaiting;
    private final Task nextTask;

    public WaitTask() {
        this(null);
    }

    public WaitTask(Predicate<EventScript> shouldContinueWaiting) {
        this(shouldContinueWaiting, null);
    }

    public WaitTask(Predicate<EventScript> shouldContinueWaiting, Task nextTask) {
        this.shouldContinueWaiting = shouldContinueWaiting;
        this.nextTask = nextTask;
    }

    @Override
    public boolean applies(EventScript script) {
        var isMoving = Client.getLocalPlayer().isMoving();
        var isAnimating = Client.getLocalPlayer().getAnimationId() != -1;
        return isMoving || isAnimating;
    }

    @Override
    public void apply(EventScript script) {
        if (shouldContinueWaiting != null && shouldContinueWaiting.test(script)) {
            script.queueTask(this);
            return;
        }
        script.queueTask(nextTask);
    }

}
