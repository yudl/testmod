package com.whisperwaltz;

import com.whisperwaltz.util.GradientName;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WhisperwaltzMod.MOD_ID);

    // Tab title: subtle purple → cornflower blue static gradient
    private static final int[] TITLE_FROM = {160, 32, 240};
    private static final int[] TITLE_TO   = {64,  160, 255};

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WHISPERWALTZ_TAB =
            CREATIVE_MODE_TABS.register("whisperwaltz_tab", () ->
                    CreativeModeTab.builder()
                            .title(GradientName.buildLinear("Custom Mod", TITLE_FROM, TITLE_TO))
                            .icon(() -> ModItems.WHISPERWALTZ_SWORD.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.WHISPERWALTZ_SWORD.get());
                            })
                            .build()
            );
}
