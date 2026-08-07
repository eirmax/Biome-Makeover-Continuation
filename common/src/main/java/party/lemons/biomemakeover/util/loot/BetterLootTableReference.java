package party.lemons.biomemakeover.util.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import party.lemons.biomemakeover.init.BMItems;

import java.util.List;
import java.util.function.Consumer;

public class BetterLootTableReference extends LootPoolSingletonContainer {
    public static final MapCodec<BetterLootTableReference> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("name").forGetter(ref -> ref.name)
            ).and(singletonFields(instance)).apply(instance, BetterLootTableReference::new)
    );

    final ResourceLocation name;

    BetterLootTableReference(ResourceLocation name, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.name = name;
    }

    public LootPoolEntryType getType() {
        return BMItems.BETTER_LOOTTABLE_REFERENCE.get();
    }


    public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        ResourceKey<LootTable> lootTableKey = ResourceKey.create(LootDataType.TABLE.registryKey(), this.name);
        lootContext.getResolver()
                .get(LootDataType.TABLE.registryKey(), lootTableKey)
                .map(ref -> ref.value())
                .orElse(LootTable.EMPTY)
                .getRandomItemsRaw(lootContext, consumer);
    }


    public void validate(ValidationContext validationContext) {
        ResourceKey<LootTable> lootTableKey = ResourceKey.create(LootDataType.TABLE.registryKey(), this.name);
        if (validationContext.hasVisitedElement(lootTableKey)) {
            validationContext.reportProblem("Table " + this.name + " is recursively called");
        } else {
            super.validate(validationContext);
            validationContext.resolver()
                    .get(LootDataType.TABLE.registryKey(), lootTableKey)
                    .map(ref -> ref.value())
                    .ifPresentOrElse(table -> {
                        table.validate(validationContext.enterElement("->{" + this.name + "}", lootTableKey));
                    }, () -> {
                        validationContext.reportProblem("Unknown loot table called " + this.name);
                    });
        }
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableReference(ResourceLocation resourceLocation) {
        return simpleBuilder((weight, quality, conditions, functions) -> {
            return new BetterLootTableReference(resourceLocation, weight, quality, conditions, functions);
        });
    }
}
