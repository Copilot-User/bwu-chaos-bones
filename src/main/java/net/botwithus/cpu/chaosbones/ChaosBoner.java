package net.botwithus.cpu.chaosbones;

import java.util.List;

import net.botwithus.cpu.chaosbones.task.BankTask;
import net.botwithus.cpu.chaosbones.task.OfferBonesTask;
import net.botwithus.cpu.chaosbones.task.WaitTask;
import net.botwithus.cpu.chaosbones.task.WalkToAltarTask;
import net.botwithus.cpu.core.EventScript;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.script.config.ScriptConfig;

public class ChaosBoner extends EventScript {

    public ChaosBoner(String name, ScriptConfig config, ScriptDefinition definition) {
        super(name, config, definition, List.of(
                new WaitTask(),
                new WalkToAltarTask(),
                new OfferBonesTask(),
                new BankTask()));
    }
}
