package com.whisperwaltz.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.fml.loading.FMLEnvironment;

public class WhisperwaltzSwordItem extends SwordItem {

    // Total attack damage = 12
    // Base player damage (1) + DIAMOND tier bonus (3) + our bonus (8) = 12
    private static final int DAMAGE_BONUS = 8;

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
        return buildAnimatedName();
    }

    private static Component buildAnimatedName() {
        String text = "Whisperwaltz";
        long time = System.currentTimeMillis();
        MutableComponent result = Component.empty();

        for (int i = 0; i < text.length(); i++) {
            // Each character is offset in phase, creating a ripple wave across the name
            double phase = time / 900.0 + i * 0.48;
            float t = (float) (Math.sin(phase) * 0.5 + 0.5); // smooth 0..1

            // Interpolate: light blue (85, 220, 255) → white (255, 255, 255)
            int r = (int) (85 + t * 170);
            int g = (int) (220 + t * 35);
            int b = 255;
            int rgb = (r << 16) | (g << 8) | b;

            result.append(
                    Component.literal(String.valueOf(text.charAt(i)))
                            .withStyle(Style.EMPTY
                                    .withColor(TextColor.fromRgb(rgb))
                                    .withItalic(false))
            );
        }

        return result;
    }
}
