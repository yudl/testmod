package com.whisperwaltz;

import com.whisperwaltz.entity.LeafProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, WhisperwaltzMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<LeafProjectile>> LEAF_PROJECTILE =
            ENTITY_TYPES.register("leaf_projectile", () ->
                    EntityType.Builder.<LeafProjectile>of(LeafProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .build(WhisperwaltzMod.MOD_ID + ":leaf_projectile"));
}
