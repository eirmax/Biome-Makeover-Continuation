package party.lemons.biomemakeover.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Collection;

public final class EntityUtil
{

    public static void applyProjectileResistance(Iterable<ItemStack> equipment, MutableFloat resistance)
    {
        MutableInt slotIndex = new MutableInt(0);
        equipment.forEach(e->{
            if(!e.isEmpty())
            {
                EquipmentSlot slot = EquipmentSlot.values()[2 + slotIndex.getValue()];


                e.forEachModifier(slot, (attributeHolder, modifier) -> {
                    if(attributeHolder.equals(BMEntities.ATT_PROJECTILE_RESISTANCE.get()))
                    {
                        resistance.add((float) modifier.amount());
                    }
                });
            }
            slotIndex.add(1);
        });
    }

    public static double getProjectileResistance(LivingEntity e)
    {
        final double[] res = {0.0};

        for(EquipmentSlot slot : EquipmentSlot.values())
        {
            ItemStack st = e.getItemBySlot(slot);
            if(!st.isEmpty())
            {
                st.forEachModifier(slot, (attributeHolder, modifier) -> {
                    if(attributeHolder.equals(BMEntities.ATT_PROJECTILE_RESISTANCE.get()))
                    {
                        res[0] += modifier.amount();
                    }
                });
            }
        }
        return res[0];
    }
    private EntityUtil()
    {
    }

    public static boolean attemptProjectileResistanceBlock(LivingEntity entity, DamageSource source)
    {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            double protection = EntityUtil.getProjectileResistance(entity);
            if (protection > 0D && (entity.getRandom().nextDouble() * 30D) < protection) {
                entity.playSound(SoundEvents.SHIELD_BLOCK, 1F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
                return true;
            }
        }

        return false;
    }
}
