package com.waterfoxr.elementalenergistics.client;

import appeng.init.client.InitScreens;
import appeng.items.storage.BasicStorageCell;
import appeng.items.tools.powered.PortableCellItem;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEItems;
import com.waterfoxr.elementalenergistics.me.menu.MEImprovedBinderMenu;
import com.waterfoxr.elementalenergistics.me.menu.MEImprovedBinderScreen;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class EEClient {
    public static void initialize() {
        var bus = ElementalEnergistics.Bus;
        bus.addListener(EEClient::registerItemColors);



        // 注册ME进阶元素聚合器Screen
        InitScreens.register(MEImprovedBinderMenu.TYPE, MEImprovedBinderScreen::new,"/screens/me_improved_binder.json");
    }

    // 为存储元件加上状态指示LED
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for(var tier : EEItems.Ae2CellTier.values()){
            event.register(BasicStorageCell::getColor, EEItems.getCell(tier));
            event.register(PortableCellItem::getColor, EEItems.getPortableCell(tier));
        }
    }
}
