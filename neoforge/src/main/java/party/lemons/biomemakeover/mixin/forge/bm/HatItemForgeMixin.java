package party.lemons.biomemakeover.mixin.forge.bm;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import party.lemons.biomemakeover.entity.render.HatModels;
import party.lemons.biomemakeover.item.HatItem;

import java.util.function.Consumer;

@Mixin(HatItem.class)
public abstract class HatItemForgeMixin extends ArmorItem
{
    @Shadow @Final
    private ResourceLocation hatTexture;


    @Unique
    public void biomemakeover$initialize(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(
                new IClientItemExtensions() {
                    @Override
                    public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
                    {
                        return HatModels.getHatModel(itemStack.getItem(), original.getHead());
                    }
                }
        );
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return hatTexture;
    }

    private HatItemForgeMixin(Holder<ArmorMaterial> arg, Type arg2, Properties arg3) {
        super(arg, arg2, arg3);
    }
}
