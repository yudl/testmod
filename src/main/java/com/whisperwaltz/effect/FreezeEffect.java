package com.whisperwaltz.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * Marker effect applied when Whisperwaltz freezes an enemy.
 * Maintains the entity's powder-snow freeze state every tick so
 * the vanilla slowness kicks in and the client ice-box renderer
 * knows to draw the cage.
 */
public class FreezeEffect extends MobEffect {

    public FreezeEffect() {
        super(MobEffectCategory.HARMFUL, 0x55CCFF);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // Re-apply frozen state every tick so it doesn't fall off mid-effect
        int required = entity.getTicksRequiredToFreeze();
        if (entity.getTicksFrozen() < required) {
            entity.setTicksFrozen(required + 5);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
