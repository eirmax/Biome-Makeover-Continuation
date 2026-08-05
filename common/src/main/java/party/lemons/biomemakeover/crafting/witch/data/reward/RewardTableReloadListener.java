package party.lemons.biomemakeover.crafting.witch.data.reward;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import party.lemons.biomemakeover.Constants;

import java.util.Map;

public class RewardTableReloadListener extends SimpleJsonResourceReloadListener
{
	public static final String QUEST_CATEGORY = "quest_reward";
	private static final Gson GSON = new Gson();

	public RewardTableReloadListener()
	{
		super(GSON, QUEST_CATEGORY);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profilerFiller)
	{
		RewardTables.clear();

		elements.forEach((location, json)->{
			JsonObject object = json.getAsJsonObject();

			DataResult<RewardTable> tableParse = RewardTable.CODEC.parse(JsonOps.INSTANCE, object);
			if(tableParse.result().isPresent())
			{
				RewardTables.addTable(tableParse.result().get());
			}
			else
			{
				Constants.LOG.error("Error loading Reward Table: {}", location);
				if(tableParse.error().isPresent())
					Constants.LOG.error(tableParse.error().get().message());
			}

		});
	}
}
