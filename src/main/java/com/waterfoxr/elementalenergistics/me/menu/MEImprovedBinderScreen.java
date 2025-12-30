package com.waterfoxr.elementalenergistics.me.menu;

import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.style.ScreenStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MEImprovedBinderScreen extends UpgradeableScreen<MEImprovedBinderMenu> {

    private static final Blitter ELEMENT_PROGRESS = Blitter.texture("guis/vibchamber.png").src(176, 0, 14, 13);

//    private final ProgressBar elementBar;

    public MEImprovedBinderScreen(MEImprovedBinderMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);

//        this.elementBar = new ProgressBar(menu, style.getImage("elementBar"),
//                ProgressBar.Direction.VERTICAL);
//
//        widgets.add("elementBar",elementBar);

//        addToLeftToolbar(CommonButtons.togglePowerUnit());

    }


    @Override
    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX,
                       int mouseY) {
        // Show the flame "burning down" as we burn through an item of fuel
        if (this.menu.meImprovedBinder.hasRecipe()) {
            int f = this.menu.meImprovedBinder.getElementAmount() * ELEMENT_PROGRESS.getSrcHeight() / this.menu.meImprovedBinder.getRecipeElementAmount();
            ELEMENT_PROGRESS.copy()
                    .src(
                            ELEMENT_PROGRESS.getSrcX(),
                            ELEMENT_PROGRESS.getSrcY() + ELEMENT_PROGRESS.getSrcHeight() - f,
                            ELEMENT_PROGRESS.getSrcWidth(),
                            f)
                    .dest(80, 20 + ELEMENT_PROGRESS.getSrcHeight() - f)
                    .blit(guiGraphics);
        }
    }


}
