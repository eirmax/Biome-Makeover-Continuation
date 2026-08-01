package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.taniwha.data.criterion.SimpleCriterion;

public class BMAdvancements
{
    private static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Constants.MOD_ID, Registries.TRIGGER_TYPE);

    public static final SimpleCriterion ECTOPLASM_COMPOST = new SimpleCriterion(BiomeMakeover.ID("ectoplasm_compost"));
    public static final SimpleCriterion PEAT_COMPOST = new SimpleCriterion(BiomeMakeover.ID("peat_compost"));
    public static final SimpleCriterion ARM_GOLEM = new SimpleCriterion(BiomeMakeover.ID("arm_golem"));
    public static final SimpleCriterion WITCH_TRADE = new SimpleCriterion(BiomeMakeover.ID("witch_trade"));
    public static final SimpleCriterion GLOWFISH_SAVE = new SimpleCriterion(BiomeMakeover.ID("glowfish_bucket_save"));
    public static final SimpleCriterion POLTERGEIST_YOURSELF = new SimpleCriterion(BiomeMakeover.ID("poltergeist_yourself"));
    public static final SimpleCriterion ANTIDOTE = new SimpleCriterion(BiomeMakeover.ID("antidote"));

    static {
        TRIGGERS.register(ECTOPLASM_COMPOST.getId(), () -> ECTOPLASM_COMPOST);
        TRIGGERS.register(PEAT_COMPOST.getId(), () -> PEAT_COMPOST);
        TRIGGERS.register(ARM_GOLEM.getId(), () -> ARM_GOLEM);
        TRIGGERS.register(WITCH_TRADE.getId(), () -> WITCH_TRADE);
        TRIGGERS.register(GLOWFISH_SAVE.getId(), () -> GLOWFISH_SAVE);
        TRIGGERS.register(POLTERGEIST_YOURSELF.getId(), () -> POLTERGEIST_YOURSELF);
        TRIGGERS.register(ANTIDOTE.getId(), () -> ANTIDOTE);
    }

    public static void init()
    {
        TRIGGERS.register();
    }
}
