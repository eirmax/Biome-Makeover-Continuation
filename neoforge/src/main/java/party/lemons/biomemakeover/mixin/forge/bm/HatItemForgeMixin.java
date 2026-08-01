package party.lemons.biomemakeover.mixin.forge.bm;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.item.HatItem;

@Mixin(HatItem.class)
public abstract class HatItemForgeMixin extends ArmorItem
{
    @Shadow @Final
    private ResourceLocation hatTexture;

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return hatTexture;
    }

    private HatItemForgeMixin(Holder<ArmorMaterial> arg, Type arg2, Properties arg3) {
        super(arg, arg2, arg3);
    }
}
