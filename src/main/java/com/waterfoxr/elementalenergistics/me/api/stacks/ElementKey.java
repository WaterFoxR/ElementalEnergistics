package com.waterfoxr.elementalenergistics.me.api.stacks;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import com.waterfoxr.elementalenergistics.data.ModLanguageProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

import java.util.List;

/*
 * Author: Almost Reliable, rlnt
 * Source: Applied Elemental (https://github.com/AlmostReliable/appliedelemental)
 * License: ARR - Used with permission. All rights reserved by the original author.
 */
public class ElementKey extends AEKey {

    private final ElementType elementType;

    public ElementKey(ElementType elementType){
        this.elementType = elementType;
    }

    public static ElementKey of(ElementType type){
        return new ElementKey(type);
    }

    public ElementType getElementType(){return elementType;}

    @Override
    public AEKeyType getType() {
        return ElementKeyType.INSTANCE;
    }

    @Override
    public AEKey dropSecondary() {
        return this;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString(ECNames.ELEMENT_TYPE, elementType.getSerializedName());
        return tag;
    }

    @Override
    public Object getPrimaryKey() {
        return elementType;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(ElementalCraftApi.MODID,elementType.getSerializedName());
    }

    @Override
    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeUtf(elementType.getSerializedName());
    }

    @Override
    public Component computeDisplayName() {
        return ModLanguageProvider.ELEMENT_NAMES.get(elementType.getSerializedName()).get();
    }

    @Override
    public void addDrops(long l, List<ItemStack> list, Level level, BlockPos blockPos) {
    }

    

    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj==null||getClass() != obj.getClass())return false;
        return elementType == ((ElementKey) obj).elementType;
    }

    @Override
    public int hashCode() {
        return elementType.hashCode();
    }

}
