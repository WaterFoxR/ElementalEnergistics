package com.waterfoxr.elementalenergistics.core;

import appeng.api.client.AEKeyRendering;
import appeng.api.client.StorageCellModels;
import appeng.api.features.P2PTunnelAttunement;
import appeng.api.stacks.AEKeyTypes;
import appeng.api.storage.StorageCells;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.data.ModDataGeneration;
import com.waterfoxr.elementalenergistics.data.ModLanguageProvider;
import com.waterfoxr.elementalenergistics.me.api.behaviors.ContainerElementStrategy;
import com.waterfoxr.elementalenergistics.me.api.client.ElementStackRenderer;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKeyType;
import com.waterfoxr.elementalenergistics.me.api.storage.ElementCellGuiHandler;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;

public final class EERegistration {
    private EERegistration(){}

    public static void init(IEventBus modEventBus){
        EEItems.ITEM_DEFERRED_REGISTER.register(modEventBus);
        EEItems.initAe2Item(ForgeRegistries.ITEMS);

        EEBlocks.registerBlock(modEventBus);

        modEventBus.addListener(ModDataGeneration::init);

        EEEntityType.register(modEventBus);

//        EEMenus.register(modEventBus);

        EECreativeTab.CREATIVE_MODE_TABS.register(modEventBus);


        modEventBus.addListener(EERegistration::onRegisterEvent);
        modEventBus.addListener(EERegistration::onCommonSetupEvent);
        modEventBus.addListener(EECreativeTab::register);



        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(EEClientRegistration::onClientSetup);
        }
    }

    private static void onRegisterEvent(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.CREATIVE_MODE_TAB) {
            EECreativeTab.register(event);
        }
        if (event.getRegistryKey() == Registries.ITEM) {
            AEKeyTypes.register(ElementKeyType.INSTANCE);
            ContainerElementStrategy.register();
        }
    }

    private static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StorageCells.addCellGuiHandler(new ElementCellGuiHandler());
            P2PTunnelAttunement.registerAttunementTag(EEItems.ELEMENT_P2P_TUNNEL);
            P2PTunnelAttunement.registerAttunementApi(EEItems.ELEMENT_P2P_TUNNEL,
                    ElementalCraftCapabilities.ELEMENT_STORAGE,
                    ModLanguageProvider.P2P_CAP_DESCRIPTION.get()
            );
            Upgrades.add(AEItems.SPEED_CARD, EEBlocks.ME_IMPROVED_BINDER_BLOCK.get(),3);
        });
    }



    public static String getNameOrFormatId(String id, @Nullable String name) {
        if (name == null) {
            String[] parts = id.split("_");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
            }
            return String.join(" ", parts);
        }
        return name;
    }

    private static class EEClientRegistration {

        private static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {

                EEItems.ae2Cells.forEach(cell->
                        StorageCellModels.registerModel(cell, ElementalEnergistics.id("block/element_cell"))
                );
                EEItems.ae2PortableCells.forEach(portableCell->
                        StorageCellModels.registerModel(portableCell, ElementalEnergistics.id("block/element_cell"))
                );

                AEKeyRendering.register(ElementKeyType.INSTANCE, ElementKey.class, new ElementStackRenderer());
            });
        }
    }
}
