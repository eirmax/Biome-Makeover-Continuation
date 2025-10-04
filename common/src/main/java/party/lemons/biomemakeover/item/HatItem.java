package party.lemons.biomemakeover.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.init.ArmorMaterialRegistry;

import static party.lemons.biomemakeover.init.ArmorMaterialRegistry.HAT_ARMOR_MATERIAL;

public class HatItem extends ArmorItem
{
    private final ResourceLocation hatTexture;

    public HatItem(ResourceLocation hatTexture, Properties properties)
    {
        super(BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(HAT_ARMOR_MATERIAL), Type.HELMET, properties);
        this.hatTexture = hatTexture;
    }
    public ResourceLocation getHatTexture()
    {
        return hatTexture;
    }


}