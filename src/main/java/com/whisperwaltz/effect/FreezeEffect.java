package com.whisperwaltz.effect;

import com.whisperwaltz.WhisperwaltzMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;

public class FreezeEffect extends MobEffect {

    public FreezeEffect() {
        super(MobEffectCategory.HARMFUL, 0x55CCFF);
        // ADD_MULTIPLIED_TOTAL of -1.0 collapses the final speed to 0
        // regardless of base speed or Speed potions: final = prev_total * (1 + (-1.0)) = 0
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(WhisperwaltzMod.MOD_ID, "freeze_slow"),
                -1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // Keep vanilla freeze visual active
        int required = entity.getTicksRequiredToFreeze();
        if (entity.getTicksFrozen() < required) {
            entity.setTicksFrozen(required + 5);
        }
        // Cancel any remaining horizontal momentum (knockback, etc.)
        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
        // Stop AI pathfinding every tick
        if (entity instanceof Mob mob) {
            mob.getNavigation().stop();
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
