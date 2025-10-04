package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import party.lemons.biomemakeover.BMConfig;

import java.util.function.Supplier;

public class DecayCurseEnchantment extends BMEnchantment {
    public DecayCurseEnchantment(Supplier<BMConfig.EnchantConfig> config) {
        super(config, true, Rarity.EPIC, EquipmentSlot.values());
    }

}