package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import party.lemons.biomemakeover.BiomeMakeover;

import java.util.Optional;

public class AncientOakSaplingGenerator {
    public static final ResourceKey<ConfiguredFeature<?,?>> MEGA = ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("dark_forest/ancient_oak"));
    public static final ResourceKey<ConfiguredFeature<?,?>> SMALL = ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("dark_forest/ancient_oak_small"));

    public static final TreeGrower ANCIENT_OAK = new TreeGrower(
            "ancient_oak",
            0.0f,
            Optional.of(MEGA),
            Optional.empty(),
            Optional.of(SMALL),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );

    public static final TreeGrower ANCIENT_OAK_WITH_VARIANTS = new TreeGrower(
            "ancient_oak_variants",
            0.1f,
            Optional.of(MEGA),
            Optional.empty(),
            Optional.of(SMALL),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
}