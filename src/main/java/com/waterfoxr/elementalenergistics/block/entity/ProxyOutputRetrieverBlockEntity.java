package com.waterfoxr.elementalenergistics.block.entity;

import com.waterfoxr.elementalenergistics.core.EEEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;

public class ProxyOutputRetrieverBlockEntity extends BlockEntity {


    public ProxyOutputRetrieverBlockEntity(BlockPos pos, BlockState state) {
        super(EEEntityType.PROXY_OUTPUT_RETRIEVER.get(), pos, state);
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if( level == null || level.isClientSide )return LazyOptional.empty();

        Direction source =  this.getBlockState().getValue(RetrieverBlock.SOURCE);
        BlockPos sourcePos = this.worldPosition.relative(source);
        BlockEntity belowBE =  level.getBlockEntity(sourcePos);
        BlockEntity belowContainer = level.getBlockEntity(sourcePos.below());

        if(cap.equals(ForgeCapabilities.ITEM_HANDLER) && belowBE !=null){
            return belowBE.getCapability(cap);
        }
        if(cap.equals(ElementalCraftCapabilities.ELEMENT_STORAGE)&&belowContainer!=null){
            return belowContainer.getCapability(cap);
        }

        return LazyOptional.empty();
    }
}
