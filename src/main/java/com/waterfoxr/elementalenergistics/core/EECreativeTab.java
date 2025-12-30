package com.waterfoxr.elementalenergistics.core;

import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.data.ModLanguageProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EECreativeTab {
    private static final ResourceKey<CreativeModeTab> KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ElementalEnergistics.id("main"));
    private static final List<DeferredRegister<? extends Item>> ITEMS = new ArrayList<>();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ElementalEnergistics.MOD_ID);
    public static final String ELEMENTALENERGISTICS_TAB_STRING = "creativetab.example_tab";

    public static final Supplier<CreativeModeTab> ELEMENTALENERGISTICS_TAB =
            CREATIVE_MODE_TABS.register("elementalenergistics_tab", () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable(ELEMENTALENERGISTICS_TAB_STRING))
                    .icon(() -> EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.get().asItem().getDefaultInstance())
                    .displayItems((pParameters, pOutput) -> {

                        EEItems.ITEM_DEFERRED_REGISTER.getEntries().forEach(item-> pOutput.accept(item.get().asItem()));
                        EEItems.AllAe2Item.forEach(aeItem-> pOutput.accept(aeItem.asItem()));
                        EEBlocks.BLOCKS.getEntries().forEach(block->pOutput.accept(block.get().asItem()));

                    })
                    .build());

    static void register(RegisterEvent registerEvent) {
        CreativeModeTab tab = CreativeModeTab.builder()
                .title(ModLanguageProvider.TAB_NAME.get())
                .icon(() -> EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.get().asItem().getDefaultInstance())
                .build();

        registerEvent.register(Registries.CREATIVE_MODE_TAB, KEY.location(), () -> tab);
    }
}
