package com.whisperwaltz;

import com.whisperwaltz.item.LeafWeaverItem;
import com.whisperwaltz.item.WhisperwaltzSwordItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, WhisperwaltzMod.MOD_ID);

    public static final DeferredHolder<Item, WhisperwaltzSwordItem> WHISPERWALTZ_SWORD =
            ITEMS.register("whisperwaltz_sword", () -> new WhisperwaltzSwordItem(new Item.Properties()));

    public static final DeferredHolder<Item, LeafWeaverItem> LEAF_WEAVER =
            ITEMS.register("leaf_weaver", () -> new LeafWeaverItem(new Item.Properties()));
}
