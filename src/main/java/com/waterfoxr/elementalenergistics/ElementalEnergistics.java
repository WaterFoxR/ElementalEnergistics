package com.waterfoxr.elementalenergistics;

import appeng.capabilities.Capabilities;
import com.mojang.logging.LogUtils;
import com.waterfoxr.elementalenergistics.client.EEClient;
import com.waterfoxr.elementalenergistics.core.EERegistration;
import com.waterfoxr.elementalenergistics.me.api.behaviors.GenericInternalInventoryWrapper;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixins;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;

@Mod(ElementalEnergistics.MOD_ID)
public class ElementalEnergistics {

    public static final String MOD_ID = "elementalenergistics";
    public static final String MOD_NAME = "ElementalEnergistics";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus Bus;

    public ElementalEnergistics(FMLJavaModLoadingContext context){
        IEventBus modEventBus = context.getModEventBus();
        Bus = modEventBus;

        EERegistration.init(modEventBus);
        context.registerConfig(ModConfig.Type.COMMON,EEConfig.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class,this::registerCapabilities);


        Mixins.addConfiguration("elementalenergistics.mixins.json");

        DistExecutor.safeRunWhenOn(Dist.CLIENT,()-> EEClient::initialize);
    }

    private void registerCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        var blockEntity = event.getObject();
        event.addCapability(ElementalEnergistics.id("generic_inv_wrapper"), new ICapabilityProvider() {
            @Override
            @NotNull
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
                if (capability == ElementalCraftCapabilities.ELEMENT_STORAGE) {
                    return blockEntity.getCapability(Capabilities.GENERIC_INTERNAL_INV, side)
                            .lazyMap(GenericInternalInventoryWrapper::new).cast();
                }
                return LazyOptional.empty();
            }
        });
    }

    public static IEventBus getBus() {
        return Bus;
    }

    public static ResourceLocation id(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID,path);
    }
}
