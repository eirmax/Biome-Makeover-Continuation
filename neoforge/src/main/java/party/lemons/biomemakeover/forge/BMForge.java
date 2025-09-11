package party.lemons.biomemakeover.forge;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.fml.common.Mod;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

@Mod(value = Constants.MOD_ID)
public class BMForge
{
    public BMForge()
    {
        BiomeMakeover.init();
        EnvExecutor.runInEnv(Env.CLIENT, ()-> BMForgeClient::initClient);

    }
}
