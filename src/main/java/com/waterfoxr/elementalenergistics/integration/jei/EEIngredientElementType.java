package com.waterfoxr.elementalenergistics.integration.jei;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.network.chat.Component;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import java.util.List;

public record EEIngredientElementType(ElementType elementType,int amount, int elementAmount) implements IElementTypeProvider {
    public static final IIngredientType<EEIngredientElementType> TYPE = () -> EEIngredientElementType.class;

    public EEIngredientElementType(ElementType elementType,int amount, int elementAmount) {
        this.elementType = elementType;
        this.amount = amount;
        this.elementAmount = elementAmount;
    }

    public ElementType getElementType() {
        return this.elementType;
    }

    public int getElementAmount(){return this.elementAmount;}

    public Component getDisplayName() {
        return this.elementType.getDisplayName();
    }

    public EEIngredientElementType copy() {
        return new EEIngredientElementType(this.elementType,this.amount, this.elementAmount);
    }

    public static List<EEIngredientElementType> all() {
        return all(-1);
    }

    public static List<EEIngredientElementType> all(int amount) {
        return ElementType.ALL_VALID.stream().map((type) -> new EEIngredientElementType(type, amount,1)).toList();
    }
}
