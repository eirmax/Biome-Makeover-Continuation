package party.lemons.biomemakeover.mixin.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WaterlilyBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterlilyBlock.class)
public abstract class LilypadBlockMixin_PlantType implements IPlantable
{
    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.WATER;
    }
}
