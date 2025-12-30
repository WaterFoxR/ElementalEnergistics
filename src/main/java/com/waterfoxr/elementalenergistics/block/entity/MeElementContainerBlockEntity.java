package com.waterfoxr.elementalenergistics.block.entity;

import appeng.api.config.Actionable;
import appeng.api.networking.*;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.api.util.AECableType;
import appeng.hooks.ticking.TickHandler;
import appeng.me.InWorldGridNode;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import com.google.common.primitives.Ints;
import com.waterfoxr.elementalenergistics.EEConfig;
import com.waterfoxr.elementalenergistics.block.MeElementContainerBlock;
import com.waterfoxr.elementalenergistics.core.EEBlocks;
import com.waterfoxr.elementalenergistics.core.EEEntityType;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MeElementContainerBlockEntity extends AbstractElementContainerBlockEntity
        implements IInWorldGridNodeHost, IGridConnectedBlockEntity{


    public int num_MAX;
    public int numToLoad;
    private boolean setChangedQueued = false;

    private final IManagedGridNode mainNode = GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE)
            .setFlags(GridFlags.REQUIRE_CHANNEL)
            .setVisualRepresentation(EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.get())
            .setInWorldNode(true)
            .setExposedOnSides(EnumSet.complementOf(EnumSet.of(Direction.UP,Direction.DOWN)))
            .setTagName("proxy");

    private final IActionSource actionSource = IActionSource.ofMachine(mainNode::getNode);

    public MeElementContainerBlockEntity(BlockPos pos, BlockState state) {
        super(EEEntityType.ME_ELEMENT_CONTAINER, pos, state, (self) -> new SingleElementStorage(EEConfig.COMMON.meContainerCapacity.get(), self::setChanged));
        this.getMainNode().setIdlePowerUsage(2);
        this.numToLoad = 0;
        this.num_MAX = EEConfig.COMMON.meContainerTickToLoad.get();
    }

    @Override
    public @NotNull <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @org.jetbrains.annotations.Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    @Override
    public @NotNull BlockPos getBlockPos() {
        return this.worldPosition;
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return EEEntityType.ME_ELEMENT_CONTAINER.get();
    }

    @Override
    public @Nullable IGridNode getGridNode() {
        return this.getMainNode().getNode();
    }

    private ElementType getElementType(){
        return this.elementStorage.getElementType();
    }

    /**
     * 尝试将ME元素容器内所有元素返回到ME网络中
     */
    public void unloadElementToME(){
        if(
                this.getGridNode() !=null &&
                this.elementStorage.getElementType()!=ElementType.NONE &&
                this.elementStorage.getElementAmount()!=0
        ){
            this.getGridNode().getGrid().getStorageService().getInventory()
                    .insert(ElementKey.of(this.elementStorage.getElementType()),this.elementStorage.getElementAmount(), Actionable.MODULATE,actionSource);
            this.elementStorage.extractElement(this.elementStorage.getElementAmount(),false);
            this.elementStorage.markDirty();
        }
    }

    public void loadElementFromME(){
        if(this.getGridNode()==null)return;
        ElementType setType = this.getBlockState().getValue(MeElementContainerBlock.ELEMENT_TYPE).getElementType();
        if(setType == ElementType.NONE)return;
        if(getElementType()!=ElementType.NONE && getElementType()!=setType){
            unloadElementToME();
        }
        MEStorage ME_Inv =  this.getGridNode().getGrid().getStorageService().getInventory();
        AEKey elementKey = ElementKey.of(setType);
        long toExtract = ME_Inv.getAvailableStacks().get(elementKey);
        if(toExtract==0)return;
        long noExtracted = this.elementStorage.insertElement(Ints.saturatedCast(toExtract),setType,false);
        ME_Inv.extract(elementKey,toExtract-noExtracted,Actionable.MODULATE,actionSource);
        this.elementStorage.markDirty();
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
        GridHelper.onFirstTick(this, MeElementContainerBlockEntity::onReady);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("num",numToLoad);
        tag.putInt("num_MAX",num_MAX);
        tag.putInt("num_MAX_config",EEConfig.COMMON.meContainerTickToLoad.get());
    }

    public void tickServer(){
        numToLoad = numToLoad+1>num_MAX?0:numToLoad+1;
        if(numToLoad==num_MAX){
            loadElementFromME();
            this.sendUpdate();
        }
    }
}
