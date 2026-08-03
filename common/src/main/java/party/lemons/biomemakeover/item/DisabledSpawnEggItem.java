package party.lemons.biomemakeover.item;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DisabledSpawnEggItem extends ArchitecturySpawnEggItem
{
	public DisabledSpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties)
	{
		super(entityType, backgroundColor, highlightColor, properties);
	}

	public DisabledSpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties, @Nullable DispenseItemBehavior dispenseItemBehavior)
	{
		super(entityType, backgroundColor, highlightColor, properties, dispenseItemBehavior);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
		list.add(Component.translatable("item.biomemakeover.toad_spawn_egg.desc"));
	}
}
