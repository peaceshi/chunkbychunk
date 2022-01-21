package xyz.immortius.chunkbychunk.common.util;

import net.minecraft.world.level.material.MaterialColor;

public class ColorUtil {
    public static final int HIGH = 2;
    public static final int NORMAL = 1;
    public static final int LOW = 0;
    public static final int LOWEST = 3;

    public static final byte encode(MaterialColor color, int brightness) {
        return (byte) (color.id << 2 | brightness);
    }

    public static final int decode(int color) {
        return MaterialColor.MATERIAL_COLORS[color / 4].calculateRGBColor(color & 3);
    }
}
