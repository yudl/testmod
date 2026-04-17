package com.whisperwaltz;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(WhisperwaltzMod.MOD_ID)
public class WhisperwaltzMod {

    public static final String MOD_ID = "whisperwaltz";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WhisperwaltzMod(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        LOGGER.info("Whisperwaltz mod loaded.");
    }
}
