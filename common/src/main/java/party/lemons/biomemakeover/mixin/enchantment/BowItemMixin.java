package party.lemons.biomemakeover.mixin.enchantment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.util.RandomUtil;

@Mixin(BowItem.class)
public class BowItemMixin
{
    @Inject(method = "shootProjectile", at = @At(value = "TAIL", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onStoppedUsing(LivingEntity livingEntity, Projectile projectile, int i, float f, float g, float h, LivingEntity livingEntity2, CallbackInfo ci)
    {
        ItemStack stack = livingEntity.getUseItem();
        int inaccuracy = EnchantmentHelper.getItemEnchantmentLevel(BMEnchantments.INACCURACY_CURSE, stack);
        if(inaccuracy >= 1)
        {
            float pitch = livingEntity.getXRot() + RandomUtil.randomDirection(livingEntity2.level().random.nextFloat() * (inaccuracy * 1.3F));
            float yaw = livingEntity.getYRot() + RandomUtil.randomDirection(livingEntity.level().random.nextFloat() * (inaccuracy * 1.3F));
            projectile.shootFromRotation(livingEntity, pitch, yaw, 0.0F, f * 3.0F, 1.0F);
        }
    }
}
