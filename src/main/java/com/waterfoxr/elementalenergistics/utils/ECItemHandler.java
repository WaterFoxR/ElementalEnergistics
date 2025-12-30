package com.waterfoxr.elementalenergistics.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;

import java.util.Objects;

public class ECItemHandler{
    protected ItemStack ECitemStack;
    protected EquipmentSlot slot;
    protected LivingEntity entity;
    public boolean isElementStorageItem;
    protected int ECConfigCapacity;

    @Deprecated
    public ECItemHandler( LivingEntity pEntity, EquipmentSlot pSlot){
        this.slot = pSlot;
        this.ECitemStack = pEntity.getItemBySlot(slot);
        this.entity = pEntity;
        this.ECConfigCapacity = this.GetItemECConfigCapacityByClassName();
        this.isElementStorageItem = ECConfigCapacity > 0;
        if(this.isElementStorageItem && ECitemStack.getTag() == null) {
            this.createEmptyTagForElementStorageItem();
        }
    }

    /**
     * 此方法需要写入最后的物品栈
     * @param stack 要处理的物品
     */
    public ECItemHandler(ItemStack stack){
        this.ECitemStack = stack.copyWithCount(1);
        this.slot = null;
        this.entity = null;
        this.ECConfigCapacity = this.GetItemECConfigCapacityByClassName();
        this.isElementStorageItem = ECConfigCapacity > 0;
        if(this.isElementStorageItem && ECitemStack.getTag() == null) {
            this.createEmptyTagForElementStorageItem();
        }
    }

    /**
     * @param number 要输入元素的量
     * @param type 输入的元素类型
     * @param sim 是否为模拟
     * @return 返回剩余无法输输出的元素量
     */
    public int insertElement(int number,ElementType type,boolean sim) {
        if (!isElementStorageItem || this.isFull()) return number;
        if (!Objects.equals(this.getElementTypeString(), ECNames.NONE)) {
            if (this.getElementType() != type) return number;
        }
        else {
            if (this.getElementType()!=ElementType.NONE && this.GetFixedElementType()!=type) return number;
        }
        //最多输入的量
        int num = Math.min(this.getElementCapacity() - this.getElementAmount(), number);
        if (!sim) {
            CompoundTag ec = new CompoundTag();
            ec.putInt(ECNames.ELEMENT_AMOUNT, this.getElementAmount() + num);
            ec.putString(ECNames.ELEMENT_TYPE, type.getSerializedName());
            this.getElementStorage().merge(ec);
            this.updateTagChange();
        }
        return number - num;
    }
    /**
     * @param number 要输出元素的量
     * @param sim 是否为模拟
     * @return 返回剩余无法输输出的元素量
     */
    public int extractElement(int number, boolean sim, @Nullable ElementType type) {
        if (
                !isElementStorageItem ||
                Objects.equals(this.getElementTypeString(), ECNames.NONE) ||
                this.isEmpty()
        ) return number;
        if(type != null && this.getElementType() != type)return number;
        int num = Math.min(this.getElementAmount(), number);
        if (!sim) {
            if (this.getElementAmount() - num == 0) {
                this.createEmptyTagForElementStorageItem();
            }
            else {
                this.getElementStorage().putInt(ECNames.ELEMENT_AMOUNT,num);
            }
            this.updateTagChange();
        }
        return number-num;
    }

    public CompoundTag getElementStorage(){
        if(ECitemStack.getTag() == null)this.createEmptyTagForElementStorageItem();
        return ECitemStack.getTag()
                    .getCompound(ECNames.BLOCK_ENTITY_TAG)
                    .getCompound(ECNames.ELEMENT_STORAGE);
    }

    public int getElementAmount(){
        return this.getElementStorage().getInt(ECNames.ELEMENT_AMOUNT);
    }

    public int getElementCapacity(){
        return this.getElementStorage().getInt(ECNames.ELEMENT_CAPACITY);
    }

    public String getElementTypeString(){
        return this.getElementStorage().getString(ECNames.ELEMENT_TYPE);
    }

    public ElementType getElementType(){
        return ElementType.byName(this.getElementTypeString());
    }

    public boolean isFull(){
        return this.getElementAmount()==this.getElementCapacity();
    }

    public boolean isEmpty(){
        return this.getElementType() == ElementType.NONE || this.getElementAmount() == 0;
    }

    public int getPercent(){
        if(!isElementStorageItem)return 0;
        return this.getElementAmount()*100 / this.getElementCapacity();
    }
    /**
     * 检查元素容器存储有没有指定的元素类型,有就返回FIRE\WATER\EARTH\AIR 没有就NONE
     * @return 如果需要String则额外使用".getSerializedName()"
     */
    public ElementType GetFixedElementType(){
        String id = ECitemStack.getItem().getDescriptionId();
        id = id.substring(id.indexOf("_")+1);
        return  ElementType.byName(id);
    }

    /**
     * 根据物品所属的类名，查询元素工艺的Config所设定的最大容量
     * @return 返回设定的最大容量Element_Capacity
     */
    public Integer GetItemECConfigCapacityByClassName(){
        Item item = ECitemStack.getItem();
        String ClassName = (item instanceof BlockItem)?((BlockItem) item).getBlock().getClass().getName():item.getClass().getName();
        return switch (ClassName) {
            case "sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock" -> ECConfig.COMMON.pedestalCapacity.get();//元素台座
            case "sirttas.elementalcraft.block.container.SmallElementContainerBlock" -> ECConfig.COMMON.tankSmallCapacity.get();//小型元素容器
            case "sirttas.elementalcraft.block.container.ElementContainerBlock" -> ECConfig.COMMON.tankCapacity.get();//元素容器
            case "sirttas.elementalcraft.block.container.reservoir.ReservoirBlock" -> ECConfig.COMMON.reservoirCapacity.get();//蓄积器
            case "sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock" -> 1000000;//创造元素容器
            case "sirttas.elementalcraft.item.holder.ElementHolderItem" -> ECConfig.COMMON.elementHolderCapacity.get();//元素球
            case "sirttas.elementalcraft.item.holder.PureElementHolderItem" -> ECConfig.COMMON.pureElementHolderCapacity.get();//至纯元素球
            default -> 0;
        };
    }

    /**
     * 处理没有NBT的物品，如果是元素容器则添加上NBT
     */
    private void createEmptyTagForElementStorageItem(){
        CompoundTag tag = new CompoundTag();
        tag.putInt(ECNames.ELEMENT_AMOUNT, 0);
        tag.putInt(ECNames.ELEMENT_CAPACITY, ECConfigCapacity);
        tag.putString(ECNames.ELEMENT_TYPE, this.GetFixedElementType().getSerializedName());

        CompoundTag store = new CompoundTag();
        store.put(ECNames.ELEMENT_STORAGE, tag);

        CompoundTag nbt = new CompoundTag();
        nbt.put(ECNames.BLOCK_ENTITY_TAG, store);

        this.ECitemStack.setTag(nbt);
        this.updateTagChange();
    }

    /**
     * 写入处理结果
     */
    public void updateTagChange(){
        if(entity != null && slot !=null) entity.setItemSlot(slot,ECitemStack);
    }

    public CompoundTag getHandlerTag(){
        return this.ECitemStack.getTag();
    }

    public ItemStack getHandledItemStack(){
        return this.ECitemStack;
    }
}
