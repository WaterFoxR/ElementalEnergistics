package com.waterfoxr.elementalenergistics.core;

import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.me.menu.MEImprovedBinderMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Deprecated
public class EEMenus {
    private static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ElementalEnergistics.MOD_ID);

    public static final RegistryObject<MenuType<MEImprovedBinderMenu>> ME_IMPROVED_BINDER =
            MENUS.register("me_improved_binder", () -> MEImprovedBinderMenu.TYPE);

    public static void register(IEventBus eventBus) {
        EEMenus.MENUS.register(eventBus);
    }
}
