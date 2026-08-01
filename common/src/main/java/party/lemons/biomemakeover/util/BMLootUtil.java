package party.lemons.biomemakeover.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class BMLootUtil {
    public static void dropEntityLoot(LivingEntity entity, ResourceKey<LootTable> tableKey) {
        dropEntityLoot(entity, tableKey, entity.getLastDamageSource(), false);
    }

    public static void dropEntityLoot(LivingEntity entity, ResourceKey<LootTable> tableKey, DamageSource damageSource) {
        dropEntityLoot(entity, tableKey, damageSource, false);
    }

    public static void dropEntityLoot(LivingEntity entity, ResourceKey<LootTable> tableKey, DamageSource damageSource, boolean extendedLifetime) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(tableKey);
        LootParams.Builder params = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position());

        LivingEntity killCredit = entity.getKillCredit();
        if (killCredit instanceof Player player) {
            params.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
        }

        if (damageSource != null) {
            params.withParameter(LootContextParams.DAMAGE_SOURCE, damageSource);

            Entity attackingEntity = damageSource.getEntity();
            if (attackingEntity != null) {
                params.withParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity);
            }

            Entity directEntity = damageSource.getDirectEntity();
            if (directEntity != null) {
                params.withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directEntity);
            }
        }

        lootTable.getRandomItems(params.create(damageSource == null ? LootContextParamSets.ALL_PARAMS : LootContextParamSets.ENTITY), stack -> spawn(entity, stack, extendedLifetime));
    }

    private static void spawn(LivingEntity entity, ItemStack stack, boolean extendedLifetime) {
        ItemEntity itemEntity = entity.spawnAtLocation(stack);
        if (extendedLifetime && itemEntity != null) {
            itemEntity.setExtendedLifetime();
        }
    }
}
