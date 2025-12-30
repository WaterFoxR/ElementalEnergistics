package com.waterfoxr.elementalenergistics.utils;

import appeng.api.networking.IGrid;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.me.helpers.IGridConnectedBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class EEUtils {
    public static void ClientPlayerTell( Object o){
        Player player = Minecraft.getInstance().player;
        if(player == null)return;


        player.displayClientMessage(o instanceof Component? (Component) o :Component.nullToEmpty(String.valueOf(o))
        ,false);
    }

    public static IGrid getGrid(Object a, Direction side) {
        if (a instanceof IGridConnectedBlockEntity ba) {
            var gn = ba.getGridNode();
            return gn == null ? null : gn.getGrid();
        } else if (a instanceof IInWorldGridNodeHost ha) {
            var gn = ha.getGridNode(side);
            return gn == null ? null : gn.getGrid();
        }
        return null;
    }
}
