package com.waterfoxr.elementalenergistics.core;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.items.AEBaseItem;
import appeng.items.materials.MaterialItem;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import appeng.items.storage.CreativeCellItem;
import appeng.items.storage.StorageTier;
import appeng.items.tools.powered.PortableCellItem;
import appeng.menu.me.common.MEStorageMenu;
import com.google.common.base.Supplier;
import com.waterfoxr.elementalenergistics.ElementalEnergistics;
import com.waterfoxr.elementalenergistics.data.ModLanguageProvider;
import com.waterfoxr.elementalenergistics.item.ElementPortableCellItem;
import com.waterfoxr.elementalenergistics.item.ElementStorageCell;
import com.waterfoxr.elementalenergistics.me.parts.p2p.ElementP2PTunnelPart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EEItems {
    public static final List<ItemDefinition<? extends Item>> ae2P2P = new ArrayList<>();
    public static final List<ItemDefinition<? extends Item>> ae2Cells = new ArrayList<>();
    public static final List<ItemDefinition<PortableCellItem>> ae2PortableCells = new ArrayList<>();
    public static final List<ItemDefinition<? extends Item>> AllAe2Item = new ArrayList<>();

    public static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS,ElementalEnergistics.MOD_ID);



    //元素存储外壳
    public static final ItemDefinition<MaterialItem> ELEMENT_CELL_HOUSING =
            registerAe2ItemDefinition("Element Cell Housing", "element_cell_housing", MaterialItem::new, AllAe2Item);

    //创造元素元件
    public static final ItemDefinition<CreativeCellItem> ELEMENT_CELL_CREATIVE =
            registerAe2ItemDefinition("Creative ME Element Storage Cell", "creative_element_cell", p -> new CreativeCellItem(p.stacksTo(1).rarity(Rarity.EPIC)), null);


    //元素存储元件
    public static final ItemDefinition<ElementStorageCell> ELEMENT_CELL_1K =
            registerAe2ItemDefinition("1k ME Element Storage Cell", "element_storage_cell_1k", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_1K, AEItems.CELL_COMPONENT_1K,ELEMENT_CELL_HOUSING), ae2Cells);
    public static final ItemDefinition<ElementStorageCell> ELEMENT_CELL_4K =
            registerAe2ItemDefinition("4k ME Element Storage Cell", "element_storage_cell_4k", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_4K, AEItems.CELL_COMPONENT_1K,ELEMENT_CELL_HOUSING), ae2Cells);
    public static final ItemDefinition<ElementStorageCell> ELEMENT_CELL_16K =
            registerAe2ItemDefinition("16k ME Element Storage Cell", "element_storage_cell_16k", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_16K, AEItems.CELL_COMPONENT_1K,ELEMENT_CELL_HOUSING), ae2Cells);
    public static final ItemDefinition<ElementStorageCell> ELEMENT_CELL_64K =
            registerAe2ItemDefinition("64k ME Element Storage Cell", "element_storage_cell_64k", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_64K, AEItems.CELL_COMPONENT_1K,ELEMENT_CELL_HOUSING), ae2Cells);
    public static final ItemDefinition<ElementStorageCell> ELEMENT_CELL_256K =
            registerAe2ItemDefinition("256k ME Element Storage Cell", "element_storage_cell_256k", p -> new ElementStorageCell(p.stacksTo(1), StorageTier.SIZE_256K, AEItems.CELL_COMPONENT_1K,ELEMENT_CELL_HOUSING), ae2Cells);

    //便携元素存储元件
    public static final ItemDefinition<PortableCellItem> PORTABLE_ELEMENT_CELL_1K =
            registerAe2PortableCellItem("1k Portable Element Cell", "portable_element_cell_1k", p -> new ElementPortableCellItem( 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_1K, p.stacksTo(1), 0xebeeff));
    public static final ItemDefinition<PortableCellItem> PORTABLE_ELEMENT_CELL_4K =
            registerAe2PortableCellItem("4k Portable Element Cell", "portable_element_cell_4k", p -> new ElementPortableCellItem( 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_4K, p.stacksTo(1), 0xebeeff));
    public static final ItemDefinition<PortableCellItem> PORTABLE_ELEMENT_CELL_16K =
            registerAe2PortableCellItem("16k Portable Element Cell", "portable_element_cell_16k", p -> new ElementPortableCellItem( 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_16K, p.stacksTo(1), 0xebeeff));
    public static final ItemDefinition<PortableCellItem> PORTABLE_ELEMENT_CELL_64K =
            registerAe2PortableCellItem("64k Portable Element Cell", "portable_element_cell_64k", p -> new ElementPortableCellItem( 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_64K, p.stacksTo(1), 0xebeeff));
    public static final ItemDefinition<PortableCellItem> PORTABLE_ELEMENT_CELL_256K =
            registerAe2PortableCellItem("256k Portable Element Cell", "portable_element_cell_256k", p -> new ElementPortableCellItem( 4, MEStorageMenu.PORTABLE_FLUID_CELL_TYPE, StorageTier.SIZE_256K, p.stacksTo(1), 0xebeeff));

    public static ItemDefinition<? extends Item> getCell(Ae2CellTier tier) {
        return switch (tier) {
            case _1K -> ELEMENT_CELL_1K;
            case _4K -> ELEMENT_CELL_4K;
            case _16K -> ELEMENT_CELL_16K;
            case _64K -> ELEMENT_CELL_64K;
            case _256K -> ELEMENT_CELL_256K;
        };
    }

    public static ItemDefinition<? extends Item> getPortableCell(Ae2CellTier tier) {
        return switch (tier) {
            case _1K -> PORTABLE_ELEMENT_CELL_1K;
            case _4K -> PORTABLE_ELEMENT_CELL_4K;
            case _16K -> PORTABLE_ELEMENT_CELL_16K;
            case _64K -> PORTABLE_ELEMENT_CELL_64K;
            case _256K -> PORTABLE_ELEMENT_CELL_256K;
        };
    }

    public enum Ae2CellTier {
        _1K,
        _4K,
        _16K,
        _64K,
        _256K
    }

    //P2P通道
    public static final ItemDefinition<PartItem<ElementP2PTunnelPart>> ELEMENT_P2P_TUNNEL =
            registerAe2PartItem("Element P2P Tunnel", "element_p2p_tunnel", ElementP2PTunnelPart.class, ElementP2PTunnelPart::new, ae2P2P);


    private static <T extends Item> RegistryObject<T> registerItem(String englishName , String id , Supplier<T> supplier){
        RegistryObject<T> newItem = ITEM_DEFERRED_REGISTER.register(id,supplier);
        ModLanguageProvider.LangEntry.of("item",id,englishName);
        return newItem;
    }

    /**
     * @param englishName 英文翻译 例:"1k ME Element Storage Cell"
     * @param id          注册的id 例:"element_storage_cell_64k"
     * @param factory     物品的属性方法
     * @param list        归纳的列表 用于注册模型(不包含List:AllAe2Item)
     * @param <T>         继承的物品类
     * @return 返回注册的ae2物品
     */
    private static <T extends AEBaseItem> ItemDefinition<T> registerAe2ItemDefinition(String englishName, String id, Function<Item.Properties, T> factory, @Nullable List<ItemDefinition<? extends Item>> list) {
        ItemDefinition<T> definition = new ItemDefinition<>(englishName, ElementalEnergistics.id(id), factory.apply(new Item.Properties()));
        if (list != null) {
            list.add(definition);
        }
        AllAe2Item.add(definition);
        return definition;
    }

    private static ItemDefinition<PortableCellItem> registerAe2PortableCellItem(String englishName, String id, Function<Item.Properties, PortableCellItem> factory) {
        ItemDefinition<PortableCellItem> definition = new ItemDefinition<>(englishName, ElementalEnergistics.id(id), factory.apply(new Item.Properties()));
        ae2PortableCells.add(definition);
        AllAe2Item.add(definition);
        return definition;
    }

    private static <T extends IPart> ItemDefinition<PartItem<T>> registerAe2PartItem(String englishName, String id, Class<T> partClass, Function<IPartItem<T>, T> factory, @Nullable List<ItemDefinition<?>> list) {
        PartModels.registerModels(PartModelsHelper.createModels(partClass));
        return registerAe2ItemDefinition(englishName, id, p -> new PartItem<>(p, partClass, factory), list);
    }

    public static void initAe2Item(IForgeRegistry<Item> registry) {
        AllAe2Item.forEach(ae2item -> registry.register(ae2item.id(), ae2item.asItem()));
    }
}

