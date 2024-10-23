package net.botwithus.cpu.chaosbones.task;

import java.util.function.Predicate;

import net.botwithus.cpu.chaosbones.Constants;
import net.botwithus.cpu.core.EventScript;
import net.botwithus.cpu.core.task.Task;
import net.botwithus.rs3.game.Client;

public class WaitTask implements Task {

    private final Predicate<EventScript> shouldContinueWaiting;
    private final Task nextTask;
    private final long timeout;
    private long startTick = -1;

    public WaitTask() {
        this(null);
    }

    public WaitTask(Predicate<EventScript> shouldContinueWaiting) {
        this(shouldContinueWaiting, null);
    }

    public WaitTask(Predicate<EventScript> shouldContinueWaiting, Task nextTask) {
        this(shouldContinueWaiting, nextTask, Constants.DEFAULT_WAIT_TIMEOUT_TICKS); // default timeout of 1 minute
    }

    public WaitTask(Predicate<EventScript> shouldContinueWaiting, Task nextTask, long timeout) {
        this.shouldContinueWaiting = shouldContinueWaiting;
        this.nextTask = nextTask;
        this.timeout = timeout;
    }

    @Override
    public boolean applies(EventScript script) {
        var isMoving = Client.getLocalPlayer().isMoving();
        var isAnimating = Client.getLocalPlayer().getAnimationId() != -1;
        return isMoving || isAnimating;
    }

    @Override
    public void apply(EventScript script) {
        if (this.startTick == -1) {
            this.startTick = script.ticks();
        }

        if (script.ticks() - this.startTick > this.timeout) {
            // we could queue the next thing, but we probably want it to auto compute.
            return;
        }

        if (shouldContinueWaiting != null && shouldContinueWaiting.test(script)) {
            script.queueTask(this);
            return;
        }
        script.queueTask(nextTask);
    }

}
