package com.waterfoxr.elementalenergistics.event;

import appeng.items.storage.BasicStorageCell;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEItems;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Cancelable
@Mod.EventBusSubscriber(modid = ElementalEnergistics.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for(var tier : EEItems.Ae2CellTier.values()){
            event.register(BasicStorageCell::getColor, EEItems.getCell(tier));
            event.register(BasicStorageCell::getColor, EEItems.getPortableCell(tier));
        }
    }
}
