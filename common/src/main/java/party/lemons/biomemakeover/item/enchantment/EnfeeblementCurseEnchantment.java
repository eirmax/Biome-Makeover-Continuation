package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;
import party.lemons.biomemakeover.BMConfig;

import java.util.UUID;
import java.util.function.Supplier;

public class EnfeeblementCurseEnchantment extends TickableAttributeEnchantment
{
    public EnfeeblementCurseEnchantment(Supplier<BMConfig.EnchantConfig> config)
    {
        super(config, true, Rarity.UNCOMMON, EquipmentSlot.values());
    }

    @Override
    public void initAttributes()
    {
        addAttributeModifier((Attribute) Attributes.MAX_HEALTH, ResourceLocation.parse(UUID.randomUUID().toString()), -2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}