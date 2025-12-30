package com.waterfoxr.elementalenergistics.item;

import appeng.items.storage.StorageTier;
import appeng.items.tools.powered.PortableCellItem;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKeyType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

import java.util.Objects;

public class ElementPortableCellItem extends PortableCellItem {
    public ElementPortableCellItem(int totalTypes, MenuType<?> menuType, StorageTier tier, Properties props, int defaultColor) {
        super(ElementKeyType.INSTANCE, totalTypes, menuType, tier, props, defaultColor);
    }

    @Override
    public ResourceLocation getRecipeId() {
        return Objects.requireNonNull(getRegistryName());
    }
}
