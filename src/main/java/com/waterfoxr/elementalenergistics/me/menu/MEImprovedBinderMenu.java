package com.waterfoxr.elementalenergistics.me.menu;

import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.common.ClientDisplaySlot;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.implementations.UpgradeableMenu;
import appeng.menu.interfaces.IProgressProvider;
import com.waterfoxr.elementalenergistics.block.entity.MEImprovedBinderBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class MEImprovedBinderMenu extends UpgradeableMenu<MEImprovedBinderBlockEntity> implements IProgressProvider {

    public static final MenuType<MEImprovedBinderMenu> TYPE = MenuTypeBuilder
            .create(MEImprovedBinderMenu::new, MEImprovedBinderBlockEntity.class)
            .build("me_improved_binder");

    public final MEImprovedBinderBlockEntity meImprovedBinder;

    public MEImprovedBinderMenu(MenuType<?> menuType, int id, Inventory inv, MEImprovedBinderBlockEntity blockEntity) {
        super(TYPE, id, inv, blockEntity);
        this.meImprovedBinder = blockEntity;
        this.addSlot(new ClientDisplaySlot(GenericStack.fromItemStack(meImprovedBinder.getRecipeOutput())), SlotSemantics.MACHINE_INPUT);
    }

    @Override
    public int getCurrentProgress() {
        return (int) meImprovedBinder.getProgressRatio();
    }

    @Override
    public int getMaxProgress() {
        return meImprovedBinder.getProgress();
    }
}
