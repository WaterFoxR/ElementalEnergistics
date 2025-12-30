package com.waterfoxr.elementalenergistics.data;

import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEBlocks;
import com.waterfoxr.elementalenergistics.core.EEItems;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.data.LanguageProvider;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public class ModLanguageProvider extends LanguageProvider {

    public static final LangEntry TAB_NAME = LangEntry.of("tab", "main", ElementalEnergistics.MOD_NAME);
    public static final LangEntry ELEMENTAL_DESCRIPTION = LangEntry.description("elements", "Elements");
    public static final LangEntry P2P_CAP_DESCRIPTION = LangEntry.description("p2p_cap", "Element Holder");
    public static final Map<String,LangEntry> ELEMENT_NAMES = new HashMap<>();

    static {
        for (ElementType elementType : ElementType.values()) {
            if (elementType == ElementType.NONE) continue;
            String serializedName = elementType.getSerializedName();
            String uppercaseName = serializedName.substring(0, 1).toUpperCase() + serializedName.substring(1);
            ELEMENT_NAMES.put(
                    elementType.getSerializedName(),
                    LangEntry.of("element", serializedName, uppercaseName + " Element")
            );
        }
    }

    ModLanguageProvider(PackOutput output){
        super(output,ElementalEnergistics.MOD_ID,"zh_cn");
    }

    @Override
    protected void addTranslations() {
        this.add(EEItems.ELEMENT_P2P_TUNNEL.asItem(),"元素P2P通道");
        this.add(EEItems.ELEMENT_CELL_HOUSING.asItem(),"ME元素元件外壳");
        this.add(EEItems.ELEMENT_CELL_CREATIVE.asItem(),"创造ME元素元件");
        this.add(EEItems.ELEMENT_CELL_1K.asItem(),"1k ME元素存储元件");
        this.add(EEItems.ELEMENT_CELL_4K.asItem(),"4k ME元素存储元件");
        this.add(EEItems.ELEMENT_CELL_16K.asItem(),"16k ME元素存储元件");
        this.add(EEItems.ELEMENT_CELL_64K.asItem(),"64k ME元素存储元件");
        this.add(EEItems.ELEMENT_CELL_256K.asItem(),"256k ME元素存储元件");
        this.add(EEItems.PORTABLE_ELEMENT_CELL_1K.asItem(),"1k便携元素元件");
        this.add(EEItems.PORTABLE_ELEMENT_CELL_4K.asItem(),"4k便携元素元件");
        this.add(EEItems.PORTABLE_ELEMENT_CELL_16K.asItem(),"16k便携元素元件");
        this.add(EEItems.PORTABLE_ELEMENT_CELL_64K.asItem(),"64k便携元素元件");
        this.add(EEItems.PORTABLE_ELEMENT_CELL_256K.asItem(),"256k便携元素元件");
        this.add(EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.get(),"ME元素容器");
        this.add(EEBlocks.ME_ADVANCE_ELEMENT_CONTAINER_BLOCK.get(),"ME进阶元素容器");
        this.add(EEBlocks.PROXY_OUTPUT_RETRIEVER_BLOCK.get(), "物品-元素代理导出器");
        this.add(EEBlocks.ME_IMPROVED_BINDER_BLOCK.get(),"ME进阶元素聚合器");
        this.add("tooltip.elementalenergistics.me_element_container.set_type","存储元素类型已被设置为：");
        this.add("tooltip.elementalenergistics.me_element_container.0","魔法与科技的又一次胜利");
        this.add("tooltip.elementalenergistics.me_element_container.1","设定元素类型：空手右键方块以循环切换元素类型，Shift+右键重置为空");
        this.add("tooltip.elementalenergistics.me_element_container.2","每%stick会尝试从连接的ME网络中抽取所设定的元素类型尝试填满");
        this.add("tooltip.elementalenergistics.me_element_container.3","当切换元素类型或被破坏时，会尝试将现存储的元素存入至连接的ME网络中，多余无法插入的将被丢失");
        this.add("tooltip.elementalenergistics.me_element_container.4","除顶面和底面以外都能传输ME频道，其余特性与元素容器相同");
        this.add("tooltip.elementalenergistics.me_advance_element_container.0","直接与连接的ME网络的元素缓存交互，破坏时不会丢失元素");
        this.add("tooltip.elementalenergistics.me_advance_element_container.1","每次元素IO都会调用ME网络，滥用可能导致ME网络卡顿");
        this.add("tooltip.elementalenergistics.me_advance_element_container.2","在上方安装元素器械时，无视当前设置的元素，直接消耗ME网络中运行元素器械所需要的元素");
        this.add("tooltip.elementalenergistics.me_improved_binder.0","为契合ME网络改造的元素聚合器，支持使用加速卡提升元素聚合速度，合成后产物自动返回ME网络");
        this.add("tooltip.elementalenergistics.me_improved_binder.1","空手右键时打开界面，显示正在合成的目标产物并可插拔加速卡升级");
        this.add("tooltip.elementalenergistics.me_improved_binder.2","空手Shift+右键时返还所有位于物品");
        this.add("tooltip.elementalenergistics.me_improved_binder.3","自带元素存储空间，下方无需额外安装其他元素容器，支持元素导管IO");
        this.add("tooltip.elementalenergistics.proxy_output_retriever.0","为自动化而设计的物品导出器升级版，基于原有物品导出器的功能上，增加了物品与元素的代理转发能力");
        this.add("tooltip.elementalenergistics.proxy_output_retriever.1","接受物品或元素时：");
        this.add("tooltip.elementalenergistics.proxy_output_retriever.2","物品将被尝试输入至提取口（大头）连接的元素器械");
        this.add("tooltip.elementalenergistics.proxy_output_retriever.3","元素将被尝试输入至元素器械下方连接的元素容器中");
        this.add("element.elementalcraft.none","无元素");
        this.add("description.elementalenergistics.elements","元素");

        for(LangEntry entry : LangEntry.ENTRIES){
            add(entry.key,entry.value);
        }
    }

        /*
         * Author: Almost Reliable, rlnt
         * Source: Applied Elemental (https://github.com/AlmostReliable/appliedelemental)
         * License: ARR - Used with permission. All rights reserved by the original author.
         */
        public record LangEntry(String key, String value) implements Supplier<MutableComponent> {

        private static final Set<LangEntry> ENTRIES = new HashSet<>();

        public static LangEntry of(String prefix, String id, String value) {
            LangEntry entry = new LangEntry(String.format("%s.%s.%s", prefix, ElementalEnergistics.MOD_ID, id), value);
            ENTRIES.add(entry);
            return entry;
        }

        private static LangEntry description(String id, String value) {
            return of("description", id, value);
        }

        @Override
        public MutableComponent get() {
            return Component.translatable(key, value);
        }
    }
}
