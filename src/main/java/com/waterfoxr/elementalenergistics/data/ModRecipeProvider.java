package com.waterfoxr.elementalenergistics.data;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

import java.util.Locale;
import java.util.function.Consumer;

public class ModRecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EEItems.ELEMENT_CELL_HOUSING)
                .pattern("ABA")
                .pattern("B B")
                .pattern("CDC")
                .define('A', ECBlocks.BURNT_GLASS.get())
                .define('B', ECItems.DRENCHED_IRON_INGOT.get())
                .define('C', ECItems.CONTAINED_CRYSTAL.get())
                .define('D', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_dusts/redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(consumer, ElementalEnergistics.id("element_cell_housing"));

        var housing = EEItems.ELEMENT_CELL_HOUSING;

        for (var tier : EEItems.Ae2CellTier.values()) {
            var cellComponent = switch (tier) {
                case _1K -> AEItems.CELL_COMPONENT_1K;
                case _4K -> AEItems.CELL_COMPONENT_4K;
                case _16K -> AEItems.CELL_COMPONENT_16K;
                case _64K -> AEItems.CELL_COMPONENT_64K;
                case _256K -> AEItems.CELL_COMPONENT_256K;
            };

            var tierName = tier.toString().toLowerCase(Locale.ROOT);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EEItems.getCell(tier))
                    .requires(housing)
                    .requires(cellComponent)
                    .unlockedBy("has_cell_component" + tierName, has(cellComponent))
                    .save(consumer);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EEItems.getPortableCell(tier))
                    .requires(AEBlocks.CHEST)
                    .requires(cellComponent)
                    .requires(AEBlocks.ENERGY_CELL)
                    .requires(housing)
                    .unlockedBy("has_" + housing.id().getPath(), has(housing))
                    .unlockedBy("has_energy_cell", has(AEBlocks.ENERGY_CELL))
                    .save(consumer);
        }
    }
}
