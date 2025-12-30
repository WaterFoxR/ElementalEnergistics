package com.waterfoxr.elementalenergistics.mixin;

import com.waterfoxr.elementalenergistics.integration.jei.NumProviderHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.library.util.RecipeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.interaction.jei.category.AbstractBlockEntityRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.PureInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

import javax.annotation.Nonnull;

@Mixin(PureInfusionRecipeCategory.class)
public abstract class MixinPureInfusionRecipeCategory extends AbstractBlockEntityRecipeCategory<PureInfuserBlockEntity, PureInfusionRecipe> {
    protected MixinPureInfusionRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
        super(translationKey, icon, background);
    }

    /**
     * @author WaterFoxR
     * @reason 修改 elementIngredients 以包含 num 参数，从而使用JEI编写ae2样板能读取真正的元素消耗量
     */
    @Overwrite(
            remap = false
    )
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull PureInfusionRecipe recipe, @Nonnull IFocusGroup focuses) {
        var ingredients = recipe.getIngredients();

        // 修改这一行：使用 NumProviderHelper.allWithNum 替代 IngredientElementType.all
        var elementIngredients = NumProviderHelper.allWithNum(
                getGaugeValue(recipe.getElementAmount()),
                recipe.getElementAmount()
        );

        // 以下是原方法的其余部分，保持不变
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 61)
                .addIngredients(ingredients.get(0));

        // Left
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 61)
                .addIngredients(ingredients.get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 9, 61)
                .addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(0));

        // Top
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 27)
                .addIngredients(ingredients.get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 10)
                .addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(1));

        // Bottom
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 95)
                .addIngredients(ingredients.get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 112)
                .addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(2));

        // Right
        builder.addSlot(RecipeIngredientRole.INPUT, 94, 61)
                .addIngredients(ingredients.get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 111, 61)
                .addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(3));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 154, 61)
                .addItemStack(RecipeUtil.getResultItem(recipe));
    }

}
