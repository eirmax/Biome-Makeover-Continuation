package party.lemons.biomemakeover.neoforge;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

@Mod(Constants.MOD_ID)
public class BMNeoForge
{
    public BMNeoForge(IEventBus bus, ModContainer modContainer)
    {
        BiomeMakeover.init();

        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
            bus.addListener(BMNeoForgeClient::registerModelLayers);
            bus.addListener(BMNeoForgeClient::registerRenderers);
            bus.addListener(BMNeoForgeClient::registerMenuScreens);
            bus.addListener(BMNeoForgeClient::registerClientExtensions);
            bus.addListener(BMNeoForgeClient::initClient);
        });
    }
}
