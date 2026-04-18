package com.whisperwaltz.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.whisperwaltz.ModEffects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import com.whisperwaltz.WhisperwaltzMod;

/**
 * Renders a translucent ice-textured bounding box around any entity
 * that currently carries the Whisperwaltz freeze effect.
 */
@EventBusSubscriber(modid = WhisperwaltzMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class FreezeBoxRenderer {

    private static final ResourceLocation ICE_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/block/ice.png");

    // Slight outset so the cage doesn't clip into the entity model
    private static final float PADDING = 0.08f;

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        if (!entity.hasEffect(ModEffects.FREEZE)) return;

        renderFreezeBox(entity, event.getPoseStack(), event.getMultiBufferSource());
    }

    private static void renderFreezeBox(LivingEntity entity, PoseStack poseStack,
                                        MultiBufferSource buffers) {
        AABB box = entity.getBoundingBox();

        // Convert to pose-space (pose stack is already at entity's feet position)
        float minX = (float) (box.minX - entity.getX()) - PADDING;
        float minY = (float) (box.minY - entity.getY()) - PADDING;
        float minZ = (float) (box.minZ - entity.getZ()) - PADDING;
        float maxX = (float) (box.maxX - entity.getX()) + PADDING;
        float maxY = (float) (box.maxY - entity.getY()) + PADDING;
        float maxZ = (float) (box.maxZ - entity.getZ()) + PADDING;

        // 160/255 alpha → clearly visible but see-through
        int r = 200, g = 235, b = 255, a = 160;

        poseStack.pushPose();
        VertexConsumer buf = buffers.getBuffer(RenderType.entityTranslucentCull(ICE_TEXTURE));
        PoseStack.Pose pose = poseStack.last();

        // North  (-Z)
        quad(buf, pose, minX, maxY, minZ,  maxX, maxY, minZ,  maxX, minY, minZ,  minX, minY, minZ,  0, 0, -1, r, g, b, a);
        // South  (+Z)
        quad(buf, pose, maxX, maxY, maxZ,  minX, maxY, maxZ,  minX, minY, maxZ,  maxX, minY, maxZ,  0, 0,  1, r, g, b, a);
        // West   (-X)
        quad(buf, pose, minX, maxY, maxZ,  minX, maxY, minZ,  minX, minY, minZ,  minX, minY, maxZ, -1, 0,  0, r, g, b, a);
        // East   (+X)
        quad(buf, pose, maxX, maxY, minZ,  maxX, maxY, maxZ,  maxX, minY, maxZ,  maxX, minY, minZ,  1, 0,  0, r, g, b, a);
        // Top    (+Y)
        quad(buf, pose, minX, maxY, minZ,  minX, maxY, maxZ,  maxX, maxY, maxZ,  maxX, maxY, minZ,  0, 1,  0, r, g, b, a);
        // Bottom (-Y)
        quad(buf, pose, minX, minY, maxZ,  minX, minY, minZ,  maxX, minY, minZ,  maxX, minY, maxZ,  0, -1, 0, r, g, b, a);

        poseStack.popPose();
    }

    /** Emits one counter-clockwise quad (4 vertices) with tiled UVs from the ice texture. */
    private static void quad(VertexConsumer buf, PoseStack.Pose pose,
                              float x1, float y1, float z1,
                              float x2, float y2, float z2,
                              float x3, float y3, float z3,
                              float x4, float y4, float z4,
                              float nx, float ny, float nz,
                              int r, int g, int b, int a) {
        vtx(buf, pose, x1, y1, z1, 0, 0, nx, ny, nz, r, g, b, a);
        vtx(buf, pose, x2, y2, z2, 1, 0, nx, ny, nz, r, g, b, a);
        vtx(buf, pose, x3, y3, z3, 1, 1, nx, ny, nz, r, g, b, a);
        vtx(buf, pose, x4, y4, z4, 0, 1, nx, ny, nz, r, g, b, a);
    }

    private static void vtx(VertexConsumer buf, PoseStack.Pose pose,
                             float x, float y, float z,
                             float u, float v,
                             float nx, float ny, float nz,
                             int r, int g, int b, int a) {
        buf.addVertex(pose, x, y, z)
           .setColor(r, g, b, a)
           .setUv(u, v)
           .setOverlay(OverlayTexture.NO_OVERLAY)
           .setLight(LightTexture.FULL_BRIGHT)
           .setNormal(pose, nx, ny, nz);
    }
}
