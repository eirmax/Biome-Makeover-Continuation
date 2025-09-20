package party.lemons.biomemakeover.crafting.witch.data.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import party.lemons.biomemakeover.util.RandomUtil;

public class PotionQuestRewardItem extends QuestRewardItem
{
	private final Holder<Potion> potion;

	public PotionQuestRewardItem(Potion potion)
	{
		this.potion = (Holder<Potion>) potion;
	}

	@Override
	RewardItemType<?> type()
	{
		return QuestRewardItem.POTION.get();
	}

	@Override
	public ItemStack getReward(RandomSource randomSource)
	{
		Item it = Items.POTION;
		if (RandomUtil.RANDOM.nextInt(4) == 0) if (RandomUtil.RANDOM.nextInt(3) == 0) {
			it = Items.LINGERING_POTION;
		} else {
			it = Items.SPLASH_POTION;
		}
		return PotionContents.createItemStack(new ItemStack(it).getItem(), potion);
	}

	public static final Codec<PotionQuestRewardItem> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
							BuiltInRegistries.POTION.byNameCodec().fieldOf("potion").forGetter(i-> i.potion.value())
					)
					.apply(instance, PotionQuestRewardItem::new));
}
