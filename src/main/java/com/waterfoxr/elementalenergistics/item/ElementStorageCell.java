package com.waterfoxr.elementalenergistics.item;

import appeng.items.storage.BasicStorageCell;
import appeng.items.storage.StorageTier;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKeyType;
import net.minecraft.world.level.ItemLike;

public class ElementStorageCell extends BasicStorageCell {

    public ElementStorageCell(Properties properties, StorageTier tier, ItemLike housing, ItemLike coreItem) {
        super(
                properties,
                coreItem,
                housing,
                tier.idleDrain(),
                tier.bytes() / 1_024,
                tier.bytes() / 128,
                4,
                ElementKeyType.INSTANCE
        );
    }


}