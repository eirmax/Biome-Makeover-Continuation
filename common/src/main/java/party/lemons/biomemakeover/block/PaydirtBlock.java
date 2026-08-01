package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.taniwha.block.types.TBlock;

public class PaydirtBlock extends TBlock
{
    public PaydirtBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if(random.nextInt(3) == 0 && isTouchingWater(level, pos))
        {
            dropResources(state, level, pos);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    private boolean isTouchingWater(ServerLevel level, BlockPos pos)
    {
        for(Direction direction : Direction.values())
        {
            if(level.getFluidState(pos.relative(direction)).is(FluidTags.WATER))
                return true;
        }

        return false;
    }
}
