package net.botwithus.cpu.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

import net.botwithus.cpu.core.task.Task;
import net.botwithus.cpu.core.task.TaskRegistry;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.ServerTickedEvent;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.script.TickingScript;
import net.botwithus.rs3.script.config.ScriptConfig;

public abstract class EventScript extends TickingScript {
    private final TransferQueue<Task> tasks = new LinkedTransferQueue<>();
    private final TaskRegistry taskRegistry;

    public EventScript(String name, ScriptConfig config, ScriptDefinition definition, List<Task> tasks) {
        super(name, config, definition);
        this.taskRegistry = new TaskRegistry(tasks);
        CustomLogger.setScript(this);
    }

    @Override
    public boolean onInitialize() {
        super.onInitialize();

        var nextAction = this.taskRegistry.computeNextTask(this);
        nextAction.ifPresent(tasks::offer);
        return true;
    }

    @Override
    public void onActivation() {
        super.onActivation();

        CustomLogger.log("Resuming script.");
        this.tasks.clear();
        this.unsubscribeAll();
        this.subscribe(ServerTickedEvent.class, this);

        var nextAction = this.taskRegistry.computeNextTask(this);
        nextAction.ifPresent(tasks::offer);
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();
        CustomLogger.log("Pausing script.");
        this.tasks.clear();
    }

    private long lastTickProcessed = this.ticks();

    @Override
    public void onTick(LocalPlayer arg0) {
        try {
            if (Client.getLocalPlayer() == null) {
                CustomLogger.log("Player is null, waiting.");
                return;
            }

            if (Client.getLocalPlayer().isMoving()) {
                CustomLogger.log("Player is moving, waiting.");
                return;
            }

            var tasksToProcess = new ArrayList<Task>();
            this.tasks.drainTo(tasksToProcess);

            // if 50 ticks happened since the last tick we processed, we should recompute
            // the next task
            if (this.ticks() - lastTickProcessed > 50) {
                CustomLogger.log("Recomputing next task, we're likely stuck.");
                var nextAction = taskRegistry.computeNextTask(this);
                nextAction.ifPresent(tasks::offer);
                lastTickProcessed = this.ticks();
                return;
            }

            CustomLogger.log("Processing " + tasksToProcess.size() + " tasks.");
            tasksToProcess.forEach((task) -> task.apply(this));
            if (tasksToProcess.size() > 0) {
                lastTickProcessed = this.ticks();
            }
        } catch (Exception e) {
            CustomLogger.logThrowable(e);
        }
    }

    public void queueTask(Class<? extends Task> task) {
        CustomLogger.log("Queuing task: " + task.getSimpleName());
        this.taskRegistry.findByClass(task).ifPresent(tasks::offer);
    }

    public void queueTask(Task task) {
        CustomLogger.log("Queuing task: " + task.getClass().getSimpleName());
        tasks.offer(task);
    }

    public TaskRegistry taskRegistry() {
        return this.taskRegistry;
    }

    public long ticks() {
        return super.ticks;
    }
}
