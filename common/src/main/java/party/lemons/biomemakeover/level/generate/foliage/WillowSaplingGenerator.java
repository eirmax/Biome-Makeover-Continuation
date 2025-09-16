package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;

import java.util.Optional;

public class WillowSaplingGenerator {
    public static final ResourceKey<ConfiguredFeature<?,?>> SMALL = ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("swamp/willow"));

    public static final TreeGrower WILLOW = new TreeGrower(
            "willow",
            0.1F,
            Optional.empty(),
            Optional.empty(),
            Optional.of(SMALL),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}