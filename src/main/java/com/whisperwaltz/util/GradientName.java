package com.whisperwaltz.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/**
 * Animated per-character gradient name for items and UI labels.
 *
 * Two-color usage:
 *   GradientName.builder().fromColor(r,g,b).toColor(r,g,b).speed(900).charSpread(0.48).build()
 *
 * Multi-stop usage:
 *   GradientName.builder().colorStops(new int[][]{{r,g,b},{r,g,b},{r,g,b}}).build()
 */
public final class GradientName {

    public enum Direction { LEFT_TO_RIGHT, RIGHT_TO_LEFT }

    private final int[][] colorStops;
    private final double speed;
    private final Direction direction;
    private final double charSpread;

    // Per-instance 50 ms cache — prevents hotbar flicker from new Component objects every frame
    private String   cachedText;
    private Component cachedComponent;
    private long     cacheTimestamp = -1;
    private static final long CACHE_INTERVAL_MS = 50;

    private GradientName(int[][] colorStops, double speed, Direction direction, double charSpread) {
        this.colorStops = colorStops;
        this.speed = speed;
        this.direction = direction;
        this.charSpread = charSpread;
    }

    /** Builds the animated gradient Component. Must be called on the client thread. */
    public Component build(String text) {
        long now = System.currentTimeMillis();
        if (cachedComponent != null && text.equals(cachedText) && now - cacheTimestamp < CACHE_INTERVAL_MS) {
            return cachedComponent;
        }

        MutableComponent result = Component.empty();
        int len = text.length();

        for (int i = 0; i < len; i++) {
            int idx = (direction == Direction.RIGHT_TO_LEFT) ? (len - 1 - i) : i;
            double phase = (double) now / speed + idx * charSpread;
            float t = (float) (Math.sin(phase) * 0.5 + 0.5);

            int[] rgb = interpolateStops(t);
            result.append(
                    Component.literal(String.valueOf(text.charAt(i)))
                            .withStyle(Style.EMPTY
                                    .withColor(TextColor.fromRgb((rgb[0] << 16) | (rgb[1] << 8) | rgb[2]))
                                    .withItalic(false))
            );
        }

        cachedText = text;
        cachedComponent = result;
        cacheTimestamp = now;
        return result;
    }

    private int[] interpolateStops(float t) {
        int n = colorStops.length;
        if (n == 1) return colorStops[0].clone();

        float scaled  = t * (n - 1);
        int   seg     = Math.min((int) scaled, n - 2);
        float segT    = scaled - seg;

        int[] a = colorStops[seg];
        int[] b = colorStops[seg + 1];
        return new int[]{
            (int)(a[0] + segT * (b[0] - a[0])),
            (int)(a[1] + segT * (b[1] - a[1])),
            (int)(a[2] + segT * (b[2] - a[2]))
        };
    }

    /**
     * Builds a static, position-based linear gradient Component (no animation).
     * Safe to call on any side. Used for UI labels like creative tab titles.
     */
    public static Component buildLinear(String text, int[] from, int[] to) {
        MutableComponent result = Component.empty();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            float t = (len == 1) ? 0.5f : (float) i / (len - 1);
            int r = (int)(from[0] + t * (to[0] - from[0]));
            int g = (int)(from[1] + t * (to[1] - from[1]));
            int b = (int)(from[2] + t * (to[2] - from[2]));
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

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private int[]   fromRgb       = {85, 220, 255};
        private int[]   toRgb         = {255, 255, 255};
        private int[][] stopsOverride = null;
        private double  speed         = 900.0;
        private Direction dir         = Direction.LEFT_TO_RIGHT;
        private double  charSpread    = 0.48;

        public Builder fromColor(int r, int g, int b) { fromRgb = new int[]{r, g, b}; return this; }
        public Builder toColor(int r, int g, int b)   { toRgb   = new int[]{r, g, b}; return this; }

        /** Explicit multi-stop array; overrides fromColor/toColor. */
        public Builder colorStops(int[][] stops) { stopsOverride = stops; return this; }

        public Builder speed(double ms)           { speed      = ms;  return this; }
        public Builder direction(Direction d)     { dir        = d;   return this; }
        public Builder charSpread(double radians) { charSpread = radians; return this; }

        public GradientName build() {
            int[][] stops = (stopsOverride != null) ? stopsOverride : new int[][]{fromRgb, toRgb};
            return new GradientName(stops, speed, dir, charSpread);
        }
    }
}
