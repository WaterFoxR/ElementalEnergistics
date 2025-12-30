package com.waterfoxr.elementalenergistics.event;

import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEEntityType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.block.container.ContainerRenderer;
import sirttas.elementalcraft.block.instrument.binder.BinderRenderer;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ElementalEnergistics.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        ElementalEnergistics.LOGGER.info("HELLO from server starting");
    }


    @SubscribeEvent
    public static void registerGhostForContainerRender(ModelEvent.RegisterGeometryLoaders evt){
        register(EEEntityType.ME_ELEMENT_CONTAINER.get(), ContainerRenderer::new);
        register(EEEntityType.ME_ADVANCE_ELEMENT_CONTAINER.get(), ContainerRenderer::new);
        register(EEEntityType.ME_IMPROVED_BINDER.get(), BinderRenderer::new);
    }

    public static <T extends BlockEntity> void register(BlockEntityType<T> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
        BlockEntityRenderers.register(type, d -> renderProvider.get());
    }


}
