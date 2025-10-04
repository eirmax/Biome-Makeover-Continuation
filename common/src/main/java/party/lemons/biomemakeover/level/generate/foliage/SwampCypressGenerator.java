package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import party.lemons.biomemakeover.BiomeMakeover;

import java.util.Optional;

public class SwampCypressGenerator {

    public static final ResourceKey<ConfiguredFeature<?,?>> SMALL = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            BiomeMakeover.ID("swamp/swamp_cypress")
    );

    public static final TreeGrower SWAMP_CYPRESS = new TreeGrower(
            "swamp_cypress",
            Optional.empty(),
            Optional.of(SMALL),
            Optional.empty()
    );
}