package party.lemons.biomemakeover.neoforge;

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

    }
}
