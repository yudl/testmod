package com.whisperwaltz;

import com.whisperwaltz.effect.FreezeEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, WhisperwaltzMod.MOD_ID);

    public static final DeferredHolder<MobEffect, FreezeEffect> FREEZE =
            MOB_EFFECTS.register("freeze", FreezeEffect::new);
}
