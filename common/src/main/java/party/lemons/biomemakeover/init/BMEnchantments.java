package party.lemons.biomemakeover.init;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import party.lemons.biomemakeover.BMConfig;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.item.enchantment.ConductivityCurseEnchantment;
import party.lemons.biomemakeover.item.enchantment.DepthsCurseEnchantment;
import party.lemons.biomemakeover.item.enchantment.EnfeeblementCurseEnchantment;
import party.lemons.biomemakeover.item.enchantment.InsomniaCurseEnchantment;
import party.lemons.biomemakeover.item.enchantment.TickableAttributeEnchantment;
import party.lemons.biomemakeover.item.enchantment.UnwieldinessCurseEnchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BMEnchantments {

    public static final TagKey<Enchantment> ALTAR_CURSE_EXCLUDED = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_curse_excluded"));
    public static final TagKey<Enchantment> ALTAR_CANT_UPGRADE = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_cant_upgrade"));

    public static final ResourceKey<Enchantment> DECAY_CURSE = key("decay_curse");
    public static final ResourceKey<Enchantment> INSOMNIA_CURSE = key("insomnia_curse");
    public static final ResourceKey<Enchantment> CONDUCTIVITY_CURSE = key("conductivity_curse");
    public static final ResourceKey<Enchantment> ENFEEBLEMENT_CURSE = key("enfeeblement_curse");
    public static final ResourceKey<Enchantment> DEPTH_CURSE = key("depth_curse");
    public static final ResourceKey<Enchantment> FLAMMABILITY_CURSE = key("flammability_curse");
    public static final ResourceKey<Enchantment> SUFFOCATION_CURSE = key("suffocation_curse");
    public static final ResourceKey<Enchantment> UNWIELDINESS_CURSE = key("unwieldiness_curse");
    public static final ResourceKey<Enchantment> INACCURACY_CURSE = key("inaccuracy_curse");
    public static final ResourceKey<Enchantment> BUCKLING_CURSE = key("buckling_curse");

    private static final Map<ResourceKey<Enchantment>, TickableAttributeEnchantment> tickableEnchantments = new HashMap<>();
    private static boolean initialized = false;

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID(name));
    }

    private static void ensureInitialized() {
        if (!initialized) {
            BMConfig.ensureLoaded();
            tickableEnchantments.put(INSOMNIA_CURSE, new InsomniaCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.INSOMNIA));
            tickableEnchantments.put(CONDUCTIVITY_CURSE, new ConductivityCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.CONDUCTIVITY));
            tickableEnchantments.put(ENFEEBLEMENT_CURSE, new EnfeeblementCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.ENFEEBLEMENT));
            tickableEnchantments.put(DEPTH_CURSE, new DepthsCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.DEPTHS));
            tickableEnchantments.put(UNWIELDINESS_CURSE, new UnwieldinessCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.UNWIELDINESS));
            initialized = true;
        }
    }

    public static boolean isTickableAttributeEnchantment(Holder<Enchantment> enchantment) {
        ensureInitialized();
        return enchantment.unwrapKey().map(tickableEnchantments::containsKey).orElse(false);
    }

    public static TickableAttributeEnchantment getTickableAttributeEnchantment(Holder<Enchantment> enchantment) {
        ensureInitialized();
        return enchantment.unwrapKey().map(tickableEnchantments::get).orElse(null);
    }

    public static int getItemEnchantmentLevel(ResourceKey<Enchantment> enchantment, ItemStack stack) {
        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        int level = 0;
        for (var entry : enchantments.entrySet()) {
            if (entry.getKey().is(enchantment)) {
                level = Math.max(level, entry.getIntValue());
            }
        }
        return level;
    }

    public static int getEnchantmentLevel(ResourceKey<Enchantment> enchantment, LivingEntity entity) {
        int level = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            level = Math.max(level, getItemEnchantmentLevel(enchantment, entity.getItemBySlot(slot)));
        }
        return level;
    }

    public static Optional<Holder.Reference<Enchantment>> getHolder(RegistryAccess registryAccess, ResourceKey<Enchantment> enchantment) {
        return registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolder(enchantment);
    }

    public static void init() {
        BMConfig.ensureLoaded();
    }
}
