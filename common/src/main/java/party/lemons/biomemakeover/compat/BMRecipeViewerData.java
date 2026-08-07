package party.lemons.biomemakeover.compat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BMRecipeViewerData {
    private static final Gson GSON = new Gson();

    private static final String[] QUEST_CATEGORIES = {
            "common", "dark_forest", "flower", "jungle", "mesa", "mushroom", "nether", "ocean", "rare", "swamp"
    };
    private static final String[] REWARD_TABLES = {
            "items", "multi_potions", "potion", "potion_ingredients"
    };


    public static List<AltarInfo> altarDisplays() {
        return List.of(
                new AltarInfo(
                        BiomeMakeover.ID("altar/book"),
                        List.of(new ItemStack(Items.BOOK), new ItemStack(BMItems.ILLUNITE_SHARD.get())),
                        named(new ItemStack(Items.ENCHANTED_BOOK), "recipe.biomemakeover.altar.random_curse_book"),
                        Component.translatable("recipe.biomemakeover.altar.book_info")
                ),
                new AltarInfo(
                        BiomeMakeover.ID("altar/enchanted_item"),
                        List.of(glint(new ItemStack(Items.DIAMOND_SWORD)), new ItemStack(BMItems.ILLUNITE_SHARD.get())),
                        named(glint(new ItemStack(Items.DIAMOND_SWORD)), "recipe.biomemakeover.altar.cursed_item"),
                        Component.translatable("recipe.biomemakeover.altar.item_info")
                )
        );
    }

    public static List<WitchQuestInfo> witchQuestDisplays() {
        List<WitchQuestInfo> displays = new ArrayList<>();
        for (String category : QUEST_CATEGORIES) {
            JsonObject json = readJson("data/biomemakeover/quest_category/" + category + ".json");
            if (json == null) {
                continue;
            }

            List<ItemStack> requests = new ArrayList<>();

            JsonArray requestArray = json.getAsJsonArray("requests");

            if (requestArray != null) {
                for (JsonElement requestElement : requestArray) {
                    JsonObject request = requestElement.getAsJsonObject();
                    Item item = item(request.get("item").getAsString());
                    if (item == Items.AIR) {
                        continue;
                    }

                    int maxCount = request.get("max_count").getAsInt();
                    requests.add(new ItemStack(item, clampStackCount(maxCount)));
                }
            }

            if (!requests.isEmpty()) {
                displays.add(new WitchQuestInfo(BiomeMakeover.ID("witch_quest/" + category), category, json.get("weight").getAsInt(), requests));
            }
        }
        return displays;
    }

    public static List<WitchRewardInfo> witchRewardDisplays() {
        List<WitchRewardInfo> displays = new ArrayList<>();
        for (String table : REWARD_TABLES) {
            JsonObject json = readJson("data/biomemakeover/quest_reward/" + table + ".json");
            if (json == null) {
                continue;
            }

            List<ItemStack> rewards = new ArrayList<>();
            JsonArray rewardArray = json.getAsJsonArray("rewards");
            if (rewardArray != null) {
                for (JsonElement rewardElement : rewardArray) {
                    JsonObject reward = rewardElement.getAsJsonObject();
                    ItemStack stack = rewardStack(reward);
                    if (!stack.isEmpty()) {
                        rewards.add(stack);
                    }
                }
            }

            if (!rewards.isEmpty()) {
                displays.add(new WitchRewardInfo(BiomeMakeover.ID("witch_reward/" + table), table, rewards));
            }
        }
        return displays;
    }

    public static Component displayName(String id) {
        return Component.translatable("recipe.biomemakeover." + id);
    }

    public static Component categoryText(String category) {
        return Component.literal(title(category));
    }

    private static JsonObject readJson(String path) {
        try (InputStream stream = BMRecipeViewerData.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                return null;
            }
            return GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static ItemStack rewardStack(JsonObject reward) {
        String type = reward.get("type").getAsString();
        if (type.endsWith(":potion")) {
            ResourceLocation potionId = ResourceLocation.parse(reward.get("potion").getAsString());

            Holder<Potion> potion = BuiltInRegistries.POTION.getHolder(potionId).orElse(null);

            if (potion == null) {
                return named(new ItemStack(Items.POTION), potionId.toString());
            }
            return PotionContents.createItemStack(Items.POTION, potion);
        }

        if (type.endsWith(":item")) {
            Item item = item(reward.get("item").getAsString());
            if (item == Items.AIR) {
                return ItemStack.EMPTY;
            }
            int min = reward.has("min") ? reward.get("min").getAsInt() : 1;
            int max = reward.has("max") ? reward.get("max").getAsInt() : min;
            return new ItemStack(item, clampStackCount(Math.max(min, max)));
        }

        return ItemStack.EMPTY;
    }

    private static Item item(String id) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ItemStack glint(ItemStack stack) {
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        return stack;
    }

    private static ItemStack named(ItemStack stack, String translationKey) {
        stack.set(DataComponents.CUSTOM_NAME, Component.translatable(translationKey));
        return stack;
    }

    private static int clampStackCount(int count) {
        return Math.clamp(count, 1, 64);
    }

    private static String title(String id) {
        String[] parts = id.split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(part.substring(0, 1).toUpperCase(Locale.ROOT));
            builder.append(part.substring(1));
        }
        return builder.toString();
    }

    public record AltarInfo(ResourceLocation id, List<ItemStack> inputs, ItemStack output, Component note) {
    }

    public record WitchQuestInfo(ResourceLocation id, String category, int weight, List<ItemStack> requests) {
        public Component note() {
            return Component.translatable("recipe.biomemakeover.witch_requests.note", categoryText(category), weight);
        }
    }

    public record WitchRewardInfo(ResourceLocation id, String table, List<ItemStack> rewards) {
        public Component note() {
            return Component.translatable("recipe.biomemakeover.witch_rewards.note", categoryText(table));
        }
    }
}
