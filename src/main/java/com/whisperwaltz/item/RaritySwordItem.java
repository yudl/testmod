package com.whisperwaltz.item;

import com.whisperwaltz.rarity.ModRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * Base class for all Whisperwaltz weapons. Subclasses declare a rarity and display name;
 * the gradient is applied automatically without any per-item gradient code.
 */
public abstract class RaritySwordItem extends SwordItem {

    private final ModRarity rarity;
    private final String    displayName;

    public RaritySwordItem(Tier tier, int damageBonus, float attackSpeed,
                           ModRarity rarity, String displayName, Item.Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, damageBonus, attackSpeed)));
        this.rarity      = rarity;
        this.displayName = displayName;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (!FMLEnvironment.dist.isClient()) {
            return super.getName(stack);
        }
        return rarity.apply(displayName);
    }
}
