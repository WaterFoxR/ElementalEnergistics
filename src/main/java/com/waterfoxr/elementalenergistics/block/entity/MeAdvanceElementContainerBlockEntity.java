package com.waterfoxr.elementalenergistics.block.entity;

import appeng.api.config.Actionable;
import appeng.api.networking.*;
import appeng.api.networking.security.IActionSource;
import appeng.api.util.AECableType;
import appeng.hooks.ticking.TickHandler;
import appeng.me.InWorldGridNode;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import com.waterfoxr.elementalenergistics.EEConfig;
import com.waterfoxr.elementalenergistics.block.MeAdvanceElementContainerBlock;
import com.waterfoxr.elementalenergistics.core.EEBlocks;
import com.waterfoxr.elementalenergistics.core.EEEntityType;
import com.waterfoxr.elementalenergistics.element.MeElementStorage;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

public class MeAdvanceElementContainerBlockEntity extends AbstractElementContainerBlockEntity
        implements IInWorldGridNodeHost, IGridConnectedBlockEntity{

    private boolean setChangedQueued = false;

    private final IManagedGridNode mainNode = GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE)
            .setFlags(GridFlags.REQUIRE_CHANNEL)
            .setVisualRepresentation(EEBlocks.ME_ADVANCE_ELEMENT_CONTAINER_BLOCK.get())
            .setInWorldNode(true)
            .setExposedOnSides(EnumSet.complementOf(EnumSet.of(Direction.UP,Direction.DOWN)))
            .setTagName("proxy");

    public final IActionSource actionSource = IActionSource.ofMachine(mainNode::getNode);

    public MeAdvanceElementContainerBlockEntity(BlockPos pos, BlockState state) {
        super(EEEntityType.ME_ADVANCE_ELEMENT_CONTAINER, pos, state, (self) ->
                new MeElementStorage((MeAdvanceElementContainerBlockEntity) self, EEConfig.COMMON.meContainerCapacity.get(), self::setChanged));
        this.getMainNode().setIdlePowerUsage(4);
    }

//    @Override
//    public @NotNull <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
//        return this.isConnectME()?LazyOptional.of(() -> this.elementStorage).cast():LazyOptional.empty();
//    }



    @Override
    public @NotNull BlockPos getBlockPos() {
        return this.worldPosition;
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return EEEntityType.ME_ADVANCE_ELEMENT_CONTAINER.get();
    }

    @Override
    public @Nullable IGridNode getGridNode() {
        return this.getMainNode().getNode();
    }


    public boolean isConnectME(){
        return getMainNode().getNode()!=null;
    }

    public ElementType getSetType(){
        return this.getBlockState().getValue(MeAdvanceElementContainerBlock.ELEMENT_TYPE);
    }

    public long getMEElementAmount(ElementType type){
        if(!isConnectME())return 0;
        return Objects.requireNonNull(this.getMainNode().getNode()).getGrid()
                .getStorageService().getInventory().getAvailableStacks().get(ElementKey.of(type));
    }



    @Override
    public IManagedGridNode getMainNode() {
        return this.mainNode;
    }

    @Override
    public boolean isSmall() {
        return false;
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

    @Override
    public IGridNode getGridNode(Direction dir) {
        var node = this.getMainNode().getNode();
        return node instanceof InWorldGridNode inWorldNode && inWorldNode.isExposedOnSide(dir) ? node : null;
    }

    @Override
    public void saveChanges() {
        if(this.level==null)return;
        if(this.level.isClientSide){
            this.setChanged();
        }
        else{
            this.level.blockEntityChanged(this.worldPosition);
            if(!this.setChangedQueued){
                TickHandler.instance().addCallable(null,this::setChangedAtEndOfTick);
                this.setChangedQueued=true;
            }
        }
    }



    private void setChangedAtEndOfTick(){
        this.setChanged();
        this.setChangedQueued = false;
    }

    public void onReady(){
        this.getMainNode().create(getLevel(),getBlockPos());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(this.getGridNode() != null && this.elementStorage.getElementAmount()>0){
            this.getGridNode().getGrid().getStorageService().getInventory().insert(ElementKey.of(this.elementStorage.getElementType()),this.elementStorage.getElementAmount(),Actionable.MODULATE,actionSource);
        }
        this.getMainNode().destroy();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        GridHelper.onFirstTick(this, MeAdvanceElementContainerBlockEntity::onReady);
    }

}
