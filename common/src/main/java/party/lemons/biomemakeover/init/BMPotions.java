package party.lemons.biomemakeover.init;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.mobeffect.AntidoteMobEffect;
import party.lemons.biomemakeover.mobeffect.NocturnalMobEffect;
import party.lemons.biomemakeover.mobeffect.PossessedEffect;
import party.lemons.taniwha.entity.effect.TMobEffect;
import party.lemons.taniwha.hooks.block.BrewingStandHooks;


public class BMPotions
{
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Constants.MOD_ID, Registries.MOB_EFFECT);
    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Constants.MOD_ID, Registries.POTION);


    public static final RegistrySupplier<MobEffect> SHOCKED = EFFECTS.register(BiomeMakeover.ID("shocked"), ()->new TMobEffect(MobEffectCategory.HARMFUL, 0x6effff).addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.parse("ad5a6d44-4a23-11eb-b378-0242ac130002"), -2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final RegistrySupplier<MobEffect>  ANTIDOTE = EFFECTS.register(BiomeMakeover.ID("antidote"), AntidoteMobEffect::new);
    public static final RegistrySupplier<MobEffect>  NOCTURNAL = EFFECTS.register(BiomeMakeover.ID("nocturnal"), NocturnalMobEffect::new);
    public static final RegistrySupplier<MobEffect>  POSSESSED = EFFECTS.register(BiomeMakeover.ID("possessed"), PossessedEffect::new);

    public static final RegistrySupplier<Potion> ADRENALINE = POTIONS.register(BiomeMakeover.ID("adrenaline"), ()->new Potion("adrenaline", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 1), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400)));
    public static final RegistrySupplier<Potion> ASSASSIN = POTIONS.register(BiomeMakeover.ID("assassin"), ()->new Potion("assassin", new MobEffectInstance(MobEffects.INVISIBILITY, 2400), new MobEffectInstance(MobEffects.SLOW_FALLING, 2400, 1), new MobEffectInstance(MobEffects.JUMP, 2400, 2)));
    public static final RegistrySupplier<Potion> DARKNESS = POTIONS.register(BiomeMakeover.ID("darkness"), ()->new Potion("darkness", new MobEffectInstance(MobEffects.BLINDNESS, 300), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0), new MobEffectInstance(MobEffects.WEAKNESS, 170, 0)));
    public static final RegistrySupplier<Potion> DOLPHIN_MASTER = POTIONS.register(BiomeMakeover.ID("dolphin_master"), ()->new Potion("dolphin_master", new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1800), new MobEffectInstance(MobEffects.WATER_BREATHING, 1800)));
    public static final RegistrySupplier<Potion> LIQUID_BREAD = POTIONS.register(BiomeMakeover.ID("liquid_bread"), ()->new Potion("liquid_bread", new MobEffectInstance(MobEffects.SATURATION, 1800), new MobEffectInstance(MobEffects.ABSORPTION, 1800, 4)));
    public static final RegistrySupplier<Potion> PHANTOM_SPIRIT = POTIONS.register(BiomeMakeover.ID("phantom_spirit"), ()->new Potion("phantom_spirit", new MobEffectInstance(MobEffects.NIGHT_VISION, 2400), new MobEffectInstance(MobEffects.LEVITATION, 600, 0), new MobEffectInstance(MobEffects.SLOW_FALLING, 1000)));
    public static final RegistrySupplier<Potion> LIGHT_FOOTED = POTIONS.register(BiomeMakeover.ID("light_footed"), ()->new Potion("light_footed", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), new MobEffectInstance(MobEffects.JUMP, 1200, 0), new MobEffectInstance(MobEffects.SLOW_FALLING, 1200)));
    public static final RegistrySupplier<Potion> MINER = POTIONS.register(BiomeMakeover.ID("miner"), ()->new Potion("miner", new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 1), new MobEffectInstance(MobEffects.NIGHT_VISION, 4250, 0), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200)));

    public static final RegistrySupplier<Potion> ANTIDOTE_POT = POTIONS.register(BiomeMakeover.ID("actidote_pot"), ()->new Potion("antidote", new MobEffectInstance(antidote(), 1)));
    public static final RegistrySupplier<Potion> NOCTURNAL_POT = POTIONS.register(BiomeMakeover.ID("nocturnal_pot"), ()->new Potion("nocturnal", new MobEffectInstance(nocturnal(), 72000)));
    public static final RegistrySupplier<Potion> LONG_NOCTURNAL_POT = POTIONS.register(BiomeMakeover.ID("long_nocturnal_pot"), ()->new Potion("nocturnal", new MobEffectInstance(nocturnal(), 144000)));

    public static void init()
    {
        EFFECTS.register();
        POTIONS.register();

        LifecycleEvent.SETUP.register(() -> {
            BrewingStandHooks.addMix(Potions.AWKWARD, BMItems.WART.get(), antidotePotion());
            BrewingStandHooks.addMix(Potions.AWKWARD, BMItems.SCUTTLER_TAIL.get(), antidotePotion());
            BrewingStandHooks.addMix(Potions.AWKWARD, BMItems.MOTH_SCALES.get(), nocturnalPotion());
            BrewingStandHooks.addMix(nocturnalPotion(), Items.REDSTONE, longNocturnalPotion());
        });

    }

    public static Holder<MobEffect> shocked() {
        return effectHolder(SHOCKED);
    }

    public static Holder<MobEffect> antidote() {
        return effectHolder(ANTIDOTE);
    }

    public static Holder<MobEffect> nocturnal() {
        return effectHolder(NOCTURNAL);
    }

    public static Holder<MobEffect> possessed() {
        return effectHolder(POSSESSED);
    }

    public static Holder<Potion> antidotePotion() {
        return potionHolder(ANTIDOTE_POT);
    }

    public static Holder<Potion> nocturnalPotion() {
        return potionHolder(NOCTURNAL_POT);
    }

    public static Holder<Potion> longNocturnalPotion() {
        return potionHolder(LONG_NOCTURNAL_POT);
    }

    private static Holder<MobEffect> effectHolder(RegistrySupplier<MobEffect> effect) {
        ResourceKey<MobEffect> key = ResourceKey.create(Registries.MOB_EFFECT, effect.getId());
        return BuiltInRegistries.MOB_EFFECT.getHolder(key).orElseThrow(() -> new IllegalStateException("Missing mob effect: " + key.location()));
    }

    private static Holder<Potion> potionHolder(RegistrySupplier<Potion> potion) {
        ResourceKey<Potion> key = ResourceKey.create(Registries.POTION, potion.getId());
        return BuiltInRegistries.POTION.getHolder(key).orElseThrow(() -> new IllegalStateException("Missing potion: " + key.location()));
    }
}
