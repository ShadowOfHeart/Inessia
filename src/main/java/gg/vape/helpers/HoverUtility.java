package gg.vape.helpers;

import net.minecraft.util.Util;

public class HoverUtility extends Util
{
    public static boolean isHovered(final int mouseX, final int mouseY, final double x, final double y, final double width, final double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
