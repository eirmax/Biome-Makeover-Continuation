package party.lemons.biomemakeover.mobeffect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import party.lemons.biomemakeover.level.PoltergeistHandler;
import party.lemons.taniwha.entity.effect.TMobEffect;

public class PossessedEffect extends TMobEffect
{
    public PossessedEffect() {
        super(MobEffectCategory.HARMFUL, 0x20c09e);
    }

    public boolean applyEffectTick(LivingEntity livingEntity, int effectLevel) {
        if(livingEntity.level().isClientSide())
            return false;

        for(int i = 0; i < Math.min(effectLevel + 1, 20); i++)
        {
            PoltergeistHandler.doPoltergeist(livingEntity.level(), livingEntity, livingEntity.blockPosition(), 4);
        }
        return false;
    }

    public boolean isDurationEffectTick(int time, int level) {
        return time % 10 < Math.min(level + 1, 8);
    }
}
