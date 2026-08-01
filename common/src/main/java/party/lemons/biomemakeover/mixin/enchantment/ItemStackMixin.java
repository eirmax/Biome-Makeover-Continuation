package party.lemons.biomemakeover.mixin.enchantment;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import party.lemons.biomemakeover.init.BMEnchantments;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
    @ModifyVariable(at = @At("HEAD"), method = "hurtAndBreak*", argsOnly = true)
    private int modifyAmount(int amount)
    {
        return amount + BMEnchantments.getItemEnchantmentLevel(BMEnchantments.DECAY_CURSE, ((ItemStack) (Object)this));
    }
}
