package com.whisperwaltz.rarity;

import com.whisperwaltz.util.GradientName;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/**
 * All weapon/item rarities. Each rarity drives the item's name gradient automatically.
 * Item classes declare a rarity; no gradient code lives in the item class itself.
 */
public enum ModRarity {

    // Animated: white → gold → white shimmer
    LEGENDARY(GradientName.builder()
            .colorStops(new int[][]{{255, 255, 255}, {255, 215, 0}, {255, 255, 255}})
            .speed(700.0).charSpread(0.45).build()),

    // Animated: orange → red
    ELEMENTAL_FIRE(GradientName.builder()
            .colorStops(new int[][]{{255, 150, 0}, {220, 20, 20}})
            .speed(800.0).charSpread(0.40).build()),

    // Animated: light blue → white (same as original Whisperwaltz)
    ELEMENTAL_ICE(GradientName.builder()
            .fromColor(85, 220, 255).toColor(255, 255, 255)
            .speed(900.0).charSpread(0.48).build()),

    // Animated: dark green → green → light green
    ELEMENTAL_EARTH(GradientName.builder()
            .colorStops(new int[][]{{0, 80, 0}, {34, 139, 34}, {144, 238, 144}})
            .speed(1000.0).charSpread(0.42).build()),

    // Static solid colors
    EPIC   (null, 0xFF69B4),   // hot pink
    RARE   (null, 0x87CEEB),   // sky blue
    COMMON (null, 0xFFFFFF);   // white

    private final GradientName gradient;
    private final int           staticColor;

    ModRarity(GradientName gradient) {
        this.gradient    = gradient;
        this.staticColor = 0xFFFFFF;
    }

    ModRarity(GradientName gradient, int staticColor) {
        this.gradient    = gradient;
        this.staticColor = staticColor;
    }

    /**
     * Returns the styled display Component for the given name.
     * Must be called on the client for animated rarities.
     */
    public Component apply(String text) {
        if (gradient != null) {
            return gradient.build(text);
        }
        return Component.literal(text)
                .withStyle(Style.EMPTY
                        .withColor(TextColor.fromRgb(staticColor))
                        .withItalic(false));
    }
}
