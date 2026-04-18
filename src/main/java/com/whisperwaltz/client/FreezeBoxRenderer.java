package com.whisperwaltz.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.whisperwaltz.WhisperwaltzMod;
import com.whisperwaltz.effect.FreezeEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.minecraft.client.renderer.LightTexture;

/**
 * Renders a translucent ice-textured bounding box around frozen entities.
 * Uses RenderLevelStageEvent (AFTER_TRANSLUCENT_BLOCKS) with an explicit
 * endBatch() call so the buffer is guaranteed to flush and appear on screen.
 */
@EventBusSubscriber(modid = WhisperwaltzMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class FreezeBoxRenderer {

    private static final net.minecraft.resources.ResourceLocation ICE_TEXTURE =
            net.minecraft.resources.ResourceLocation.withDefaultNamespace("textures/block/ice.png");

    private static final float PADDING = 0.08f;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Vec3 camPos = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        float pt = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        boolean anyRendered = false;

        for (LivingEntity entity : mc.level.getEntitiesOfClass(
                LivingEntity.class, mc.player.getBoundingBox().inflate(64))) {
            boolean frozen = entity.getActiveEffects().stream()
                    .anyMatch(i -> i.getEffect().value() instanceof FreezeEffect);
            if (!frozen) continue;

            // Use lerped position for smooth rendering (entities can still be knocked back)
            double ex = Mth.lerp(pt, entity.xOld, entity.getX());
            double ey = Mth.lerp(pt, entity.yOld, entity.getY());
            double ez = Mth.lerp(pt, entity.zOld, entity.getZ());

            poseStack.pushPose();
            poseStack.translate(ex - camPos.x, ey - camPos.y, ez - camPos.z);
            renderFreezeBox(entity, poseStack, bufferSource);
            poseStack.popPose();
            anyRendered = true;
        }

        if (anyRendered) {
            bufferSource.endBatch(RenderType.entityTranslucent(ICE_TEXTURE));
        }
    }

    private static void renderFreezeBox(LivingEntity entity, PoseStack poseStack,
                                        MultiBufferSource buffers) {
        AABB box = entity.getBoundingBox();

        float minX = (float) (box.minX - entity.getX()) - PADDING;
        float minY = (float) (box.minY - entity.getY()) - PADDING;
        float minZ = (float) (box.minZ - entity.getZ()) - PADDING;
        float maxX = (float) (box.maxX - entity.getX()) + PADDING;
        float maxY = (float) (box.maxY - entity.getY()) + PADDING;
        float maxZ = (float) (box.maxZ - entity.getZ()) + PADDING;

        int r = 200, g = 235, b = 255, a = 160;

        VertexConsumer buf = buffers.getBuffer(RenderType.entityTranslucent(ICE_TEXTURE));
        PoseStack.Pose pose = poseStack.last();

        quad(buf, pose, minX, maxY, minZ,  maxX, maxY, minZ,  maxX, minY, minZ,  minX, minY, minZ,  0, 0,-1, r, g, b, a);
        quad(buf, pose, maxX, maxY, maxZ,  minX, maxY, maxZ,  minX, minY, maxZ,  maxX, minY, maxZ,  0, 0, 1, r, g, b, a);
        quad(buf, pose, minX, maxY, maxZ,  minX, maxY, minZ,  minX, minY, minZ,  minX, minY, maxZ, -1, 0, 0, r, g, b, a);
        quad(buf, pose, maxX, maxY, minZ,  maxX, maxY, maxZ,  maxX, minY, maxZ,  maxX, minY, minZ,  1, 0, 0, r, g, b, a);
        quad(buf, pose, minX, maxY, minZ,  minX, maxY, maxZ,  maxX, maxY, maxZ,  maxX, maxY, minZ,  0, 1, 0, r, g, b, a);
        quad(buf, pose, minX, minY, maxZ,  minX, minY, minZ,  maxX, minY, minZ,  maxX, minY, maxZ,  0,-1, 0, r, g, b, a);
    }

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
                             float x, float y, float z, float u, float v,
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
