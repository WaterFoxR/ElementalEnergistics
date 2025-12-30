package com.waterfoxr.elementalenergistics.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.gui.GuiHandler;
import sirttas.elementalcraft.gui.GuiHelper;

import static sirttas.elementalcraft.gui.GuiHandler.getXOffset;
import static sirttas.elementalcraft.gui.GuiHandler.getYOffset;

@Mixin(GuiHandler.class)
public abstract class MixinGuiHandler {



    /**
     * @author WaterFoxR
     * @reason 修正元素显示文字重叠问题
     */
    @Overwrite(remap = false)
    private static void renderElementGauge(GuiGraphics guiGraphics, Font font, int element, int max, ElementType type, int index) {
        GuiHelper.renderElementGauge(guiGraphics, font, getXOffset() - 32 , getYOffset() - 40 + 20 * index, element, max, type);
    }
}
