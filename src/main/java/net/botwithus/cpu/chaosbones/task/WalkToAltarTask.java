package net.botwithus.cpu.chaosbones.task;

import net.botwithus.cpu.chaosbones.Constants;
import net.botwithus.cpu.core.CustomLogger;
import net.botwithus.cpu.core.EventScript;
import net.botwithus.cpu.core.task.Task;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;

public class WalkToAltarTask implements Task {

    @Override
    public boolean applies(EventScript script) {
        var noAltarNearby = SceneObjectQuery.newQuery().name(Constants.CHAOS_ALTAR_PATTERN).results().isEmpty();
        return noAltarNearby;
    }

    @Override
    public void apply(EventScript script) {
        CustomLogger.log("Walk to altar manually and restart script.");
        script.queueTask(new WaitTask(s -> this.applies(s), new BankTask()));
    }

}
