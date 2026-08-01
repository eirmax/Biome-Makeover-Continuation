package party.lemons.biomemakeover.neoforge;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.crafting.AltarScreen;
import party.lemons.biomemakeover.crafting.DirectionDataScreen;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMScreens;


public class BMNeoForgeClient
{
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        BMEntities.registerModelLayers(event::registerLayerDefinition);
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(BMEntities.TUMBLEWEED.get(), TumbleweedRender::new);
        event.registerEntityRenderer(BMEntities.LIGHTNING_BOTTLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(BMEntities.GLOWFISH.get(), GlowfishRender::new);
        event.registerEntityRenderer(BMEntities.BLIGHTBAT.get(), BlightBatRender::new);
        event.registerEntityRenderer(BMEntities.MUSHROOM_TRADER.get(), MushroomTraderRender::new);
        event.registerEntityRenderer(BMEntities.SCUTTLER.get(), ScuttlerRender::new);
        event.registerEntityRenderer(BMEntities.GHOST.get(), GhostRender::new);
        event.registerEntityRenderer(BMEntities.COWBOY.get(), CowboyRender::new);
        event.registerEntityRenderer(BMEntities.DECAYED.get(), DecayedRender::new);
        event.registerEntityRenderer(BMEntities.DRAGONFLY.get(), DragonflyRender::new);
        event.registerEntityRenderer(BMEntities.TOAD.get(), ToadRender::new);
        event.registerEntityRenderer(BMEntities.TADPOLE.get(), TadpoleRender::new);
        event.registerEntityRenderer(BMEntities.LIGHTNING_BUG.get(), LightningBugRender::new);
        event.registerEntityRenderer(BMEntities.LIGHTNING_BUG_ALTERNATE.get(), LightningBugRender::new);
        event.registerEntityRenderer(BMEntities.OWL.get(), OwlRender::new);
        event.registerEntityRenderer(BMEntities.MOTH.get(), MothRender::new);
        event.registerEntityRenderer(BMEntities.ROOTLING.get(), RootlingRender::new);
        event.registerEntityRenderer(BMEntities.ADJUDICATOR.get(), AdjudicatorRender::new);
        event.registerEntityRenderer(BMEntities.ADJUDICATOR_MIMIC.get(), AdjudicatorMimicRender::new);
        event.registerEntityRenderer(BMEntities.STONE_GOLEM.get(), StoneGolemRender::new);
        event.registerEntityRenderer(BMEntities.HELMIT_CRAB.get(), HelmitCrabRender::new);
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event)
    {
        event.register(BMScreens.WITCH.get(), WitchScreen::new);
        event.register(BMScreens.ALTAR.get(), AltarScreen::new);
        event.register(BMScreens.DIRECTIONAL_DATA.get(), DirectionDataScreen::new);
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event)
    {
        IClientItemExtensions hatExtensions = new IClientItemExtensions() {
            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
            {
                return HatModels.getHatModel(itemStack.getItem(), original.getHead());
            }
        };

        event.registerItem(hatExtensions, BMItems.COWBOY_HAT.get(), BMItems.WITCH_HAT.get());
    }

    public static void initClient(FMLClientSetupEvent event)
    {
        BiomeMakeoverClient.init();
    }
}
