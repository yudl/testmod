package com.whisperwaltz.item;

import com.whisperwaltz.rarity.ModRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

public class WhisperwaltzSwordItem extends RaritySwordItem {

    // Total attack damage = 12
    // Base player damage (1) + DIAMOND tier bonus (3) + our bonus (8) = 12
    private static final int DAMAGE_BONUS = 8;

    public WhisperwaltzSwordItem(Item.Properties properties) {
        super(Tiers.DIAMOND, DAMAGE_BONUS, -2.4F, ModRarity.ELEMENTAL_ICE, "Whisperwaltz", properties);
    }
}
