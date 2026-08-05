package party.lemons.biomemakeover.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMFeatures;

@Mixin(TreeGrower.class)
public class DarkOakTreeGrowerMixin_SingleTree {
    @Inject(at = @At("HEAD"), method = "getConfiguredFeature", cancellable = true)
    protected void getConfiguredFeature(RandomSource randomSource, boolean bl, CallbackInfoReturnable<ResourceKey<ConfiguredFeature<?, ?>>> cbi) {
        if((Object) this == TreeGrower.DARK_OAK)
            cbi.setReturnValue(BMFeatures.SINGLE_DARK_OAK_KEY);
    }
}
