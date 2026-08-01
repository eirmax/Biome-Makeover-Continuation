package party.lemons.biomemakeover.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Iterator;

public class CowboyEntity extends Pillager {
    public CowboyEntity(EntityType<? extends Pillager> entityType, Level level) {
        super(entityType, level);

        armorDropChances[0] = 0.25F;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance)
    {
        super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(BMItems.COWBOY_HAT.get()));
    }

    @Override
    public void tick() {
        super.tick();

        if(isPassenger())
        {
            Entity v = getVehicle();
            v.setYRot(getYRot());
        }
    }

    @Override
    public void die(DamageSource damageSource)
    {
        //Doesn't trigger bad omen without this since it checks if the banner == raid banner
        if (this.level() instanceof ServerLevel) {
            Entity entity = damageSource.getEntity();
            if (this.isPatrolLeader() && raid == null && ((ServerLevel)this.level()).getRaidAt(this.blockPosition()) == null) {
                ItemStack itemStack = this.getItemBySlot(EquipmentSlot.HEAD);
                ServerPlayer player = null;
                Entity entity2 = entity;
                if (entity2 instanceof ServerPlayer sp) {
                    player = sp;
                }
                else if (entity2 instanceof Wolf)
                {
                    Wolf wolf = (Wolf)entity2;
                    LivingEntity livingEntity = wolf.getOwner();
                    if (wolf.isTame() && livingEntity instanceof ServerPlayer sp) {
                        player = sp;
                    }
                }
                if (!itemStack.isEmpty() && ItemStack.matches(itemStack, getOminousBanner()) && player != null) {
                    MobEffectInstance mobEffectInstance = player.getEffect(MobEffects.BAD_OMEN);
                    int i = 1;
                    if (mobEffectInstance != null) {
                        i += mobEffectInstance.getAmplifier();
                        player.removeEffectNoUpdate(MobEffects.BAD_OMEN);
                    } else {
                        --i;
                    }
                    i = Mth.clamp(i, 0, 4);
                    MobEffectInstance mobEffectInstance2 = new MobEffectInstance(MobEffects.BAD_OMEN, 120000, i, false, false, true);
                    if (!this.level().getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
                        player.addEffect(mobEffectInstance2);
                    }

                    grantAdvancement(player);
                }
            }
        }
        super.die(damageSource);
    }

    /*
        Grants the "Voluntary Exile" advancement to player
        This isn't automatically triggered because of the advancement specifically looking for the vanilla banner
     */
    private void grantAdvancement(ServerPlayer player)
    {
        AdvancementHolder advancement = player.level().getServer().getAdvancements().get(ResourceLocation.withDefaultNamespace("adventure/voluntary_exile"));
        if(advancement != null)
        {
            AdvancementProgress advancementProgress = player.getAdvancements().getOrStartProgress(advancement);
            if (!advancementProgress.isDone()) {
                for (String string : advancementProgress.getRemainingCriteria()) {
                    player.getAdvancements().award(advancement, string);
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
        if(isPatrolLeader())
        {
            this.setItemSlot(EquipmentSlot.HEAD, getOminousBanner());
            this.armorDropChances[0] = 2.0F;
        }
        return data;
    }

    public static ItemStack getOminousBanner()
    {
        ItemStack itemStack = new ItemStack(Items.WHITE_BANNER);

        CompoundTag blockEntityTag = new CompoundTag();
        ListTag patterns = new ListTag();

        patterns.add(createPatternTag(BannerPatterns.RHOMBUS_MIDDLE, DyeColor.CYAN));
        patterns.add(createPatternTag(BannerPatterns.STRIPE_BOTTOM, DyeColor.RED));
        patterns.add(createPatternTag(BannerPatterns.HALF_HORIZONTAL, DyeColor.BROWN));
        patterns.add(createPatternTag(BannerPatterns.TRIANGLES_TOP, DyeColor.BLACK));
        patterns.add(createPatternTag(BannerPatterns.BORDER, DyeColor.BLACK));
        patterns.add(createPatternTag(BannerPatterns.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY));
        patterns.add(createPatternTag(BannerPatterns.STRIPE_MIDDLE, DyeColor.BROWN));

        blockEntityTag.put("patterns", patterns);
        itemStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));

        itemStack.set(DataComponents.CUSTOM_NAME, Component.translatable("block.biomemakeover.cowboy_banner").withStyle(ChatFormatting.GOLD));

        itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);

        return itemStack;
    }

    private static CompoundTag createPatternTag(ResourceKey<BannerPattern> pattern, DyeColor color) {
        CompoundTag patternTag = new CompoundTag();
        patternTag.putString("pattern", pattern.location().toString());
        patternTag.putInt("color", color.getId());
        return patternTag;
    }
}
