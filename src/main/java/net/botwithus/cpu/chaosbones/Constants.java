package net.botwithus.cpu.chaosbones;

import java.util.regex.Pattern;

import net.botwithus.rs3.game.Area;
import net.botwithus.rs3.game.Coordinate;

public class Constants {
    public static final Pattern BONES_PATTERN = Pattern.compile(".*bone.*", Pattern.CASE_INSENSITIVE);
    public static final Pattern CHAOS_ALTAR_PATTERN = Pattern.compile(".*altar.*", Pattern.CASE_INSENSITIVE);
    public static final String ACTION = "Offer";
    public static final Area ALATAR_AREA = new Area.Circular(new Coordinate(3239, 3609, 0), 3);
    public static final String BANK_OPTION = "Load Last Preset from";
}
