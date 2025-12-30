package com.waterfoxr.elementalenergistics.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.*;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.gui.GuiHelper;

import static sirttas.elementalcraft.gui.GuiHelper.showDebugInfo;

@Mixin(GuiHelper.class)
@OnlyIn(Dist.CLIENT)
public class MixinGuiHelper {

    @Final
    @Shadow(remap = false)
    private static ResourceLocation GAUGE;

    @Unique
    private static int elementalEnergistics$getElementTypeOffset(ElementType type) {
        byte var10000;
        switch (type) {
            case WATER -> var10000 = 1;
            case FIRE -> var10000 = 2;
            case EARTH -> var10000 = 3;
            case AIR -> var10000 = 4;
            default -> var10000 = 0;
        }

        return var10000;
    }


    /**
     * @author WaterFoxR
     * @reason 重写渲染方法使得显示数值时同时显示元素名
     */
    @Overwrite(remap = false)
    public static void renderElementGauge(GuiGraphics guiGraphics, Font font, int x, int y, int amount, int max, ElementType type, boolean showDebugInfo){
        guiGraphics.blit(GAUGE, x, y, 0, 0, 16, 16);

        int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

        if (progress <= 1 && amount > 0) {
            progress = 2;
        }
        guiGraphics.blit(GAUGE, x, y + 16 - progress, elementalEnergistics$getElementTypeOffset(type) * 16, 16 - progress + (Boolean.TRUE.equals(ECConfig.CLIENT.usePaleElementGauge.get()) ? 16 : 0), 16, progress);
        if (showDebugInfo() && showDebugInfo) {
            guiGraphics.drawString(font, type.getDisplayName().getString() + amount + "/" + max, x + 16, y + 4, 16777215, true);
        }
    }

}
