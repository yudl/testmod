package com.whisperwaltz.entity;

import com.whisperwaltz.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class LeafProjectile extends ThrowableProjectile {

    private static final float DAMAGE = 10.0F;

    public LeafProjectile(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public LeafProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.LEAF_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if (target instanceof LivingEntity living) {
            living.hurt(level().damageSources().thrown(this, getOwner()), DAMAGE);
        }
        if (!level().isClientSide) {
            spreadMoss(BlockPos.containing(target.getX(), target.getY() - 0.5, target.getZ()));
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!level().isClientSide) {
            spreadMoss(result.getBlockPos());
        }
        this.discard();
    }

    private void spreadMoss(BlockPos center) {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        int radius = 2;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius + 0.5) continue;
                BlockPos base = center.offset(dx, 0, dz);
                BlockPos above = base.above();
                BlockState baseState = serverLevel.getBlockState(base);
                BlockState aboveState = serverLevel.getBlockState(above);
                if (!baseState.isAir() && aboveState.isAir()) {
                    serverLevel.setBlockAndUpdate(above, Blocks.MOSS_CARPET.defaultBlockState());
                }
            }
        }
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        // No custom synced data needed
    }
}
