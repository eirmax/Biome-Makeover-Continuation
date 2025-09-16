package party.lemons.biomemakeover.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BaseEntityBlock;

public class ColorTapestryWallBlock extends AbstractTapestryWallBlock {
    private final DyeColor color;

    public ColorTapestryWallBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public ResourceLocation getRenderTexture() {
        return ColorTapestryBlock.TEXTURES.get(color);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
}