package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import party.lemons.taniwha.block.types.TSaplingBlock;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class WaterSaplingBlock extends TSaplingBlock implements SimpleWaterloggedBlock {
    private final int maxDepth;

    public WaterSaplingBlock(TreeGrower abstractTreeGrower, int maxDepth, Properties settings)
    {
        super(abstractTreeGrower, settings);
        this.maxDepth = maxDepth;
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    public void advanceTree(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, RandomSource random) {
        if(!canGrowAtDepth(serverLevel, blockPos, blockState)) return;

        super.advanceTree(serverLevel, blockPos, blockState, random);
    }

    protected boolean canGrowAtDepth(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
        return !blockState.getValue(WATERLOGGED) || serverLevel.getFluidState(blockPos.above(maxDepth)).getType() != Fluids.WATER;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        BlockPos pos = ctx.getClickedPos();
        FluidState fluidState = ctx.getLevel().getFluidState(pos);
        BlockState placementState = super.getStateForPlacement(ctx);

        return placementState == null ? null : placementState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState newState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos posFrom) {
        if(blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return super.updateShape(blockState, direction, newState, levelAccessor, blockPos, posFrom);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }
}
