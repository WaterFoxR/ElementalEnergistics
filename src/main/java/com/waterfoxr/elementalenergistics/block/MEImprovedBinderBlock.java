package com.waterfoxr.elementalenergistics.block;

import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import com.waterfoxr.elementalenergistics.block.entity.MEImprovedBinderBlockEntity;
import com.waterfoxr.elementalenergistics.core.EEEntityType;
import com.waterfoxr.elementalenergistics.me.menu.MEImprovedBinderMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MEImprovedBinderBlock extends ImprovedBinderBlock{

    private static final VoxelShape SHAPE = Shapes.or(
            // 底部平台 (0,0,0) 到 (16,2,16)
            box(0, 0, 0, 16, 2, 16),
            // 中间平台 (2,2,2) 到 (14,5,14)
            box(2, 2, 2, 14, 5, 14),
            // 四个立柱
            // 左下立柱 (1,2,1) 到 (3,16,3)
            box(1, 2, 1, 3, 16, 3),
            // 右下立柱 (13,2,1) 到 (15,16,3)
            box(13, 2, 1, 15, 16, 3),
            // 右上立柱 (13,2,13) 到 (15,16,15)
            box(13, 2, 13, 15, 16, 15),
            // 左上立柱 (1,2,13) 到 (3,16,15)
            box(1, 2, 13, 3, 16, 15),
            // 顶部玻璃框架 (3,13,2.5) 到 (13,13.5,13.5)
            box(3, 13, 2.5, 13, 13.5, 3),
            box(3, 13, 13, 13, 13.5, 13.5),
            box(2.5, 13, 3, 3, 13.5, 13),
            box(13, 13, 3, 13.5, 13.5, 13)
    );


    public MEImprovedBinderBlock() {
        super();
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader world, BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if(level.isClientSide || be == null)return super.use(state, level, pos, player, hand, hit);
        if(player.getMainHandItem().isEmpty() && !player.isCrouching()){
           boolean b =  MenuOpener.open(MEImprovedBinderMenu.TYPE,player, MenuLocators.forBlockEntity(be));
           return b?InteractionResult.SUCCESS:InteractionResult.FAIL;
        }


        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity be =  level.getBlockEntity(pos);
            if(be instanceof MEImprovedBinderBlockEntity){
                ((MEImprovedBinderBlockEntity) be).getUpgrades().forEach(
                        itemStack ->Containers.dropItemStack(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack)
                        );
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MEImprovedBinderBlockEntity(pos,state);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, BlockGetter level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_improved_binder.0").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_improved_binder.1").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_improved_binder.2").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_improved_binder.3").withStyle(ChatFormatting.GOLD));
    }


    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        return this.createInstrumentTicker(level, type, EEEntityType.ME_IMPROVED_BINDER);
    }


}
