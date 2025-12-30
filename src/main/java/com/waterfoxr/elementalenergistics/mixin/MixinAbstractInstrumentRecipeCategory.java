package com.waterfoxr.elementalenergistics.mixin;

import com.waterfoxr.elementalenergistics.integration.jei.NumProviderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.interaction.jei.category.instrument.AbstractInstrumentRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

import java.util.List;

@Mixin(AbstractInstrumentRecipeCategory.class)
public abstract class MixinAbstractInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>>{



    @Inject(
            method = "getElementTypeIngredients",
            at=@At("RETURN"),
            cancellable = true,
            remap = false
    )
    protected void getElementTypeIngredients(T recipe, CallbackInfoReturnable<List<IngredientElementType>> cir){
        List<IngredientElementType> list =  recipe.getValidElementTypes().stream()
                .map((t) -> {
                    IngredientElementType type =  new IngredientElementType(t, this.elementalEnergistics$getGaugeValue(recipe.getElementAmount()));
                    NumProviderHelper.setNum(type,recipe.getElementAmount());
                    return type;
                }).toList();
        cir.setReturnValue(list);
    }

    @Unique
    private int elementalEnergistics$getGaugeValue(int amount) {
        return (int)Math.log10((double)amount) - 1;
    }

}
