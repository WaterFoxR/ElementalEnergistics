package com.waterfoxr.elementalenergistics.integration.jei;

import appeng.api.integrations.jei.IngredientConverters;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

@JeiPlugin
public class EEJEIPlugin implements IModPlugin {

    public static final mezz.jei.api.ingredients.IIngredientType<IngredientElementType> ELEMENT_TYPE =
            () -> IngredientElementType.class;

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        IngredientConverters.register(new ElementIngredientConverter(ELEMENT_TYPE));
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ElementalEnergistics.id("jei_plugin");
    }
}
