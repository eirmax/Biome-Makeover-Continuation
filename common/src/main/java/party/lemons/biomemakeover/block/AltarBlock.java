package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.taniwha.block.types.TBlock;

import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class AltarBlock extends TBlock implements SimpleWaterloggedBlock, EntityBlock
{
    public static final VoxelShape BOTTOM_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    public static final VoxelShape MIDDLE_SHAPE = Block.box(4.0D, 2.0D, 4D, 12.0D, 10.0D, 12.0D);
    public static final VoxelShape TOP_SHAPE = Block.box(2.0D, 10.0D, 2D, 14.0D, 12.0D, 14.0D);
    public static final VoxelShape BOOK_SHAPE = Block.box(3.0D, 12.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    public static final VoxelShape SHAPE = Shapes.or(BOTTOM_SHAPE, MIDDLE_SHAPE, TOP_SHAPE, BOOK_SHAPE);
    public static BooleanProperty ACTIVE = BooleanProperty.create("active");

    public AltarBlock(Properties properties)
    {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        openMenu(blockState, level, blockPos, player);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        openMenu(blockState, level, blockPos, player);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private void openMenu(BlockState blockState, Level level, BlockPos blockPos, Player player)
    {
        if(!level.isClientSide())
        {
            MenuProvider screenHandlerFactory = blockState.getMenuProvider(level, blockPos);
            if(screenHandlerFactory != null)
            {
                ((ServerPlayer) player).openMenu(screenHandlerFactory);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AltarBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return AltarBlockEntity.getTicker(level, blockState, blockEntityType);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());

        return defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock())
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof AltarBlockEntity)
            {
                Containers.dropContents(level, pos, (AltarBlockEntity) blockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int type, int data) {
        super.triggerEvent(state, level, pos, type, data);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(type, data);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }


    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
        builder.add(WATERLOGGED);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE)) {
            for (int i = 0; i < 5; i++) {
                double xSpeed = RandomUtil.randomRange(-1, 1) / 0.75F;
                double zSpeed = RandomUtil.randomRange(-1, 1) / 0.75F;
                double ySpeed = random.nextDouble() / 0.1F;

                level.addParticle(ParticleTypes.ENCHANT, (double) pos.getX() + 0.5F, (double) pos.getY() + 0.75F, (double) pos.getZ() + 0.5F, xSpeed, ySpeed, zSpeed);
            }
        }

        if (random.nextInt(5) == 0) {
            Direction direction = Direction.getRandom(random);
            if (!direction.getAxis().isVertical()) {
                double x = direction.getAxis() == Direction.Axis.X ? (0.5F + (0.3F * (float) RandomUtil.randomDirection(1))) : (float) RandomUtil.randomRange(2, 8) / 10F;
                double z = direction.getAxis() == Direction.Axis.Z ? (0.5F + (0.3F * (float) RandomUtil.randomDirection(1))) : (float) RandomUtil.randomRange(2, 8) / 10F;
                double y = 0.2F + (random.nextFloat() / 3F);

                level.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }
}
