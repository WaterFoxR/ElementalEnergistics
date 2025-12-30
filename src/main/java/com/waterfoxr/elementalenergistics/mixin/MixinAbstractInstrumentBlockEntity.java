package com.waterfoxr.elementalenergistics.mixin;

import com.waterfoxr.elementalenergistics.block.SpeedMultipliable;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;


@Mixin(AbstractInstrumentBlockEntity.class)
public abstract class MixinAbstractInstrumentBlockEntity <T extends IInstrument, R extends IInstrumentRecipe<T>> implements SpeedMultipliable {


    @Unique
    private float MULTISPEED = 1.0F;

    @Override
    @Unique
    public float getSpeedMultiplier() {
        return MULTISPEED;
    }

    @Override
    @Unique
    public float setSpeedMultiplier(float multiplier) {
        return MULTISPEED = Mth.clamp(multiplier, 1.0F, 100.0F);
    }

    // 重定向 runeHandler.getTransferSpeed(this.transferSpeed) 调用
    @Redirect(
            method = "makeProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lsirttas/elementalcraft/api/rune/handler/RuneHandler;getTransferSpeed(F)F"
            ),
            remap = false
    )
    private float redirectGetTransferSpeed(RuneHandler instance, float originalSpeed) {
        // 将 transferSpeed 乘以倍数
        return instance.getTransferSpeed(originalSpeed * MULTISPEED);
    }

    // 修改 runeHandler.getTransferSpeed 调用中的 transferSpeed
    @ModifyConstant(
            method = "makeProgress",
            constant = @Constant(
                    floatValue = 1.0F,
                    ordinal = 0 // 可能需要调整ordinal值
            ),
            remap = false
    )
    private float modifyTransferSpeedConstant(float constant) {
        return constant * MULTISPEED;
    }
}
