package com.whisperwaltz.item;

import com.whisperwaltz.ModEntities;
import com.whisperwaltz.WhisperwaltzMod;
import com.whisperwaltz.entity.LeafProjectile;
import com.whisperwaltz.rarity.ModRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;

public class LeafWeaverItem extends Item {

    private static final ResourceLocation DAMAGE_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(WhisperwaltzMod.MOD_ID, "leaf_weaver_damage");

    public LeafWeaverItem(Item.Properties properties) {
        super(properties.attributes(
                ItemAttributeModifiers.builder()
                        // Subtract 1 from player base damage → 0 effective melee damage
                        .add(Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(DAMAGE_MODIFIER_ID, -1.0,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .build()
        ));
    }

    @Override
    public Component getName(ItemStack stack) {
        if (!FMLEnvironment.dist.isClient()) {
            return super.getName(stack);
        }
        return ModRarity.ELEMENTAL_EARTH.apply("Leaf Weaver");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            LeafProjectile projectile = new LeafProjectile(level, player);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 0.0F);
            level.addFreshEntity(projectile);
        }
        player.getCooldowns().addCooldown(this, 15);
        return InteractionResultHolder.success(stack);
    }
}
