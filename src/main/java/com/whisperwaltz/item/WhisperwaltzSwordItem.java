package com.whisperwaltz.item;

import com.whisperwaltz.ModEffects;
import com.whisperwaltz.util.GradientName;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.loading.FMLEnvironment;

public class WhisperwaltzSwordItem extends SwordItem {

    // Total attack damage = 12
    // Base player damage (1) + DIAMOND tier bonus (3) + our bonus (8) = 12
    private static final int DAMAGE_BONUS = 8;

    // Gradient: light blue → white, rippling left-to-right
    private static final GradientName GRADIENT = GradientName.builder()
            .fromColor(85, 220, 255)
            .toColor(255, 255, 255)
            .speed(900.0)
            .direction(GradientName.Direction.LEFT_TO_RIGHT)
            .charSpread(0.48)
            .build();

    // 1.5 seconds = 30 ticks
    private static final int FREEZE_TICKS = 30;

    public WhisperwaltzSwordItem(Item.Properties properties) {
        super(Tiers.DIAMOND, properties.attributes(
                SwordItem.createAttributes(Tiers.DIAMOND, DAMAGE_BONUS, -2.4F)
        ));
    }

    @Override
    public Component getName(ItemStack stack) {
        if (!FMLEnvironment.dist.isClient()) {
            return super.getName(stack);
        }
        return GRADIENT.build("Whisperwaltz");
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker); // handles durability damage
        if (attacker.level().isClientSide()) return;

        // Freeze marker effect — drives the ice-box renderer and maintains frozen ticks
        target.addEffect(new MobEffectInstance(ModEffects.FREEZE, FREEZE_TICKS, 0, false, false, true));
        // Slowness VI (movement speed * (1 - 0.15*7) ≈ 0) — stops movement completely
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, FREEZE_TICKS, 6, false, false, false));
    }
}
