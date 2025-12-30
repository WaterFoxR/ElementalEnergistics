package com.waterfoxr.elementalenergistics.data;

import appeng.core.AppEng;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.block.ECBlocks;

import javax.annotation.Nullable;

public class ModModels extends BlockStateProvider {



    ModModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ElementalEnergistics.MOD_ID, existingFileHelper);

    }

    @Override
    protected void registerStatesAndModels() {
        // blocks
        simpleBlockWithExistingModel(EEBlocks.ME_ELEMENT_CONTAINER_BLOCK.get(),null);
        simpleBlockWithExistingModel(EEBlocks.ME_ADVANCE_ELEMENT_CONTAINER_BLOCK.get(),null);
        simpleBlockWithExistingModel(EEBlocks.ME_IMPROVED_BINDER_BLOCK.get(),null);



        // 物品-元素代理导出器
        useRetrieverModelForCapabilityProxy();

        // ME元素容器
        itemModels().withExistingParent("me_element_container", ElementalEnergistics.id("block/me_element_container"));

        // ME进阶元素容器
        itemModels().withExistingParent("me_advance_element_container", ElementalEnergistics.id("block/me_advance_element_container"));


        // 存储元件在ME驱动器的模型
        models().singleTexture("element_cell", AppEng.makeId("block/drive/drive_cell"), "cell", ElementalEnergistics.id("block/element_cell"));

        // parts
        p2p();
    }

    private void simpleBlockWithExistingModel(Block block, @Nullable Block modelBlock) {
        Block model = modelBlock==null?block:modelBlock;
        ModelFile modelFile = models().getExistingFile(blockTexture(model));
        simpleBlockItem(block,modelFile);
    }

    private void useRetrieverModelForCapabilityProxy() {
        // 注册方块状态，直接引用 RetrieverBlock 的模型
        getVariantBuilder(EEBlocks.PROXY_OUTPUT_RETRIEVER_BLOCK.get())
                .forAllStates(state ->
                        ConfiguredModel.builder()
                                .build());

        // 注册物品模型，同样使用 RetrieverBlock 的模型
        itemModels().withExistingParent("proxy_output_retriever", ElementalEnergistics.id("block/proxy_output_retriever"));
    }


    private void p2p() {
        ResourceLocation texture = blockTexture(ECBlocks.PURE_ROCK.get());
        itemModels().withExistingParent("item/element_p2p_tunnel", AppEng.makeId("item/p2p_tunnel_base")).texture("type", texture);
        itemModels().withExistingParent("part/element_p2p_tunnel", AppEng.makeId("part/p2p/p2p_tunnel_base")).texture("type", texture);
    }
}