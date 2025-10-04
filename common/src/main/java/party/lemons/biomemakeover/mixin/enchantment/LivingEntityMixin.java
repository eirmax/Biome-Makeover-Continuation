package party.lemons.biomemakeover.mixin.enchantment;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.item.enchantment.TickableAttributeEnchantment;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo cbi)
    {
        if(!level().isClientSide())
        {
            Iterator<Pair<EquipmentSlot, ItemStack>> it = attributeStacks.iterator();
            while(it.hasNext())
            {
                Pair<EquipmentSlot, ItemStack> pair = it.next();
                ItemStack st = pair.getSecond();
                if(!hasStackEquipInSlot(st, pair.getFirst()))
                {
                    // Fixed: Use ItemEnchantments instead of Map, and proper enchantment iteration
                    ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(st);
                    for(Holder<Enchantment> enchantmentHolder : enchants.keySet())
                    {
                        Enchantment enchantment = enchantmentHolder.value();
                        // Check if this enchantment is a TickableAttributeEnchantment by checking the registry
                        if(BMEnchantments.isTickableAttributeEnchantment(enchantment))
                        {
                            // Get the BMEnchantment instance and cast to TickableAttributeEnchantment
                            TickableAttributeEnchantment tickable = BMEnchantments.getTickableAttributeEnchantment(enchantment);
                            if(tickable != null)
                            {
                                tickable.removeAttributes((LivingEntity) (Object) this, pair.getFirst());
                            }
                        }
                    }
                    it.remove();
                }
            }

            for(EquipmentSlot slot : EquipmentSlot.values())
            {
                ItemStack stack = getItemBySlot(slot);
                if(!stack.isEmpty())
                {
                    // Fixed: Use ItemEnchantments and proper iteration
                    ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(stack);
                    for(Object2IntMap.Entry<Holder<Enchantment>> entry : enchants.entrySet())
                    {
                        Holder<Enchantment> enchantmentHolder = entry.getKey();
                        Enchantment enchantment = enchantmentHolder.value();
                        int lvl = entry.getIntValue();

                        // Check if this enchantment is a TickableAttributeEnchantment by checking the registry
                        if(BMEnchantments.isTickableAttributeEnchantment(enchantment))
                        {
                            // Get the BMEnchantment instance and cast to TickableAttributeEnchantment
                            TickableAttributeEnchantment tickable = BMEnchantments.getTickableAttributeEnchantment(enchantment);
                            if(tickable != null)
                            {
                                tickable.onTick((LivingEntity) (Object) this, stack, lvl);
                                if(!hasAttributeStack(stack) && tickable.addAttributes((LivingEntity) (Object) this, stack, slot, lvl))
                                {
                                    attributeStacks.add(new Pair<>(slot, stack));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @ModifyArg(method = "causeFallDamage", at = @At(value = "INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"), index = 0)
    private float changeFallDistance(float distance){
        if(distance >= 3.0)
            return distance + EnchantmentHelper.getEnchantmentLevel(BMEnchantments.BUCKLING_CURSE, (LivingEntity)(Object)this);

        return distance;
    }

    @Unique
    private boolean hasStackEquipInSlot(ItemStack stack, EquipmentSlot slot)
    {
        return getItemBySlot(slot).equals(stack);
    }

    @Unique
    private final Collection<Pair<EquipmentSlot, ItemStack>> attributeStacks = Lists.newArrayList();

    @Unique
    public boolean hasAttributeStack(ItemStack stack)
    {
        for(Pair<EquipmentSlot, ItemStack> pair : attributeStacks)
        {
            if(pair.getSecond().equals(stack)) return true;
        }
        return false;
    }

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot var1);

}
