package com.waterfoxr.elementalenergistics.core;

import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.block.*;
import com.waterfoxr.elementalenergistics.data.ModLanguageProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class EEBlocks {
    static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ElementalEnergistics.MOD_ID);

    public static final RegistryObject<? extends Block> ME_ELEMENT_CONTAINER_BLOCK =
            registerBlock("me_element_container", "ME Element Container", MeElementContainerBlock::new);

    public static final RegistryObject<? extends Block> ME_ADVANCE_ELEMENT_CONTAINER_BLOCK =
            registerBlock("me_advance_element_container", "ME ADVANCE Element Container", MeAdvanceElementContainerBlock::new);


    public static final RegistryObject<? extends Block> PROXY_OUTPUT_RETRIEVER_BLOCK =
            registerBlock("proxy_output_retriever","Item - Element Proxy Output Retriever", ProxyOutputRetrieverBlock::new);

//    public static final RegistryObject<? extends Block> ME_BINDER_BLOCK =
//            registerBlock("me_binder", "ME Binder", MEBinderBlock::new);

    public static final RegistryObject<? extends Block> ME_IMPROVED_BINDER_BLOCK =
            registerBlock("me_improved_binder", "ME Improved Binder", MEImprovedBinderBlock::new);

    private static <T extends Block> void registryBlockItem(String id,RegistryObject<T> block){
        EEItems.ITEM_DEFERRED_REGISTER.register(id,()->new BlockItem(block.get(),new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String id, String name, Supplier<T> blockSupplier) {
        RegistryObject<T> blocks = BLOCKS.register(id, blockSupplier);
        registryBlockItem(id,blocks);
        ModLanguageProvider.LangEntry.of("block", id, EERegistration.getNameOrFormatId(id, name));
        return blocks;
    }
    public static void registerBlock(IEventBus eventBus){
       EEBlocks.BLOCKS.register(eventBus);
    }
}
