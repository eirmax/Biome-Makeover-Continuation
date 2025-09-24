package party.lemons.biomemakeover.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.util.EntityUtil;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin
{
    /*
        Apply Projectile Resisitence attritube
     */

    @Inject(at = @At(value = "TAIL", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;runIterationOnInventory(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Ljava/lang/Iterable;)V"), method = "getDamageProtection", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void applyProjectileResistance(ServerLevel serverLevel, LivingEntity livingEntity, DamageSource damageSource, CallbackInfoReturnable<Float> cir, MutableFloat mutableFloat)
    {
        if(damageSource.is(DamageTypeTags.IS_PROJECTILE))
        {
            EntityUtil.applyProjectileResistance(livingEntity.getArmorAndBodyArmorSlots(), mutableFloat);
        }
    }
}
