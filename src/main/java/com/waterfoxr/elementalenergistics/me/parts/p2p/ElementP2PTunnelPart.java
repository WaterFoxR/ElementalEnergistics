package com.waterfoxr.elementalenergistics.me.parts.p2p;

import appeng.api.config.PowerUnits;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.items.parts.PartModels;
import appeng.parts.p2p.CapabilityP2PTunnelPart;
import appeng.parts.p2p.P2PModels;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKeyType;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import java.util.ArrayList;
import java.util.List;
/*
 * Author: Almost Reliable, rlnt
 * Source: Applied Elemental (https://github.com/AlmostReliable/appliedelemental)
 * License: ARR - Used with permission. All rights reserved by the original author.
 */
public class ElementP2PTunnelPart extends CapabilityP2PTunnelPart<ElementP2PTunnelPart, IElementStorage> {

    private static final P2PModels MODELS = new P2PModels(ElementalEnergistics.id("part/p2p_tunnel_element"));
    private static final IElementStorage NULL_ELEMENT_HANDLER = new NullElementHandler();

    @PartModels
    public static List<IPartModel> getModels(){
        return MODELS.getModels();
    }

    @Override
    public IPartModel getStaticModels() {
        return MODELS.getModel(this.isPowered(),this.isActive());
    }

    public ElementP2PTunnelPart(IPartItem<?> partItem) {
        super(partItem, ElementalCraftCapabilities.ELEMENT_STORAGE);
        emptyHandler = NULL_ELEMENT_HANDLER;
        inputHandler = new InputElementHandler();
        outputHandler = new outputElementHandler();
    }

    private static class NullElementHandler implements IElementStorage{

        @Override
        public int getElementAmount(ElementType type) {
            return 0;
        }

        @Override
        public int getElementCapacity(ElementType type) {
            return 0;
        }

        @Override
        public int insertElement(int count, ElementType type, boolean simulate) {
            return count;
        }

        @Override
        public int extractElement(int count, ElementType type, boolean simulate) {
            return 0;
        }
    }

    private class InputElementHandler implements IElementStorage{

        @Override
        public int getElementAmount(ElementType type) {
            return 0;
        }

        @Override
        public int getElementCapacity(ElementType type) {
            return Integer.MAX_VALUE;
        }

        @Override
        public int insertElement(int count, ElementType type, boolean simulate) {
            //元素p2p输出的端口数
            int outputTunnels = getOutputs().size();
            if (outputTunnels == 0) return count;

            //获取每个可用的输出端
            List<IElementStorage> list = new ArrayList<>();
            for (ElementP2PTunnelPart target : getOutputs()) {
                try (CapabilityGuard guard = target.getAdjacentCapability()) {
                    //输出端连接、且能输入元素的元素容器
                    IElementStorage output = guard.get();
                    if(output.insertElement(1,type,true)==0) list.add(output);
                } catch (Exception ignored) {
                    //不可用的输出端忽略掉
                }
            }
            if(list.isEmpty())return count;

            //计算总共成功输出的量
            int successCount = 0;
            //计算平均每个输出端口分到的元素量
            int countPerOutput = (count-successCount)/(list.size());
            //无法完全平均的剩余
            int overflow = (count-successCount)%(list.size());

            for(IElementStorage outputStorage : list){
                //准备插入的量
                int toInsert = countPerOutput + overflow;
                //无法插入的剩余量
                int noInserted = outputStorage.insertElement(toInsert, type, simulate);
                //没有成功插入的留到下一个输出端口再次尝试
                overflow = noInserted;
                //统计成功操作的总量
                successCount += toInsert - noInserted;
            }

            if (!simulate) {
                // 收取转移元素的过路费
                deductEnergyCost((double) successCount / ElementKeyType.INSTANCE.getAmountPerOperation(),PowerUnits.FE);
            }

            return count-successCount;
        }

        @Override
        public int extractElement(int count, ElementType type, boolean simulate) {
            return count;
        }
    }

    public class outputElementHandler implements IElementStorage {

        @Override
        public int getElementAmount(ElementType type) {
            try (CapabilityGuard guard = getInputCapability()) {
                return guard.get().getElementAmount(type);
            }
        }

        @Override
        public int getElementCapacity(ElementType type) {
            try (CapabilityGuard guard = getInputCapability()) {
                return guard.get().getElementCapacity(type);
            }
        }

        @Override
        public int insertElement(int count, ElementType type, boolean simulate) {
            return count;
        }

        @Override
        public int extractElement(int count, ElementType type, boolean simulate) {
            try (CapabilityGuard guard = getInputCapability()) {
                int canOutputCount =  inputHandler.extractElement(count,type,simulate);
                int result = guard.get().extractElement(Math.min(count,canOutputCount), type, simulate);
                if (!simulate) {
                    deductEnergyCost((double) (count - result) / ElementKeyType.INSTANCE.getAmountPerOperation(),PowerUnits.FE);
                }
                // 返回失败的量
                return result;
            }
        }
    }
}
