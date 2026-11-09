package com.temoria.disc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TemoriaDiscMod.MOD_ID);

    public static final DeferredItem<Item> DISC_NOAH_TES_BAN = ITEMS.registerItem(
            "music_disc_noah_tes_ban",
            Item::new,
            new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(
                    ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, "noah_tes_ban"))
            )
    );
}
