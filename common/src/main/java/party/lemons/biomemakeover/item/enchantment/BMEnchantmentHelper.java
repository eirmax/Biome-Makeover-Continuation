package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import party.lemons.biomemakeover.BMConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BMEnchantmentHelper {
    public static Enchantment createEnchantment(Supplier<BMConfig.EnchantConfig> config) {
        return createEnchantment(config, false, Rarity.UNCOMMON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public static Enchantment createEnchantment(Supplier<BMConfig.EnchantConfig> config, boolean isCurse, Rarity rarity, EquipmentSlot[] slots) {
        Enchantment.EnchantmentDefinition definition = new Enchantment.EnchantmentDefinition(
                HolderSet.direct(),
                Optional.empty(),
                config.get().maxLevel,
                1,
                new Enchantment.Cost(config.get().minCost, config.get().maxCost),
                new Enchantment.Cost(config.get().minCost, config.get().maxCost),
                isCurse ? 1 : 0,
                convertToSlotGroups(slots)
        );

        return new Enchantment(
                Component.empty(),   // Description
                definition,
                HolderSet.direct(),
                DataComponentMap.builder().build()
        );
    }

    private static List<EquipmentSlotGroup> convertToSlotGroups(EquipmentSlot[] slots) {
        return Arrays.stream(slots)
                .map(BMEnchantmentHelper::mapSlotToGroup)
                .collect(Collectors.toList());
    }

    private static EquipmentSlotGroup mapSlotToGroup(EquipmentSlot slot) {
        return switch(slot) {
            case HEAD -> EquipmentSlotGroup.HEAD;
            case CHEST -> EquipmentSlotGroup.CHEST;
            case LEGS -> EquipmentSlotGroup.LEGS;
            case FEET -> EquipmentSlotGroup.FEET;
            case MAINHAND -> EquipmentSlotGroup.MAINHAND;
            case OFFHAND -> EquipmentSlotGroup.OFFHAND;
            default -> throw new IllegalArgumentException("Unexpected equipment slot: " + slot);
        };
    }
}