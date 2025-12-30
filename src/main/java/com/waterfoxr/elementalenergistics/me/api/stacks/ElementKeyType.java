package com.waterfoxr.elementalenergistics.me.api.stacks;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import com.waterfoxr.elementalenergistics.EEConfig;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.data.ModLanguageProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

/*
 * Author: Almost Reliable, rlnt
 * Source: Applied Elemental (https://github.com/AlmostReliable/appliedelemental)
 * License: ARR - Used with permission. All rights reserved by the original author.
 */
public class ElementKeyType extends AEKeyType {

    public static final ElementKeyType INSTANCE = new ElementKeyType();

    private ElementKeyType(){
        super(ElementalEnergistics.id("element"), ElementKey.class, ModLanguageProvider.ELEMENTAL_DESCRIPTION.get());
    }

    @Override
    public int getAmountPerOperation() {
        return EEConfig.COMMON.amountPerOperation.get();
    }

    @Override
    public int getAmountPerByte() {
        return EEConfig.COMMON.amountPerByte.get();
    }

    @Override
    public @Nullable AEKey readFromPacket(FriendlyByteBuf buf) {
        ElementType elementType = ElementType.byName(buf.readUtf());
        return new ElementKey(elementType);
    }

    @Override
    public @Nullable AEKey loadKeyFromTag(CompoundTag tag) {
        ElementType elementType = ElementType.byName(tag.getString(ECNames.ELEMENT_TYPE));
        return new ElementKey(elementType);
    }
}
