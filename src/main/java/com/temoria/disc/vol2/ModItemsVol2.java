package com.temoria.disc.vol2;

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

public class ModItemsVol2 {
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

    public static final DeferredItem<Item> DISC_PEACEFUL_VILLAGE = registerDisc("peaceful_village");
    public static final DeferredItem<Item> DISC_QUIRKYTAVERN = registerDisc("quirkytavern");
    public static final DeferredItem<Item> DISC_RADIANCEANDRUST = registerDisc("radianceandrust");
    public static final DeferredItem<Item> DISC_ROOTS = registerDisc("roots");
    public static final DeferredItem<Item> DISC_SAFETY_OF_A_WARM_FIRE = registerDisc("safety_of_a_warm_fire");
    public static final DeferredItem<Item> DISC_SETTING_SUN = registerDisc("setting_sun");
    public static final DeferredItem<Item> DISC_SPRING = registerDisc("spring");
    public static final DeferredItem<Item> DISC_SUBSTITUTE_EPITAPH = registerDisc("substitute_epitaph");
    public static final DeferredItem<Item> DISC_SUMMER_DAY = registerDisc("summer_day");
    public static final DeferredItem<Item> DISC_SUMMER_STROLL = registerDisc("summer_stroll");
    public static final DeferredItem<Item> DISC_SUNNY_VILLAGE = registerDisc("sunny_village");
    public static final DeferredItem<Item> DISC_SUNNY_VILLAGE_CREATE = registerDisc("sunny_village_create");
    public static final DeferredItem<Item> DISC_SUNNY_VILLAGE_SURVIVE = registerDisc("sunny_village_survive");
    public static final DeferredItem<Item> DISC_THE_LAZARET = registerDisc("the_lazaret");
    public static final DeferredItem<Item> DISC_THE_SERAPHS = registerDisc("the_seraphs");
    public static final DeferredItem<Item> DISC_THEINVENTION = registerDisc("theinvention");
    public static final DeferredItem<Item> DISC_THERESONANCEARCHIVES_BARREN = registerDisc("theresonancearchives_barren");
    public static final DeferredItem<Item> DISC_THERESONANCEARCHIVES_EIDOLON = registerDisc("theresonancearchives_eidolon");
    public static final DeferredItem<Item> DISC_THERESONANCEARCHIVES_LIBRARY = registerDisc("theresonancearchives_library");
    public static final DeferredItem<Item> DISC_THROUGH_THE_GRASS_CREATE = registerDisc("through_the_grass_create");
    public static final DeferredItem<Item> DISC_THROUGH_THE_GRASS_SURVIVE = registerDisc("through_the_grass_survive");
    public static final DeferredItem<Item> DISC_TO_DAWN = registerDisc("to_dawn");
    public static final DeferredItem<Item> DISC_TOBIAS = registerDisc("tobias");
    public static final DeferredItem<Item> DISC_VINTAGESTORY = registerDisc("vintagestory");
    public static final DeferredItem<Item> DISC_WINDY_VILLAGE = registerDisc("windy_village");
    public static final DeferredItem<Item> DISC_WINTER = registerDisc("winter");
}
