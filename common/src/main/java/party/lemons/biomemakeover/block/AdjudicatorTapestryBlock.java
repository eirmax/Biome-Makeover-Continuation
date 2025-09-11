package party.lemons.biomemakeover.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BaseEntityBlock;
import party.lemons.biomemakeover.BiomeMakeover;

public class AdjudicatorTapestryBlock extends AbstractTapestryBlock {
    public static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/tapestry/adjudicator_tapestry.png");

    public AdjudicatorTapestryBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public ResourceLocation getRenderTexture() {
        return TEXTURE;
    }
}
