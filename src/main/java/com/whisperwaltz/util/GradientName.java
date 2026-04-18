package com.whisperwaltz.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/**
 * Reusable animated gradient name for items.
 *
 * Configure per-item via the Builder:
 *   GradientName.builder()
 *       .fromColor(r, g, b)
 *       .toColor(r, g, b)
 *       .speed(900.0)           // ms per radian — lower = faster
 *       .direction(Direction.LEFT_TO_RIGHT)
 *       .charSpread(0.48)       // phase offset between adjacent characters (radians)
 *       .build();
 */
public final class GradientName {

    public enum Direction { LEFT_TO_RIGHT, RIGHT_TO_LEFT }

    private final int[] fromRgb;
    private final int[] toRgb;
    private final double speed;
    private final Direction direction;
    private final double charSpread;

    private GradientName(int[] fromRgb, int[] toRgb, double speed, Direction direction, double charSpread) {
        this.fromRgb = fromRgb;
        this.toRgb = toRgb;
        this.speed = speed;
        this.direction = direction;
        this.charSpread = charSpread;
    }

    /** Builds the animated Component for the given text. Call this every frame from getName(). */
    public Component build(String text) {
        long time = System.currentTimeMillis();
        MutableComponent result = Component.empty();
        int len = text.length();

        for (int i = 0; i < len; i++) {
            int idx = (direction == Direction.RIGHT_TO_LEFT) ? (len - 1 - i) : i;
            double phase = time / speed + idx * charSpread;
            float t = (float) (Math.sin(phase) * 0.5 + 0.5); // 0..1

            int r = (int) (fromRgb[0] + t * (toRgb[0] - fromRgb[0]));
            int g = (int) (fromRgb[1] + t * (toRgb[1] - fromRgb[1]));
            int b = (int) (fromRgb[2] + t * (toRgb[2] - fromRgb[2]));

            result.append(
                    Component.literal(String.valueOf(text.charAt(i)))
                            .withStyle(Style.EMPTY
                                    .withColor(TextColor.fromRgb((r << 16) | (g << 8) | b))
                                    .withItalic(false))
            );
        }
        return result;
    }

    // -------------------------------------------------------------------------

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int[] fromRgb    = {85, 220, 255};
        private int[] toRgb      = {255, 255, 255};
        private double speed     = 900.0;
        private Direction dir    = Direction.LEFT_TO_RIGHT;
        private double charSpread = 0.48;

        /** Starting color of the gradient (RGB 0-255 each). */
        public Builder fromColor(int r, int g, int b) { fromRgb = new int[]{r, g, b}; return this; }

        /** Ending color of the gradient (RGB 0-255 each). */
        public Builder toColor(int r, int g, int b) { toRgb = new int[]{r, g, b}; return this; }

        /** Milliseconds per radian of the sine wave. Lower = faster animation. */
        public Builder speed(double ms) { speed = ms; return this; }

        /** Whether the color wave travels left-to-right or right-to-left. */
        public Builder direction(Direction d) { dir = d; return this; }

        /** Phase offset between adjacent characters in radians. Larger = wider wave spread. */
        public Builder charSpread(double radians) { charSpread = radians; return this; }

        public GradientName build() { return new GradientName(fromRgb, toRgb, speed, dir, charSpread); }
    }
}
