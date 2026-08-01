package party.lemons.biomemakeover.level.generate.foliage.sapling_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.WaterSaplingBlock;
import party.lemons.biomemakeover.level.generate.foliage.SwampCypressGenerator;

public class SwampCypressSapling extends WaterSaplingBlock {

    public static final ResourceKey<ConfiguredFeature<?,?>> SMALL = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            BiomeMakeover.ID("swamp/swamp_cypress")
    );

    public SwampCypressSapling(int maxDepth, Properties properties) {
        super(SwampCypressGenerator.SWAMP_CYPRESS, maxDepth, properties);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if(!canGrowAtDepth(level, pos, state)) return;

        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            this.growCustomTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }

    private boolean growCustomTree(ServerLevel serverLevel, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState blockState, RandomSource random) {
        Holder<ConfiguredFeature<?,?>> holder = serverLevel.registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE)
                .getHolder(SMALL)
                .orElse(null);

        if(holder != null) {
            serverLevel.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 4);
            if (holder.value().place(serverLevel, chunkGenerator, random, blockPos)) {
                return true;
            }
        }
        serverLevel.setBlock(blockPos, blockState, 4);
        return false;
    }
}
