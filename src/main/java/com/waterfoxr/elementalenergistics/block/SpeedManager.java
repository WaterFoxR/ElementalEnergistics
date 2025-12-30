package com.waterfoxr.elementalenergistics.block;

import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;

public class SpeedManager {
    public static void setInstrumentSpeed(AbstractInstrumentBlockEntity instrument, float multiplier) {
        if (instrument instanceof SpeedMultipliable speedMultipliable) {
            speedMultipliable.setSpeedMultiplier(multiplier);
        }
    }

    public static float getInstrumentSpeed(AbstractInstrumentBlockEntity instrument) {
        if (instrument instanceof SpeedMultipliable speedMultipliable) {
            return speedMultipliable.getSpeedMultiplier();
        }
        return 1.0F; // 默认倍数
    }
}