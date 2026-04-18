package com.whisperwaltz.item;

import com.whisperwaltz.util.GradientName;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
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
}
