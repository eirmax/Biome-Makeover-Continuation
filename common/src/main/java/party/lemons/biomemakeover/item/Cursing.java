package party.lemons.biomemakeover.item;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.BMConfig;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.stream.Collectors;

public class Cursing
{
    private static final List<Holder<Enchantment>> curses = Lists.newArrayList();

    public static boolean isValidForCurse(ItemStack stack)
    {
        if(stack.isEmpty() || stack.getItem() == Items.ENCHANTED_BOOK) return false;

        if(stack.getItem() == Items.BOOK) return true;

        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        if(stack.has(DataComponents.CUSTOM_DATA) &&
                stack.get(DataComponents.CUSTOM_DATA).copyTag().getBoolean("BMCursed")) {
            return false;
        }

        if(enchantments.isEmpty()) return false;

        boolean hasNewCompatibleCurse = false;
        for(Holder<Enchantment> enchantmentHolder : enchantments.keySet())
        {
            Enchantment enchantment = enchantmentHolder.value();
            if(!enchantmentHolder.is(BMEnchantments.ALTAR_CANT_UPGRADE) &&
                    enchantment.getMaxLevel() > 1 &&
                    !enchantmentHolder.is(EnchantmentTags.CURSE) &&
                    (!BMConfig.INSTANCE.strictAltarCursing || enchantments.getLevel(enchantmentHolder) < enchantment.getMaxLevel() + 1)) {
                return true;
            }

            if(enchantmentHolder.is(EnchantmentTags.CURSE) &&
                    enchantment.canEnchant(stack) &&
                    !enchantments.keySet().contains(enchantmentHolder)) {
                hasNewCompatibleCurse = true;
            }
        }
        return hasNewCompatibleCurse;
    }

    public static Holder<Enchantment> getRandomCurse(RegistryAccess registryAccess, RandomSource random)
    {
        if(curses.isEmpty())
        {
            curses.addAll(registryAccess.registryOrThrow(Registries.ENCHANTMENT)
                    .holders()
                    .filter(holder -> holder.is(EnchantmentTags.CURSE) &&
                            !holder.is(BMEnchantments.ALTAR_CURSE_EXCLUDED))
                    .toList());
        }
        if(curses.isEmpty())
            return null;

        return curses.get(random.nextInt(curses.size()));
    }

    public static ItemStack curseItemStack(Level level, ItemStack stack, RandomSource random)
    {
        if(isValidForCurse(stack))
        {
            if(stack.getItem() == Items.BOOK)
            {
                Holder<Enchantment> curse = Cursing.getRandomCurse(level.registryAccess(), level.random);
                if(curse == null)
                    return ItemStack.EMPTY;

                return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(curse, 1));
            }

            ItemEnchantments currentEnchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(currentEnchantments);

            List<Holder<Enchantment>> validEnchants = currentEnchantments.keySet().stream().filter(
                    holder -> !holder.is(BMEnchantments.ALTAR_CANT_UPGRADE) &&
                            holder.value().getMaxLevel() > 1 &&
                            !holder.is(EnchantmentTags.CURSE) &&
                            (!BMConfig.INSTANCE.strictAltarCursing ||
                                    currentEnchantments.getLevel(holder) < holder.value().getMaxLevel() + 1)
            ).toList();

            if(validEnchants.isEmpty()) return ItemStack.EMPTY;

            Holder<Enchantment> toUpgrade = validEnchants.get(random.nextInt(validEnchants.size()));
            int currentLevel = enchantments.getLevel(toUpgrade);
            enchantments.set(toUpgrade, currentLevel + 1);

            Holder<Enchantment> curse = getRandomCurse(level.registryAccess(), random);
            if(curse == null)
                return ItemStack.EMPTY;

            int attempts = 0;
            while(enchantments.keySet().contains(curse) || !curse.value().canEnchant(stack))
            {
                curse = getRandomCurse(level.registryAccess(), random);
                if(curse == null)
                    return ItemStack.EMPTY;

                attempts++;
                if(attempts >= 100)
                {
                    curse = null;
                    break;
                }
            }

            if(curse == null)
            {
                List<Holder<Enchantment>> allEnchantments = level.registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT)
                        .holders()
                        .sorted((e, e1) -> RandomUtil.randomRange(-1, 1))
                        .collect(Collectors.toList());

                for(Holder<Enchantment> enchantmentHolder : allEnchantments)
                {
                    if(enchantmentHolder.is(EnchantmentTags.CURSE) &&
                            enchantmentHolder.value().canEnchant(stack) &&
                            !enchantments.keySet().contains(enchantmentHolder))
                    {
                        curse = enchantmentHolder;
                        break;
                    }
                }
            }

            if(curse == null)
                return ItemStack.EMPTY;

            Enchantment curseEnchantment = curse.value();
            int curseLevel = curseEnchantment.getMaxLevel() == 1 ? 1 :
                    RandomUtil.randomRange(curseEnchantment.getMinLevel(), curseEnchantment.getMaxLevel());
            enchantments.set(curse, curseLevel);

            stack.set(DataComponents.CUSTOM_DATA,
                    stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                            .update(tag -> tag.putBoolean("BMCursed", true)));

            stack.set(DataComponents.REPAIR_COST, 39);

            // Apply enchantments
            stack.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());

            return stack;
        }

        return ItemStack.EMPTY;
    }
}