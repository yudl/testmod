package com.whisperwaltz;

import com.whisperwaltz.effect.FreezeEffect;
import com.whisperwaltz.item.WhisperwaltzSwordItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

/**
 * Applies the Whisperwaltz freeze whenever the sword deals damage.
 * Using LivingDamageEvent.Post instead of postHurtEnemy because the
 * latter is skipped when the attack flag is false (e.g. misses or
 * certain mod interactions). This event fires on every real damage hit.
 */
@EventBusSubscriber(modid = WhisperwaltzMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class FreezeEventHandler {

    private static final int FREEZE_TICKS = 60; // 3 seconds

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        Entity sourceEntity = event.getSource().getEntity();
        if (!(sourceEntity instanceof LivingEntity attacker)) return;
        if (!(attacker.getMainHandItem().getItem() instanceof WhisperwaltzSwordItem)) return;

        LivingEntity target = event.getEntity();
        // forceAddEffect bypasses the "is new effect better?" check so freeze
        // is always refreshed to the full 3 seconds on every sword hit
        target.forceAddEffect(
                new MobEffectInstance(ModEffects.FREEZE, FREEZE_TICKS, 0, false, false, true),
                attacker
        );
    }
}
