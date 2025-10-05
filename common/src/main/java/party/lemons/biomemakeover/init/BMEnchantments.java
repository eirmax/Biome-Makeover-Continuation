package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import party.lemons.biomemakeover.BMConfig;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.item.enchantment.*;
import java.util.HashMap;
import java.util.Map;


public class BMEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(Constants.MOD_ID, Registries.ENCHANTMENT);
    public static final TagKey<Enchantment> ALTAR_CURSE_EXCLUDED = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_curse_excluded"));
    public static final TagKey<Enchantment> ALTAR_CANT_UPGRADE = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_cant_upgrade"));

    public static final Holder<Enchantment> DECAY_CURSE = ENCHANTS.register(BiomeMakeover.ID("decay_curse"), ()->new DecayCurseEnchantment(()-> BMConfig.INSTANCE.enchantmentConfig.DECAY).createEnchantment());
    public static final Holder<Enchantment> INSOMNIA_CURSE = ENCHANTS.register(BiomeMakeover.ID("insomnia_curse"), ()->new InsomniaCurseEnchantment(()-> BMConfig.INSTANCE.enchantmentConfig.INSOMNIA).createEnchantment());
    public static final Holder<Enchantment> CONDUCTIVITY_CURSE = ENCHANTS.register(BiomeMakeover.ID("conductivity_curse"), ()->new ConductivityCurseEnchantment(()-> BMConfig.INSTANCE.enchantmentConfig.CONDUCTIVITY).createEnchantment());
    public static final Holder<Enchantment> ENFEEBLEMENT_CURSE = ENCHANTS.register(BiomeMakeover.ID("enfeeblement_curse"), () -> new EnfeeblementCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.ENFEEBLEMENT).createEnchantment());
    public static final Holder<Enchantment> DEPTH_CURSE = ENCHANTS.register(BiomeMakeover.ID("depth_curse"), ()->new DepthsCurseEnchantment(()-> BMConfig.INSTANCE.enchantmentConfig.DEPTHS).createEnchantment());
    public static final Holder<Enchantment> FLAMMABILITY_CURSE = ENCHANTS.register(BiomeMakeover.ID("flammability_curse"), () -> BMEnchantmentHelper.createEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.FLAMMABILITY, true, Rarity.UNCOMMON, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}));
    public static final Holder<Enchantment> SUFFOCATION_CURSE = ENCHANTS.register(BiomeMakeover.ID("suffocation_curse"), ()-> BMEnchantmentHelper.createEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.SUFFOCATION, true, Rarity.UNCOMMON, new EquipmentSlot[]{EquipmentSlot.HEAD}));
    public static final Holder<Enchantment> UNWIELDINESS_CURSE = ENCHANTS.register(BiomeMakeover.ID("unwieldiness_curse"), ()-> BMEnchantmentHelper.createEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.UNWIELDINESS));
    public static final Holder<Enchantment> INACCURACY_CURSE = ENCHANTS.register(BiomeMakeover.ID("inaccuracy_curse"), ()-> BMEnchantmentHelper.createEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.INACCURACY, true, Rarity.UNCOMMON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static final Holder<Enchantment> BUCKLING_CURSE = ENCHANTS.register(BiomeMakeover.ID("buckling_curse"), ()-> BMEnchantmentHelper.createEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.BUCKLING, true, Rarity.UNCOMMON, new EquipmentSlot[]{EquipmentSlot.LEGS}));

    private static final Map<Enchantment, TickableAttributeEnchantment> tickableEnchantments = new HashMap<>();
    private static boolean initialized = false;

    private static void ensureInitialized() {
        if (!initialized) {
            try {
                BMConfig.ensureLoaded();
                tickableEnchantments.put(INSOMNIA_CURSE.value(), new InsomniaCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.INSOMNIA));
                tickableEnchantments.put(CONDUCTIVITY_CURSE.value(), new ConductivityCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.CONDUCTIVITY));
                tickableEnchantments.put(ENFEEBLEMENT_CURSE.value(), new EnfeeblementCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.ENFEEBLEMENT));
                tickableEnchantments.put(DEPTH_CURSE.value(), new DepthsCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.DEPTHS));
                tickableEnchantments.put(UNWIELDINESS_CURSE.value(), new UnwieldinessCurseEnchantment(() -> BMConfig.INSTANCE.enchantmentConfig.UNWIELDINESS));
                initialized = true;
            } catch (Exception e) {
                // Registry not ready yet, will retry later
            }
        }
    }

    public static boolean isTickableAttributeEnchantment(Enchantment enchantment) {
        ensureInitialized();
        return tickableEnchantments.containsKey(enchantment);
    }

    public static TickableAttributeEnchantment getTickableAttributeEnchantment(Enchantment enchantment) {
        ensureInitialized();
        return tickableEnchantments.get(enchantment);
    }

    public static void init() {
        // Register enchantments during the SETUP lifecycle event when registries are available
        LifecycleEvent.SETUP.register(() -> {
            BMConfig.ensureLoaded();
            ENCHANTS.register();
        });
    }
}