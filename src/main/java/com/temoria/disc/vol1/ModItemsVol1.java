package com.temoria.disc.vol1;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModItemsVol1 {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("temoria_disc");

    public static final List<DeferredItem<Item>> DISCS = new ArrayList<>();

    private static DeferredItem<Item> registerDisc(String songId) {
        ResourceKey<JukeboxSong> songKey = ResourceKey.create(
                Registries.JUKEBOX_SONG,
                ResourceLocation.fromNamespaceAndPath("temoria_disc", songId)
        );

        DeferredItem<Item> item = ITEMS.registerItem(
                "music_disc_" + songId,
                Item::new,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(songKey)
        );
        DISCS.add(item);
        return item;
    }

    public static final DeferredItem<Item> DISC_ADVENTURING = registerDisc("adventuring");
    public static final DeferredItem<Item> DISC_ARCTICWINDS = registerDisc("arcticwinds");
    public static final DeferredItem<Item> DISC_BUILDING = registerDisc("building");
    public static final DeferredItem<Item> DISC_CHILLY_VILLAGE = registerDisc("chilly_village");
    public static final DeferredItem<Item> DISC_CREATING = registerDisc("creating");
    public static final DeferredItem<Item> DISC_CULTUREDTAVERN = registerDisc("culturedtavern");
    public static final DeferredItem<Item> DISC_DAYLIGHT = registerDisc("daylight");
    public static final DeferredItem<Item> DISC_DEVASTATION_BASELAYER = registerDisc("devastation_baselayer");
    public static final DeferredItem<Item> DISC_DEVASTATION_BELLSANDRUST = registerDisc("devastation_bellsandrust");
    public static final DeferredItem<Item> DISC_DEVASTATION_ERELFIGHT = registerDisc("devastation_erelfight");
    public static final DeferredItem<Item> DISC_DEVASTATION_ERELHARRASS = registerDisc("devastation_erelharrass");
    public static final DeferredItem<Item> DISC_FALL = registerDisc("fall");
    public static final DeferredItem<Item> DISC_FALL_O_CROFT = registerDisc("fall_o_croft");
    public static final DeferredItem<Item> DISC_GROOVE = registerDisc("groove");
    public static final DeferredItem<Item> DISC_HALLOWCROFT = registerDisc("hallowcroft");
    public static final DeferredItem<Item> DISC_HEARTBEAT_CREATE = registerDisc("heartbeat_create");
    public static final DeferredItem<Item> DISC_HEARTBEAT_SURVIVE = registerDisc("heartbeat_survive");
    public static final DeferredItem<Item> DISC_LATVIA = registerDisc("latvia");
    public static final DeferredItem<Item> DISC_MIDNIGHT = registerDisc("midnight");
    public static final DeferredItem<Item> DISC_MIRROR = registerDisc("mirror");
    public static final DeferredItem<Item> DISC_NADIYA_FALL = registerDisc("nadiya_fall");
    public static final DeferredItem<Item> DISC_NADIYA_SPRING = registerDisc("nadiya_spring");
    public static final DeferredItem<Item> DISC_NADIYA_SUMMER = registerDisc("nadiya_summer");
    public static final DeferredItem<Item> DISC_NADIYA_WINTER = registerDisc("nadiya_winter");
    public static final DeferredItem<Item> DISC_NIGHT_TO_DAY = registerDisc("night_to_day");
    public static final DeferredItem<Item> DISC_NOSTALGIC = registerDisc("nostalgic");
}
