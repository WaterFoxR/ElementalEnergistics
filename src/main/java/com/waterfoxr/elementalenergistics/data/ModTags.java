package com.waterfoxr.elementalenergistics.data;

import appeng.api.features.P2PTunnelAttunement;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEBlocks;
import com.waterfoxr.elementalenergistics.core.EEItems;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.tag.ECTags;

import java.util.concurrent.CompletableFuture;

public class ModTags {
    private ModTags(){}

    static void initTagProviders(boolean run, DataGenerator generator, PackOutput output, ExistingFileHelper existingFileHelper) {
        var lookupFilter = CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor());
        var blockTags = generator.addProvider(run, new Blocks(output, lookupFilter, existingFileHelper));
        generator.addProvider(run, new Items(output, lookupFilter, blockTags.contentsGetter(),ElementalEnergistics.MOD_ID,null));
    }

    private static final class Items extends ItemTagsProvider{


        public Items(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {
            var elementP2PTag = tag(P2PTunnelAttunement.getAttunementTag(EEItems.ELEMENT_P2P_TUNNEL));
        elementP2PTag.addOptionalTag(ECTags.Items.SHARDS)
                    .addOptionalTag(ECTags.Items.CRYSTALS)
                    .addOptionalTag(ECTags.Items.LENSES)
                    .addOptionalTag(ECTags.Items.PIPES)
                    .addOptional(ECBlocks.SMALL_CONTAINER.getId())
                    .addOptional(ECBlocks.CONTAINER.getId())
                    .addOptional(ECBlocks.AIR_RESERVOIR.getId())
                    .addOptional(ECBlocks.EARTH_RESERVOIR.getId())
                    .addOptional(ECBlocks.FIRE_RESERVOIR.getId())
                    .addOptional(ECBlocks.WATER_RESERVOIR.getId())
                    .addOptional(EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.getId());

        }
    }
    private static final class Blocks extends BlockTagsProvider {

        private Blocks(
                PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper
        ) {
            super(output, lookupProvider, ElementalEnergistics.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).addOptional(EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.getId());
        }
    }
}
