package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BMConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BMEnchantment{
    private final Supplier<BMConfig.EnchantConfig> config;
    private final boolean isCurse;
    private final Rarity rarity;
    private final EquipmentSlot[] slots;

    public BMEnchantment(Supplier<BMConfig.EnchantConfig> config, boolean isCurse, Rarity rarity, EquipmentSlot[] slots) {
        this.config = config;
        this.isCurse = isCurse;
        this.rarity = rarity;
        this.slots = slots;
    }

    public Enchantment createEnchantment() {
        List<EquipmentSlotGroup> slotGroups = Arrays.stream(slots)
                .map(this::convertToSlotGroup)
                .collect(Collectors.toList());

        Enchantment.Cost minCost = new Enchantment.Cost(config.get().minCost, 0);
        Enchantment.Cost maxCost = new Enchantment.Cost(config.get().maxCost, 0);

        Enchantment.EnchantmentDefinition definition = new Enchantment.EnchantmentDefinition(
                HolderSet.direct(),
                Optional.empty(),
                config.get().maxLevel,
                isCurse ? 1 : 0,
                minCost,
                maxCost,
                config.get().isTreasureOnly ? 1 : 0,
                slotGroups
        );

        return new Enchantment(
                Component.empty(),
                definition,
                HolderSet.direct(),
                new DataComponentMap() {
                    @Override
                    public @Nullable <T> T get(DataComponentType<? extends T> dataComponentType) {
                        return null;
                    }

                    @Override
                    public Set<DataComponentType<?>> keySet() {
                        return Set.of();
                    }
                }
        );
    }

    private EquipmentSlotGroup convertToSlotGroup(EquipmentSlot slot) {
        switch (slot) {
            case HEAD:
                return EquipmentSlotGroup.HEAD;
            case CHEST:
                return EquipmentSlotGroup.CHEST;
            case LEGS:
                return EquipmentSlotGroup.LEGS;
            case FEET:
                return EquipmentSlotGroup.FEET;
            case MAINHAND:
                return EquipmentSlotGroup.MAINHAND;
            case OFFHAND:
                return EquipmentSlotGroup.OFFHAND;
            default:
                throw new IllegalArgumentException("Unknown equipment slot: " + slot);
        }
    }
}