package com.waterfoxr.elementalenergistics.me.api.behaviors;

import appeng.api.behaviors.GenericInternalInventory;
import appeng.api.config.Actionable;
import com.google.common.primitives.Ints;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
/*
 * Author: Almost Reliable, rlnt
 * Source: Applied Elemental (https://github.com/AlmostReliable/appliedelemental)
 * License: ARR - Used with permission. All rights reserved by the original author.
 */
@SuppressWarnings("UnstableApiUsage")
public class GenericInternalInventoryWrapper implements IElementStorage, IElementTypeProvider {

    private final GenericInternalInventory internalInventory;

    public GenericInternalInventoryWrapper(GenericInternalInventory internalInventory) {
        this.internalInventory = internalInventory;
    }

    @Override
    public int getElementAmount(ElementType type) {
        int slot = findSlotWithElement(type);
        return slot == -1 ? 0 : Ints.saturatedCast(internalInventory.getAmount(slot));
    }

    @Override
    public int getElementCapacity(ElementType type) {
        return Ints.saturatedCast(internalInventory.getMaxAmount(new ElementKey(type)));
    }

    @Override
    public int insertElement(int count, ElementType type, boolean simulate) {
        return performTransfer(type, count, simulate, true);
    }

    @Override
    public int extractElement(int count, ElementType type, boolean simulate) {
        return performTransfer(type, count, simulate, false);
    }

    @Override
    public ElementType getElementType() {
        for (int slot = 0; slot < internalInventory.size(); slot++) {
            if (!(internalInventory.getKey(slot) instanceof ElementKey elementKey)) continue;
            ElementType elementType = (ElementType) elementKey.getPrimaryKey();
            if (elementType != ElementType.NONE) {
                return elementType;
            }
        }

        return ElementType.NONE;
    }

    private int findSlotWithElement(ElementType type) {
        for (int slot = 0; slot < internalInventory.size(); slot++) {
            if (!(internalInventory.getKey(slot) instanceof ElementKey elementKey)) continue;
            if (elementKey.getPrimaryKey() == type) {
                return slot;
            }
        }

        return -1;
    }

    private int performTransfer(ElementType elementType, int count, boolean simulate, boolean insert) {
        if (count <= 0 || elementType == ElementType.NONE) return 0;

        ElementKey elementKey = new ElementKey(elementType);
        Actionable mode = Actionable.ofSimulate(simulate);
        int toTransfer = count;
        int transferred = 0;

        for (int slot = 0; slot < internalInventory.size(); slot++) {
            if (!insert && !elementKey.equals(internalInventory.getKey(slot))) continue;

            int result = Ints.saturatedCast(insert ?
                    internalInventory.insert(slot, elementKey, toTransfer, mode) :
                    internalInventory.extract(slot, elementKey, toTransfer, mode)
            );
            toTransfer -= result;
            transferred += result;

            if (toTransfer <= 0) break;
        }

        return insert ? count - transferred : transferred;
    }
}