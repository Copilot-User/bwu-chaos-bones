package net.botwithus.cpu.chaosbones.task;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.cpu.chaosbones.Constants;
import net.botwithus.cpu.core.CustomLogger;
import net.botwithus.cpu.core.EventScript;
import net.botwithus.cpu.core.task.Task;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;

public class OfferBonesTask implements Task {

    @Override
    public boolean applies(EventScript script) {
        var altarIsNear = !SceneObjectQuery.newQuery().name(Constants.CHAOS_ALTAR_PATTERN).results().isEmpty();
        var hasBones = Backpack.contains(Constants.BONES_PATTERN);
        var isNotAnimating = Client.getLocalPlayer().getAnimationId() == -1;
        return altarIsNear && hasBones && isNotAnimating;
    }

    @Override
    public void apply(EventScript script) {
        var object = SceneObjectQuery.newQuery().name(Constants.CHAOS_ALTAR_PATTERN).results().nearest();
        if (object == null) { // This is a defensive check, it should never happen
            CustomLogger.log("No altar found.");
            script.queueTask(new WalkToAltarTask());
            return;
        }

        CustomLogger.log("Offering bones.");
        object.interact(Constants.ACTION);
        script.queueTask(new WaitTask(s -> Backpack.contains(Constants.BONES_PATTERN), new BankTask(), 150));
    }

}
