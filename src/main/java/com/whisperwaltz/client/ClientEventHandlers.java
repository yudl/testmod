package com.whisperwaltz.client;

import com.whisperwaltz.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.whisperwaltz.WhisperwaltzMod;

@EventBusSubscriber(modid = WhisperwaltzMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandlers {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.LEAF_PROJECTILE.get(), LeafProjectileRenderer::new);
    }
}
