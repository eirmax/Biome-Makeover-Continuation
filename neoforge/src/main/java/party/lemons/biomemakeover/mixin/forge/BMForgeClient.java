package party.lemons.biomemakeover.mixin.forge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.Constants;


@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class BMForgeClient
{
    @SubscribeEvent
    public static void initClient()
    {
        BiomeMakeoverClient.init();
    }
}
