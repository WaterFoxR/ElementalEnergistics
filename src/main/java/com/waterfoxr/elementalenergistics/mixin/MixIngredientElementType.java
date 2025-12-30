package com.waterfoxr.elementalenergistics.mixin;

import com.waterfoxr.elementalenergistics.integration.jei.INumProvider;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

@Implements({
        @Interface(
                iface = INumProvider.class,
                prefix = "elementalEnergistics$",
                remap = Interface.Remap.NONE
        )
})
@Mixin(IngredientElementType.class)
public abstract class MixIngredientElementType implements IElementTypeProvider {

    @Unique
    private int elementalEnergistics$num;


    @Unique
    public int elementalEnergistics$getNum() {
        return this.elementalEnergistics$num;
    }


    @Unique
    public void elementalEnergistics$setNum(int num) {
        this.elementalEnergistics$num = num;
    }


}
