package com.waterfoxr.elementalenergistics.block;

import com.waterfoxr.elementalenergistics.block.entity.ProxyOutputRetrieverBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;

import java.util.List;

public class ProxyOutputRetrieverBlock extends RetrieverBlock implements EntityBlock {

    public ProxyOutputRetrieverBlock(){
        super();
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(SOURCE, Direction.SOUTH)).setValue(TARGET, Direction.NORTH));
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ProxyOutputRetrieverBlockEntity(pos, state);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> tooltip, @NotNull TooltipFlag pFlag) {
        tooltip.add(Component.translatable("tooltip.elementalenergistics.proxy_output_retriever.0").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.proxy_output_retriever.1").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.proxy_output_retriever.2"));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.proxy_output_retriever.3"));
    }
}
