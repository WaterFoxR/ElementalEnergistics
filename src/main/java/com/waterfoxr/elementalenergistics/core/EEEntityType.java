package com.waterfoxr.elementalenergistics.core;

import com.mojang.datafixers.types.Type;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EEEntityType {
    private static final DeferredRegister<BlockEntityType<?>> DEFERRED_REGISTER;
    public static final RegistryObject<BlockEntityType<MeElementContainerBlockEntity>> ME_ELEMENT_CONTAINER;
    public static final RegistryObject<BlockEntityType<MeAdvanceElementContainerBlockEntity>> ME_ADVANCE_ELEMENT_CONTAINER;
    public static final RegistryObject<BlockEntityType<ProxyOutputRetrieverBlockEntity>> PROXY_OUTPUT_RETRIEVER;
    public static final RegistryObject<BlockEntityType<MEImprovedBinderBlockEntity>> ME_IMPROVED_BINDER;

    private EEEntityType() {
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }




    static {
        DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ElementalEnergistics.MOD_ID);
        ME_ELEMENT_CONTAINER = DEFERRED_REGISTER.register("me_element_container",
                ()->BlockEntityType.Builder.of(MeElementContainerBlockEntity::new, EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.get()).build((Type<?>)null) );
        ME_ADVANCE_ELEMENT_CONTAINER = DEFERRED_REGISTER.register("me_advance_element_container",
                ()->BlockEntityType.Builder.of(MeAdvanceElementContainerBlockEntity::new, EEBlocks.ME_ADVANCE_ELEMENT_CONTAINER_BLOCK.get()).build((Type<?>)null) );
        PROXY_OUTPUT_RETRIEVER = DEFERRED_REGISTER.register("capability_proxy",
                ()->BlockEntityType.Builder.of(ProxyOutputRetrieverBlockEntity::new, EEBlocks.PROXY_OUTPUT_RETRIEVER_BLOCK.get()).build((Type<?>)null) );
        ME_IMPROVED_BINDER = DEFERRED_REGISTER.register("me_improved_binder",
                ()->BlockEntityType.Builder.of(MEImprovedBinderBlockEntity::new, EEBlocks.ME_IMPROVED_BINDER_BLOCK.get()).build((Type<?>)null) );
    }
}
