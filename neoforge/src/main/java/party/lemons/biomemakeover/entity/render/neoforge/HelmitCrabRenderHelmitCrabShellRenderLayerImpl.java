package party.lemons.biomemakeover.entity.render.neoforge;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import party.lemons.biomemakeover.entity.HelmitCrabEntity;
import party.lemons.biomemakeover.entity.render.HatModels;
import party.lemons.biomemakeover.entity.render.HelmitCrabRender;
import party.lemons.biomemakeover.item.HatItem;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

public class HelmitCrabRenderHelmitCrabShellRenderLayerImpl
{
	public static Model getHelmetModel(HelmitCrabEntity entity, HumanoidModel bipedModel, ItemStack stack)
	{
		return IClientItemExtensions.of(stack).getGenericArmorModel(entity, stack, EquipmentSlot.HEAD, bipedModel);
    }

	public static void renderHelmetPlatform(HelmitCrabEntity entity, ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, int light, HumanoidModel baseModel)
	{
		matrices.pushPose();

		if (stack.getItem() instanceof HatItem hat) {
			matrices.mulPose(Axis.XN.rotationDegrees(10F));
			matrices.translate(0, 1.5F, 0.3);
			Model model = HatModels.getHatModel(hat, baseModel.getHead());
			model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.renderType(hat.getHatTexture())), light, OverlayTexture.NO_OVERLAY);
			if (stack.hasFoil()) {
				model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY);
			}
			matrices.popPose();
			return;
		}

		Model model = getHelmetModel(entity, baseModel, stack);
		if(model instanceof HumanoidModel)
		{
			matrices.translate(0, 0.65, 0.05);
			matrices.scale(1.1F, 1F, 1F);
		}
		else
		{
			matrices.mulPose(Axis.XN.rotationDegrees(10F));
			matrices.translate(0, 1.5F, 0.3);
		}

		ArmorItem armorItem = (ArmorItem) stack.getItem();

		// Render armor model for NeoForge compatibility
		renderModel(matrices, vertexConsumers, light, armorItem, model, false, 1.0F, 1.0F, 1.0F, getArmorResource(entity, stack, EquipmentSlot.HEAD, null));

		TrimMaterials.getFromIngredient(entity.level().registryAccess(), stack).ifPresent(arg3x -> HelmitCrabRender.renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, arg3x, model, false));
		if (stack.hasFoil()) {
			model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY);
		}

		matrices.popPose();
	}

	private static void renderModel(
			PoseStack arg, MultiBufferSource arg2, int i, ArmorItem arg3, Model arg4, boolean bl, float f, float g, float h, ResourceLocation armorResource
	) {
		VertexConsumer vertexconsumer = arg2.getBuffer(RenderType.armorCutoutNoCull(armorResource));
		arg4.renderToBuffer(arg, vertexconsumer, i, OverlayTexture.NO_OVERLAY);
	}

	public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
		ArmorItem item = (ArmorItem)stack.getItem();
		String texture = item.getMaterial().getRegisteredName();
		String domain = "minecraft";
		int idx = texture.indexOf(58);
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}

		String s1 = String.format(
				Locale.ROOT,
				"%s:textures/models/armor/%s_layer_%d%s.png",
				domain,
				texture,
				1,
				type == null ? "" : String.format(Locale.ROOT, "_%s", type)
		);
		ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);
		if (resourcelocation == null) {
			resourcelocation =  ResourceLocation.parse(s1);
			ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
		}

		return resourcelocation;
	}

	private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();

}
