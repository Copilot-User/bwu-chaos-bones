package net.botwithus.cpu.chaosbones.task;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.hud.inventories.Bank;
import net.botwithus.cpu.chaosbones.Constants;
import net.botwithus.cpu.core.EventScript;
import net.botwithus.cpu.core.task.Task;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;

public class BankTask implements Task {

    @Override
    public boolean applies(EventScript script) {
        var noBones = !Backpack.contains(Constants.BONES_PATTERN);
        var isNearBank = !NpcQuery.newQuery().option(Constants.BANK_OPTION).results().isEmpty();
        return noBones && isNearBank;
    }

    @Override
    public void apply(EventScript script) {
        Bank.loadLastPreset();
        script.queueTask(new WaitTask(s -> !Backpack.contains(Constants.BONES_PATTERN), new OfferBonesTask()));
    }

}
