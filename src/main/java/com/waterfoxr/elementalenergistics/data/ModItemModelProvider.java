package com.waterfoxr.elementalenergistics.data;

import appeng.core.AppEng;
import appeng.core.definitions.ItemDefinition;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.core.EEItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    //存储原件的LED灯用于标识空、未满、满
    private static final ResourceLocation STORAGE_CELL_LED = AppEng.makeId("item/storage_cell_led");
    private static final ResourceLocation PORTABLE_CELL_LED = AppEng.makeId("item/portable_cell_led");
    private static final ResourceLocation P2P_TUNNEL_BASE_ITEM = AppEng.makeId("item/p2p_tunnel_base");
    private static final ResourceLocation P2P_TUNNEL_BASE_PART = AppEng.makeId("part/p2p/p2p_tunnel_base");

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ElementalEnergistics.MOD_ID, existingFileHelper);

        existingFileHelper.trackGenerated(P2P_TUNNEL_BASE_ITEM, MODEL);
        existingFileHelper.trackGenerated(P2P_TUNNEL_BASE_PART, MODEL);
        existingFileHelper.trackGenerated(STORAGE_CELL_LED, TEXTURE);
        existingFileHelper.trackGenerated(PORTABLE_CELL_LED, TEXTURE);
    }

    @Override
    protected void registerModels() {
        var housing = EEItems.ELEMENT_CELL_HOUSING;
        flatSingleLayer(housing, "item/" + housing.id().getPath());

        var creative = EEItems.ELEMENT_CELL_CREATIVE;
        flatSingleLayer(creative, "item/" + creative.id().getPath());

        for (var tier : EEItems.Ae2CellTier.values()) {
            var cell = EEItems.getCell(tier);
            var portableCell = EEItems.getPortableCell(tier);
            cell(cell, "item/" + cell.id().getPath());
            portableCell(portableCell, "item/" + portableCell.id().getPath());
        }

    }

    private void cell(ItemDefinition<? extends Item> cell, String background) {
        singleTexture(cell.id().getPath(), mcLoc("item/generated"),
                "layer0", ElementalEnergistics.id(background))
                .texture("layer1", STORAGE_CELL_LED);
    }

    private void portableCell(ItemDefinition<? extends Item> portable, String background) {
        singleTexture(portable.id().getPath(), mcLoc("item/generated"),
                "layer0", ElementalEnergistics.id(background))
                .texture("layer1", PORTABLE_CELL_LED);
    }

    private void flatSingleLayer(ItemDefinition<? extends Item> item, String texture) {
        singleTexture(item.id().getPath(), mcLoc("item/generated"),
                "layer0", ElementalEnergistics.id(texture));
    }
}
