package party.lemons.biomemakeover.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.taniwha.data.trade.listing.TItemListing;
import party.lemons.taniwha.data.trade.listing.TradeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuspiciousStewListing extends TItemListing
{
	public static final Codec<SuspiciousStewListing> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
							ItemCost.CODEC.fieldOf("item1").forGetter(i-> i.item1),
							ItemCost.CODEC.fieldOf("item2").forGetter(i-> i.item2),
							EffectDuration.CODEC.listOf().fieldOf("effects").forGetter(i->i.effects),
							Codec.INT.optionalFieldOf("uses", 0).forGetter(i->i.uses),
							Codec.INT.fieldOf("max_uses").forGetter(i->i.maxUses),
							Codec.INT.optionalFieldOf("xp", 1).forGetter(i->i.xp),
							Codec.FLOAT.optionalFieldOf("price_multiplier", 0.05F).forGetter(i->i.priceMultiplier),
							Codec.INT.optionalFieldOf("demand", 0).forGetter(i->i.demand)
					)
					.apply(instance, SuspiciousStewListing::new));

	private final ItemCost item1;
	private final ItemCost item2;
	private final List<EffectDuration> effects;
	private final int uses;
	private final int maxUses;
	private final int xp;
	private final float priceMultiplier;
	private final int demand;

	public SuspiciousStewListing(ItemCost item1, ItemCost item2, List<EffectDuration> effects, int uses, int maxUses, int xp, float priceMultiplier, int demand)
	{
		this.item1 = item1;
		this.item2 = item2;
		this.effects = effects;
		this.uses = uses;
		this.maxUses = maxUses;
		this.xp = xp;
		this.priceMultiplier = priceMultiplier;
		this.demand = demand;
	}

	@Override
	public TradeTypes.TradeType<?> type()
	{
		return BMItems.SUSPICIOUS_STEW_TRADE.get();
	}

	@Override
	public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
		ItemStack result = new ItemStack(Items.SUSPICIOUS_STEW);
		EffectDuration effect = effects.get(randomSource.nextInt(effects.size()));

		SuspiciousStewEffects.Entry newEffect = new SuspiciousStewEffects.Entry(
				Holder.direct(effect.effect()),
				effect.duration()
		);

		SuspiciousStewEffects stewEffects = result.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
		if (stewEffects == null) {
			stewEffects = SuspiciousStewEffects.EMPTY;
		}

		List<SuspiciousStewEffects.Entry> effectList = new ArrayList<>(stewEffects.effects());
		effectList.add(newEffect);

		result.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffects(effectList));

		return new MerchantOffer(item1, Optional.ofNullable(item2), result, uses, maxUses, xp, priceMultiplier, demand);
	}

	private record EffectDuration(MobEffect effect, int duration)
	{
		public static final Codec<EffectDuration> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
							BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(EffectDuration::effect),
							Codec.INT.fieldOf("duration").forGetter(EffectDuration::duration)
						)
						.apply(instance, EffectDuration::new));
	}
}
