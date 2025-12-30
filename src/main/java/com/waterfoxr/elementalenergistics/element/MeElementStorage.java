package com.waterfoxr.elementalenergistics.element;

import appeng.api.config.Actionable;
import com.waterfoxr.elementalenergistics.block.entity.MeAdvanceElementContainerBlockEntity;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;

public class MeElementStorage extends SingleElementStorage {

    private final MeAdvanceElementContainerBlockEntity container;
    private final int ArithmeticException = 999999999;
    private final Runnable syncCallback;

    public MeElementStorage(MeAdvanceElementContainerBlockEntity container, int elementCapacity, Runnable syncCallback) {
        super(elementCapacity, syncCallback);
        this.container = container;
        this.syncCallback = syncCallback;
    }

    @Override
    public int getElementAmount() {
        if (container == null ||
                container.getMainNode().getGrid() == null ||
                container.getSetType() == ElementType.NONE
        ) return 0;
        try {
            return Math.min(Math.toIntExact(container.getMEElementAmount(container.getSetType())), this.getElementCapacity());
        } catch (Exception e) {
            return ArithmeticException;
        }
    }


    @Override
    public ElementType getElementType() {
        return container.getSetType();
    }

    @Override
    public int insertElement(int count, boolean simulate) {
        if (container == null ||
                container.getMainNode().getGrid() == null ||
                container.getSetType() == ElementType.NONE
        ) return count;

        int noInserted = Math.toIntExact(count - container.getMainNode().getGrid().getStorageService().getInventory()
                .insert(ElementKey.of(container.getSetType()), count, simulate ? Actionable.SIMULATE : Actionable.MODULATE, container.actionSource));

        return count - noInserted;
    }


    @Override
    public int insertElement(int count, ElementType type, boolean simulate) {
        if (container == null || container.getMainNode().getGrid() == null) return count;

        try {
            return Math.toIntExact(container.getMainNode().getGrid().getStorageService().getInventory()
                    .insert(ElementKey.of(type), count, simulate ? Actionable.SIMULATE : Actionable.MODULATE, container.actionSource));

        } catch (Exception e) {
            return ArithmeticException;
        }

    }

    @Override
    public int extractElement(int count, boolean simulate) {
        if (container == null ||
                container.getMainNode().getGrid() == null ||
                container.getSetType() == ElementType.NONE
        ) return 0;

        int noExtracted = Math.toIntExact(count - container.getMainNode().getGrid().getStorageService().getInventory()
                .extract(ElementKey.of(container.getSetType()), count, simulate ? Actionable.SIMULATE : Actionable.MODULATE, container.actionSource));

        return count - noExtracted;
    }

    @Override
    public int extractElement(int count, ElementType type, boolean simulate) {
        if (container == null || container.getMainNode().getGrid() == null && container.getSetType()!=type) return 0;

        try {
            return Math.toIntExact(container.getMainNode().getGrid().getStorageService().getInventory()
                    .extract(ElementKey.of(type), count, simulate ? Actionable.SIMULATE : Actionable.MODULATE, container.actionSource));
        } catch (Exception e) {
            return ArithmeticException;
        }

    }


    @Override
    public int getElementAmount(ElementType type) {
        if (container == null || container.getMainNode().getGrid() == null) return 0;
        try {
            return Math.min(Math.toIntExact(container.getMainNode().getGrid().getStorageService().getInventory()
                    .getAvailableStacks().get(ElementKey.of(type))), this.getElementCapacity(type));
        } catch (Exception e) {
            return ArithmeticException;
        }

    }

    @Override
    public int transferTo(IElementStorage other, int count) {
//        EEUtils.ClientPlayerTell(2);
        if (container == null || container.getMainNode().getGrid() == null || container.getSetType() == ElementType.NONE)
            return 0;
        var MEInv = container.getMainNode().getGrid().getStorageService().getInventory();
        int toInsert = count - Math.toIntExact(MEInv.extract(ElementKey.of(container.getSetType()), count, Actionable.SIMULATE, container.actionSource));
        int noInserted = other.insertElement(toInsert, container.getSetType(), false);
        MEInv.extract(ElementKey.of(container.getSetType()), count - noInserted, Actionable.MODULATE, container.actionSource);
        return noInserted;
    }

    @Override
    public int transferTo(IElementStorage other, ElementType type, int count) {

        return this.transferTo(other,type,count,1);
    }

    @Override
    public int transferTo(IElementStorage other, ElementType type, float count, float multiplier) {
        if (count <= 0 ||
                        container == null ||
                        container.getMainNode().getGrid() == null ||
                        container.getSetType() != type
        ) return 0;

        var MEInv = container.getMainNode().getGrid().getStorageService().getInventory();

        int amount = Math.round(MEInv.extract(ElementKey.of(type),Math.max(1, Math.round(count / multiplier)), Actionable.SIMULATE, container.actionSource) * multiplier);

        amount = amount - other.insertElement(amount, type, true);
        MEInv.extract(ElementKey.of(type),Math.round(amount / multiplier), Actionable.MODULATE, container.actionSource);
        other.insertElement(amount, type, false);
        return amount;
    }

    public Runnable getSyncCallback() {
        return syncCallback;
    }
}
