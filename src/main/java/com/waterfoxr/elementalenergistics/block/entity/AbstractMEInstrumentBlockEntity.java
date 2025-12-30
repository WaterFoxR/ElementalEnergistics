package com.waterfoxr.elementalenergistics.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

@Deprecated
public abstract class AbstractMEInstrumentBlockEntity<T extends IInstrument, R extends IInstrumentRecipe<T>> extends AbstractInstrumentBlockEntity<T,R>{

    private int multiSpeed = 1;
    private int progress = 0;

    protected AbstractMEInstrumentBlockEntity(Config<T, R> config, BlockPos pos, BlockState state) {
        super(config, pos, state);
    }


    public int getMultiSpeed() {
        return multiSpeed;
    }

    // MultiSpeed 的数值被限制在 1 到 100 之间
    public void setMultiSpeed(int multiSpeed) {
        this.multiSpeed = Mth.clamp(multiSpeed,1,100);
    }

    private int transferMultiSpeed() {
        return this.transferSpeed*this.multiSpeed;
    }

    private int ceilTransfer(ISingleElementStorage container, int transfer) {
        int max = container.getElementAmount();
        if (transfer >= max) {
            if (this.progress + max < ((IInstrumentRecipe)this.recipe).getElementAmount()) {
                transfer = max - 1;
            } else {
                transfer = max;
            }
        }

        return transfer;
    }

    @Override
    protected boolean makeProgress() {
        ISingleElementStorage container = this.getContainer();
        if (this.recipe != null && this.progress >= ((IInstrumentRecipe)this.recipe).getElementAmount()) {
            this.process();
            this.progress = 0;
            return true;
        } else if (this.isRecipeAvailable() && container != null) {
            float preservation = this.runeHandler.getElementPreservation();
            int oldProgress = this.progress;
            int transfer = this.ceilTransfer(container, Math.round(this.runeHandler.getTransferSpeed((float)this.transferSpeed) / preservation));
            this.progress = (int)((float)this.progress + (float)container.extractElement(transfer, this.getRecipeElementType(), false) * preservation);
            if (this.level.isClientSide && this.progress > 0 && this.getProgressRounded((float)this.transferSpeed, (float)this.progress) > this.getProgressRounded((float)this.transferSpeed, (float)oldProgress)) {
                ParticleHelper.createElementFlowParticle(this.getElementType(), this.level, Vec3.atCenterOf(this.worldPosition).add(this.particleOffset), Direction.UP, 1.0F, this.level.random);
                this.renderProgressParticles();
            }

            return true;
        } else {
            if (this.recipe == null) {
                this.progress = 0;
            }

            return false;
        }
    }
}
