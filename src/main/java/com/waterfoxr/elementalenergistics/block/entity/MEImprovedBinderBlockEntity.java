package com.waterfoxr.elementalenergistics.block.entity;

import appeng.api.config.Actionable;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.*;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.api.util.AECableType;
import appeng.core.definitions.AEItems;
import appeng.hooks.ticking.TickHandler;
import appeng.me.InWorldGridNode;
import appeng.me.helpers.BlockEntityNodeListener;
import appeng.me.helpers.IGridConnectedBlockEntity;
import appeng.util.inv.InternalInventoryHost;
import com.waterfoxr.elementalenergistics.block.SpeedMultipliable;
import com.waterfoxr.elementalenergistics.core.EEBlocks;
import com.waterfoxr.elementalenergistics.core.EEEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlockEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MEImprovedBinderBlockEntity extends ImprovedBinderBlockEntity implements IInWorldGridNodeHost, IGridConnectedBlockEntity, IUpgradeableObject,InternalInventoryHost,ISingleElementStorage {

    public ISingleElementStorage elementStorage;

//    private final AppEngInternalInventory inv = new AppEngInternalInventory(this, 1);
    private final IUpgradeInventory upgrades;

    private final IManagedGridNode mainNode = GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE)
            .setFlags(GridFlags.REQUIRE_CHANNEL)
            .setVisualRepresentation(EEBlocks.ME_IMPROVED_BINDER_BLOCK.get())
            .setInWorldNode(true)
            .setExposedOnSides(EnumSet.complementOf(EnumSet.of(Direction.DOWN)))
            .setTagName("proxy");

    private boolean setChangedQueued = false;
    private final IActionSource actionSource = IActionSource.ofMachine(mainNode::getNode);

    public MEImprovedBinderBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        this.elementStorage=new SingleElementStorage(100000,this::setChangedAtEndOfTick);
        this.getMainNode().setIdlePowerUsage(4);
        this.upgrades = UpgradeInventories.forMachine(EEBlocks.ME_IMPROVED_BINDER_BLOCK.get(), 3, this::setChanged);


    }

    public boolean hasRecipe() {
        return this.recipe == null;
    }

    public int getRecipeElementAmount(){
        if (this.recipe == null) {
            // 防止除0错误
            return 1;
        }
        return this.recipe.getElementAmount();
    }

    public ItemStack getRecipeOutput(){
        if (this.level != null && this.recipe != null) {
            return this.recipe.getResultItem(this.level.registryAccess());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void clearContent() {
        this.upgrades.clear();
        super.clearContent();
    }

    @Override
    public ISingleElementStorage getContainer() {
        return this.elementStorage;
    }

    @Override
    public @NotNull <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if(level == null)return super.getCapability(cap,side);
        if(cap.equals(ElementalCraftCapabilities.ELEMENT_STORAGE) && !this.remove){
            return this.elementStorage == null?
                    LazyOptional.empty():LazyOptional.of(()->this.elementStorage).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected boolean makeProgress() {
        ((SpeedMultipliable)this).setSpeedMultiplier((float) Math.pow(4,this.getInstalledUpgrades(AEItems.SPEED_CARD)));
        return super.makeProgress();
    }

    @Override
    protected void retrieve() {
        if(level == null)super.retrieve();

        //如果连接到ME网络则优先把产物返回给ME网络
        if(this.getGridNode() != null) {
            MEStorage meStorage = this.getGridNode().getGrid().getStorageService().getInventory();
            if (this.retrieveAll) {
                for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
                    ItemStack item = this.getInventory().getItem(i);
                    long inserted = meStorage.insert(AEItemKey.of(item), item.getCount(), Actionable.MODULATE, actionSource);
                    this.getInventory().removeItem(i, (int) inserted);
                }
            } else {
                ItemStack item = this.getInventory().getItem(this.outputSlot);
                long inserted = meStorage.insert(AEItemKey.of(item), item.getCount(), Actionable.MODULATE, actionSource);
                this.getInventory().removeItem(this.outputSlot, (int) inserted);
            }
        }
        //如果没有连接到ME网络则按照原本物品导出器的模式导出物品
        else super.retrieve();
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return this.upgrades;
    }

    @Override
    public @NotNull BlockPos getBlockPos() {
        return this.worldPosition;
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return EEEntityType.ME_IMPROVED_BINDER.get();
    }

    @Override
    public @Nullable IGridNode getGridNode() {
        return this.getMainNode().getNode();
    }

    @Override
    public IManagedGridNode getMainNode() {
        return this.mainNode;
    }

    @Override
    public IGridNode getGridNode(Direction dir) {
        var node = this.getMainNode().getNode();
        return node instanceof InWorldGridNode inWorldNode && inWorldNode.isExposedOnSide(dir) ? node : null;
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.SMART;
    }

    public void onReady(){
        this.getMainNode().create(getLevel(),getBlockPos());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.getMainNode().destroy();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        GridHelper.onFirstTick(this, MEImprovedBinderBlockEntity::onReady);
    }


    private void setChangedAtEndOfTick(){
        this.setChanged();
        this.setChangedQueued = false;
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

    @Override
    public void onChangeInventory(InternalInventory inv, int slot) {
    }




    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        if(this.elementStorage != null) {
            var es = new CompoundTag();
            es.putInt(ECNames.ELEMENT_AMOUNT,this.elementStorage.getElementAmount());
            es.putInt(ECNames.ELEMENT_CAPACITY,this.elementStorage.getElementCapacity());
            es.putString(ECNames.ELEMENT_TYPE,this.elementStorage.getElementType().getSerializedName());
            tag.put(ECNames.ELEMENT_STORAGE,es);
        }
        this.upgrades.writeToNBT(tag,"upgrades");
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        if (tag.contains(ECNames.ELEMENT_STORAGE)) {
            var es = tag.getCompound(ECNames.ELEMENT_STORAGE);
            this.elementStorage.extractElement(this.getElementCapacity(),false);
            this.elementStorage.insertElement(
                    es.getInt(ECNames.ELEMENT_AMOUNT),
                    ElementType.byName(es.getString(ECNames.ELEMENT_TYPE)),
                    false);
        }
        this.upgrades.readFromNBT(tag, "upgrades");
        super.load(tag);
        this.setChanged();
    }

    @Override
    public int getElementAmount() {
        return this.elementStorage.getElementAmount();
    }

    @Override
    public int getElementCapacity() {
        return this.elementStorage.getElementCapacity();
    }

    @Override
    public int insertElement(int count, ElementType type, boolean simulate) {
        return this.elementStorage.insertElement(count,type,simulate);
    }

    @Override
    public int extractElement(int count, ElementType type, boolean simulate) {
        return this.elementStorage.extractElement(count,type,simulate);
    }
}
