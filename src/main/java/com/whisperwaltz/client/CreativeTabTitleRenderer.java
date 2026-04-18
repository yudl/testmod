package com.whisperwaltz.client;

import com.whisperwaltz.ModCreativeTabs;
import com.whisperwaltz.util.GradientName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import com.whisperwaltz.WhisperwaltzMod;

import java.lang.reflect.Field;

/**
 * Draws an animated gradient title for the "Custom Mod" creative tab.
 * Uses ContainerScreenEvent.Render.Foreground, which fires inside the
 * pose.translate(leftPos, topPos) transform — so (8, 6) matches the
 * vanilla tab title position exactly.
 */
@EventBusSubscriber(modid = WhisperwaltzMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class CreativeTabTitleRenderer {

    private static final GradientName TAB_GRADIENT = GradientName.builder()
            .fromColor(160, 32, 240)
            .toColor(64, 160, 255)
            .speed(800.0)
            .charSpread(0.35)
            .build();

    // Cache the private-static selectedTab field accessor
    private static final Field SELECTED_TAB_FIELD;

    static {
        Field f = null;
        try {
            f = CreativeModeInventoryScreen.class.getDeclaredField("selectedTab");
            f.setAccessible(true);
        } catch (Exception ignored) {}
        SELECTED_TAB_FIELD = f;
    }

    @SubscribeEvent
    public static void onContainerForeground(ContainerScreenEvent.Render.Foreground event) {
        if (!(event.getContainerScreen() instanceof CreativeModeInventoryScreen)) return;
        if (SELECTED_TAB_FIELD == null) return;

        try {
            CreativeModeTab tab = (CreativeModeTab) SELECTED_TAB_FIELD.get(null);
            if (tab != ModCreativeTabs.WHISPERWALTZ_TAB.get()) return;
        } catch (Exception ignored) {
            return;
        }

        // Foreground event fires inside pose.translate(leftPos, topPos),
        // so (8, 6) is exactly where the vanilla tab title sits.
        Font font = Minecraft.getInstance().font;
        Component animated = TAB_GRADIENT.build("Custom Mod");
        event.getGuiGraphics().drawString(font, animated, 8, 6, 0x404040, false);
    }
}
