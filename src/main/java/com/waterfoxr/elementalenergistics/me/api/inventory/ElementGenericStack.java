package com.waterfoxr.elementalenergistics.me.api.inventory;

import appeng.api.stacks.GenericStack;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

/*
 * Author: Almost Reliable, rlnt
 * Source: Applied Elemental (https://github.com/AlmostReliable/appliedelemental)
 * License: ARR - Used with permission. All rights reserved by the original author.
 */

/**
 * Utility class to get the first available element from an {@link IElementStorage}.
 *
 * @param elementType The element type that was found.
 * @param amount      The amount of the element that was found.
 */
public record ElementGenericStack(ElementType elementType, int amount) {

    public static final ElementGenericStack EMPTY = new ElementGenericStack(ElementType.NONE, 0);

    public static ElementGenericStack of(IElementStorage handler) {
        if (handler.isEmpty()) return EMPTY;

        for (ElementType elementType : ElementType.values()) {
            int amount = handler.getElementAmount(elementType);
            if (amount <= 0) continue;
            return new ElementGenericStack(elementType, amount);
        }

        return EMPTY;
    }

    public ElementKey toKey() {
        return new ElementKey(elementType);
    }

    public GenericStack toGenericStack() {
        return new GenericStack(toKey(), amount);
    }

    public boolean isEmpty() {
        return this == EMPTY || elementType == ElementType.NONE || amount == 0;
    }

    public ElementType getElementType(){return elementType;};

    public int getElementAmount(){return amount;};
}