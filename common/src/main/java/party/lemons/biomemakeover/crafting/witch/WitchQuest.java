package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class WitchQuest
{
    private final ItemStack[] requiredItems;
    private float rarityPoints;
    private ItemStack itemStack;

    public WitchQuest(RandomSource random, List<QuestItem> items)
    {
        requiredItems = new ItemStack[items.size()];
        for(int i = 0; i < items.size(); i++)
        {
            QuestItem qi = items.get(i);
            rarityPoints += qi.getPoints();
            requiredItems[i] = qi.createStack(random);
        }
    }

    public WitchQuest(CompoundTag tag, HolderLookup.Provider registryAccess)
    {
        rarityPoints = tag.getFloat("Points");
        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);

        requiredItems = new ItemStack[items.size()];
        for(int i = 0; i < items.size(); i++)
        {
            CompoundTag itemTag = items.getCompound(i);
            requiredItems[i] = ItemStack.parseOptional(registryAccess, itemTag);
        }
    }

    public WitchQuest(FriendlyByteBuf buffer)
    {
        rarityPoints = buffer.readFloat();
        int length = buffer.readByte() & 255;
        requiredItems = new ItemStack[length];

        for(int i = 0; i < length; i++)
        {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(buffer.readUtf()));
            requiredItems[i] = new ItemStack(item, buffer.readVarInt());
        }
    }

    public ItemStack[] getRequiredItems()
    {
        return requiredItems;
    }

    public boolean isValid()
    {
        if(requiredItems.length == 0)
            return false;

        for(ItemStack stack : requiredItems)
        {
            if(stack.isEmpty() || stack.is(Items.AIR))
                return false;
        }

        return true;
    }

    public boolean hasItems(Container inventory)
    {
        int size = getRequiredItems().length;
        for(int i = 0; i < size; i++)
        {
            ItemStack st = getRequiredItems()[i];
            int count = inventory.countItem(st.getItem());

            if(count < st.getCount()) return false;
        }
        return true;
    }

    //Assumes inventory has items!!
    public void consumeItems(Inventory inventory)
    {
        for(int i = 0; i < inventory.getContainerSize(); i++)
        {
            ItemStack invStack = inventory.getItem(i);
            if(invStack.isEmpty()) continue;

            for(ItemStack stack : getRequiredItems())
            {
                if(stack.isEmpty()) continue;

                if(stack.getItem() == invStack.getItem())
                {
                    if(stack.getCount() <= invStack.getCount())
                    {
                        invStack.shrink(stack.getCount());
                        stack.setCount(0);
                    }else
                    {
                        stack.shrink(invStack.getCount());
                        invStack.setCount(0);
                    }
                }
            }
        }
    }

    public float getPoints()
    {
        return rarityPoints;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder("WQ | ");
        for(ItemStack st : requiredItems)
        {
            s.append(st.toString());
        }
        s.append(" | ").append(QuestRarity.getRarityFromPoints(rarityPoints));
        return s.toString();
    }

    public CompoundTag toTag(HolderLookup.Provider registryAccess)
    {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Points", rarityPoints);

        ListTag items = new ListTag();
        for(int i = 0; i < requiredItems.length; i++)
        {
            if(requiredItems[i].isEmpty())
                continue;

            CompoundTag itemTag = new CompoundTag();
            requiredItems[i].save(registryAccess, itemTag);
            items.add(itemTag);
        }
        tag.put("Items", items);
        return tag;
    }

    public void toPacket(FriendlyByteBuf buffer)
    {
        buffer.writeFloat(rarityPoints);

        int length = this.requiredItems.length;
        buffer.writeByte((byte) (length & 255));

        for(int i = 0; i < length; i++)
        {
            buffer.writeUtf(BuiltInRegistries.ITEM.getKey(requiredItems[i].getItem()).toString());
            buffer.writeVarInt(requiredItems[i].getCount());
        }
    }
}
