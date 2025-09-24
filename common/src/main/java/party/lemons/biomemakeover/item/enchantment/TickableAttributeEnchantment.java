package party.lemons.biomemakeover.item.enchantment;

import com.google.common.collect.Maps;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import party.lemons.biomemakeover.BMConfig;
import party.lemons.taniwha.util.MathUtils;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class TickableAttributeEnchantment extends BMEnchantment
{
    private final Map<Attribute, AttributeModifier> attributeModifiers = Maps.newHashMap();

    public TickableAttributeEnchantment(Supplier<BMConfig.EnchantConfig> config, boolean isCurse, Rarity weight, EquipmentSlot[] slotTypes)
    {
        super(config, isCurse, weight, slotTypes);

        initAttributes();
    }

    public void initAttributes()
    {

    }

    public void onTick(LivingEntity entity, ItemStack stack, int level)
    {

    }

    protected void addAttributeModifier(Attribute attribute, ResourceLocation uuid, double amount, AttributeModifier.Operation operation)
    {
        AttributeModifier entityAttributeModifier = new AttributeModifier(uuid, amount, operation);
        this.attributeModifiers.put(attribute, entityAttributeModifier);
    }

    public boolean addAttributes(LivingEntity entity, ItemStack stack, EquipmentSlot slot, int level)
    {
        if(attributeModifiers.size() <= 0 || stack.isEmpty()) return false;

        for(Map.Entry<Attribute, AttributeModifier> attributeEntry : this.attributeModifiers.entrySet())
        {
            UUID id = MathUtils.uuidFromString(slot.toString());
            AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance((Holder<Attribute>) attributeEntry.getKey());
            if(entityAttributeInstance != null)
            {
                AttributeModifier mod = attributeEntry.getValue();
                entityAttributeInstance.removeModifier(mod);
                entityAttributeInstance.addTransientModifier(new AttributeModifier(ResourceLocation.parse(String.valueOf(id)),  this.adjustModifierAmount(level, mod), mod.operation()));

            }
        }
        return true;
    }

    public double adjustModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return modifier.amount() * (double) (amplifier);
    }

    public void removeAttributes(LivingEntity entity, EquipmentSlot slot)
    {
        for(Map.Entry<Attribute, AttributeModifier> attributeEntry : this.attributeModifiers.entrySet())
        {
            UUID slotID = MathUtils.uuidFromString(slot.toString());
            AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance((Holder<Attribute>) attributeEntry.getKey());
            if(entityAttributeInstance != null)
            {
                AttributeModifier mod = entityAttributeInstance.getModifier(ResourceLocation.parse(String.valueOf(slotID)));
                if(mod != null)
                    entityAttributeInstance.removeModifier(mod);
                else
                    System.out.println("ERROR REMOVING MODIFIER: DOESNT EXIST??? : " );
            }
        }
    }
}