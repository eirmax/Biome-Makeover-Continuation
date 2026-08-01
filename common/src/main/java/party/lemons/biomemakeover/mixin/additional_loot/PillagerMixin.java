package party.lemons.biomemakeover.mixin.additional_loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.BMLootUtil;
import party.lemons.biomemakeover.util.extension.LootBlocker;

@Mixin(Pillager.class)
public abstract class PillagerMixin extends AbstractIllager
{
	private static final ResourceKey<LootTable> ADDITIONAL_LOOT_LEADER = ResourceKey.create(Registries.LOOT_TABLE, BiomeMakeover.ID("entities/pillager_leader_additional"));
	private static final ResourceKey<LootTable> ADDITIONAL_LOOT = ResourceKey.create(Registries.LOOT_TABLE, BiomeMakeover.ID("entities/pillager_additional"));

	@Override
	protected void dropFromLootTable(DamageSource damageSource, boolean causedByPlayer)
	{
		super.dropFromLootTable(damageSource, causedByPlayer);

		if(!causedByPlayer || LootBlocker.isBlocked(this))
			return;

		//If is leader and not in raid, use leader table, otherwise use regular table
		ResourceKey<LootTable> tableLocation = (isPatrolLeader() && !hasActiveRaid()) ? ADDITIONAL_LOOT_LEADER : ADDITIONAL_LOOT;
		BMLootUtil.dropEntityLoot(this, tableLocation, damageSource);
	}

	private PillagerMixin(EntityType<? extends AbstractIllager> entityType, Level level)
	{
		super(entityType, level);
	}
}
