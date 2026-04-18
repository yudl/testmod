package com.whisperwaltz.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.whisperwaltz.entity.LeafProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LeafProjectileRenderer extends EntityRenderer<LeafProjectile> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/block/moss_block.png");

    // Light green tint
    private static final int R = 80, G = 210, B = 80, A = 255;

    public LeafProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LeafProjectile entity) {
        return TEXTURE;
    }

    @Override
    public void render(LeafProjectile entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.pushPose();
        poseStack.scale(0.25F, 0.25F, 0.25F);

        VertexConsumer buf = bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        PoseStack.Pose pose = poseStack.last();

        float s = 0.5F;
        // North (-Z)
        quad(buf, pose, -s,  s, -s,   s,  s, -s,   s, -s, -s,  -s, -s, -s,  0, 0,-1, packedLight);
        // South (+Z)
        quad(buf, pose,  s,  s,  s,  -s,  s,  s,  -s, -s,  s,   s, -s,  s,  0, 0, 1, packedLight);
        // West (-X)
        quad(buf, pose, -s,  s,  s,  -s,  s, -s,  -s, -s, -s,  -s, -s,  s, -1, 0, 0, packedLight);
        // East (+X)
        quad(buf, pose,  s,  s, -s,   s,  s,  s,   s, -s,  s,   s, -s, -s,  1, 0, 0, packedLight);
        // Top (+Y)
        quad(buf, pose, -s,  s, -s,  -s,  s,  s,   s,  s,  s,   s,  s, -s,  0, 1, 0, packedLight);
        // Bottom (-Y)
        quad(buf, pose, -s, -s,  s,  -s, -s, -s,   s, -s, -s,   s, -s,  s,  0,-1, 0, packedLight);

        poseStack.popPose();
    }

    private void quad(VertexConsumer buf, PoseStack.Pose pose,
                      float x1, float y1, float z1,
                      float x2, float y2, float z2,
                      float x3, float y3, float z3,
                      float x4, float y4, float z4,
                      float nx, float ny, float nz, int light) {
        vtx(buf, pose, x1, y1, z1, 0, 0, nx, ny, nz, light);
        vtx(buf, pose, x2, y2, z2, 1, 0, nx, ny, nz, light);
        vtx(buf, pose, x3, y3, z3, 1, 1, nx, ny, nz, light);
        vtx(buf, pose, x4, y4, z4, 0, 1, nx, ny, nz, light);
    }

    private void vtx(VertexConsumer buf, PoseStack.Pose pose,
                     float x, float y, float z, float u, float v,
                     float nx, float ny, float nz, int light) {
        buf.addVertex(pose, x, y, z)
           .setColor(R, G, B, A)
           .setUv(u, v)
           .setOverlay(OverlayTexture.NO_OVERLAY)
           .setLight(light)
           .setNormal(pose, nx, ny, nz);
    }
}
