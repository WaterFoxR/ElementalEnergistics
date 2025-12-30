package com.waterfoxr.elementalenergistics.block;

import com.waterfoxr.elementalenergistics.EEConfig;
import com.waterfoxr.elementalenergistics.block.entity.MeElementContainerBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.container.AbstractConnectedElementContainerBlock;

import java.util.List;
import java.util.Objects;


public class MeElementContainerBlock extends AbstractConnectedElementContainerBlock {

    private static final VoxelShape BASE = box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape GLASS = box(2, 2, 2, 14, 15, 14);
    private static final VoxelShape PILLAR_1 = box(1, 2, 1, 3, 16, 3);
    private static final VoxelShape PILLAR_2 = box(13, 2, 1, 15, 16, 3);
    private static final VoxelShape PILLAR_3 = box(1, 2, 13, 3, 16, 15);
    private static final VoxelShape PILLAR_4 = box(13, 2, 13, 15, 16, 15);
    private static final VoxelShape SOCKET = box(6, 15, 6, 10, 16, 10);
    private static final VoxelShape PORT_1 = box(5, 5, 0, 11, 11, 2);
    private static final VoxelShape PORT_2 = box(5, 5, 14, 11, 11, 16);
    private static final VoxelShape PORT_3 = box(0, 5, 5, 2, 11, 11);
    private static final VoxelShape PORT_4 = box(14, 5, 5, 16, 11, 11);

    private static final VoxelShape SHAPE = Shapes.or(
            BASE, GLASS, PILLAR_1, PILLAR_2, PILLAR_3, PILLAR_4, SOCKET, PORT_1, PORT_2, PORT_3, PORT_4
    );

    public static final EnumProperty<ElementType> ELEMENT_TYPE = EnumProperty.create(ECNames.ELEMENT_TYPE, ElementType.class);
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH);
        pBuilder.add(EAST);
        pBuilder.add(SOUTH);
        pBuilder.add(WEST);
        pBuilder.add(ELEMENT_TYPE);
        super.createBlockStateDefinition(pBuilder);
    }

    public MeElementContainerBlock() {
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(ELEMENT_TYPE,ElementType.NONE));
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MeElementContainerBlockEntity(pos,state);
    }


    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(pLevel.isClientSide || pHand!=InteractionHand.MAIN_HAND || !pPlayer.getMainHandItem().isEmpty())return super.use(pState,pLevel,pPos,pPlayer,pHand,pHit);
        ElementType setType = pState.getValue(ELEMENT_TYPE);
        ElementType nextType;
        nextType= pPlayer.isCrouching()?ElementType.NONE:switch (setType){
            case NONE, AIR -> ElementType.FIRE;
            case FIRE -> ElementType.WATER;
            case WATER -> ElementType.EARTH;
            case EARTH -> ElementType.AIR;
        };
        Objects.requireNonNull(pLevel.getServer()).sendSystemMessage(nextType.getDisplayName());
        pLevel.setBlock(pPos,pState.setValue(ELEMENT_TYPE,nextType),3);
        BlockEntity be =  pLevel.getBlockEntity(pPos);
        if(be instanceof MeElementContainerBlockEntity){
            if(nextType == ElementType.NONE){
                ((MeElementContainerBlockEntity) be).unloadElementToME();
            }
            else ((MeElementContainerBlockEntity) be).loadElementFromME();
            Style color = Style.EMPTY.withColor(nextType.getColor());
            Component TypeNameWithColor = Component.empty().append(nextType.getDisplayName()).withStyle(color);
            pPlayer.displayClientMessage(this.getName().append(Component.translatable("tooltip.elementalenergistics.me_element_container.set_type")).append(TypeNameWithColor),true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if (pPlacer instanceof Player player
                && pLevel.getBlockEntity(pPos) instanceof MeElementContainerBlockEntity blockEntity) {
            blockEntity.getMainNode().setOwningPlayer(player);
        }
    }


    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, BlockGetter level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_element_container.0").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_element_container.1").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_element_container.2",EEConfig.COMMON.meContainerTickToLoad.get()));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_element_container.3"));
        tooltip.add(Component.translatable("tooltip.elementalenergistics.me_element_container.4").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public int getDefaultCapacity() {
        return EEConfig.COMMON.meContainerCapacity.get();
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return (a, b, c, be) -> {
            if (be instanceof MeElementContainerBlockEntity) {
                ((MeElementContainerBlockEntity) be).tickServer();
            }
            super.getTicker(level,state,type);
        };
    }
}
