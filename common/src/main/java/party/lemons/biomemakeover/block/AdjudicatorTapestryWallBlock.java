package party.lemons.biomemakeover.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BaseEntityBlock;

import java.awt.*;

public class AdjudicatorTapestryWallBlock extends AbstractTapestryWallBlock {
    public AdjudicatorTapestryWallBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public ResourceLocation getRenderTexture() {
        return AdjudicatorTapestryBlock.TEXTURE;
    }
}