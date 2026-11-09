package com.temoria.disc;

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

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TemoriaDiscMod.MOD_ID);

    public static final List<DeferredItem<Item>> ALL_DISCS = new ArrayList<>();
    public static final List<DeferredItem<Item>> VINTAGE_STORY_DISCS = new ArrayList<>();
    public static final List<DeferredItem<Item>> TEMORIA_DISCS = new ArrayList<>();

    private static DeferredItem<Item> registerDisc(String songId, String album) {
        ResourceKey<JukeboxSong> songKey = ResourceKey.create(
                Registries.JUKEBOX_SONG,
                ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, songId)
        );

        DeferredItem<Item> item = ITEMS.registerItem(
                "music_disc_" + songId,
                Item::new,
                new Item.Properties().stacksTo(1).rarity(album.equals("temoria") ? Rarity.EPIC : Rarity.RARE).jukeboxPlayable(songKey)
        );
        ALL_DISCS.add(item);
        if ("temoria".equals(album)) {
            TEMORIA_DISCS.add(item);
        } else {
            VINTAGE_STORY_DISCS.add(item);
        }
        return item;
    }

    public static final DeferredItem<Item> DISC_ADVENTURING = registerDisc("adventuring", "vintage_story");
    public static final DeferredItem<Item> DISC_ARCTICWINDS = registerDisc("arcticwinds", "vintage_story");
    public static final DeferredItem<Item> DISC_BUILDING = registerDisc("building", "vintage_story");
    public static final DeferredItem<Item> DISC_CHILLY_VILLAGE = registerDisc("chilly_village", "vintage_story");
    public static final DeferredItem<Item> DISC_CREATING = registerDisc("creating", "vintage_story");
    public static final DeferredItem<Item> DISC_CULTUREDTAVERN = registerDisc("culturedtavern", "vintage_story");
    public static final DeferredItem<Item> DISC_DAYLIGHT = registerDisc("daylight", "vintage_story");
    public static final DeferredItem<Item> DISC_DEVASTATION_BASELAYER = registerDisc("devastation_baselayer", "vintage_story");
    public static final DeferredItem<Item> DISC_DEVASTATION_BELLSANDRUST = registerDisc("devastation_bellsandrust", "vintage_story");
    public static final DeferredItem<Item> DISC_DEVASTATION_ERELFIGHT = registerDisc("devastation_erelfight", "vintage_story");
    public static final DeferredItem<Item> DISC_DEVASTATION_ERELHARRASS = registerDisc("devastation_erelharrass", "vintage_story");
    public static final DeferredItem<Item> DISC_FALL = registerDisc("fall", "vintage_story");
    public static final DeferredItem<Item> DISC_FALL_O_CROFT = registerDisc("fall_o_croft", "vintage_story");
    public static final DeferredItem<Item> DISC_GROOVE = registerDisc("groove", "vintage_story");
    public static final DeferredItem<Item> DISC_HALLOWCROFT = registerDisc("hallowcroft", "vintage_story");
    public static final DeferredItem<Item> DISC_HEARTBEAT_CREATE = registerDisc("heartbeat_create", "vintage_story");
    public static final DeferredItem<Item> DISC_HEARTBEAT_SURVIVE = registerDisc("heartbeat_survive", "vintage_story");
    public static final DeferredItem<Item> DISC_LATVIA = registerDisc("latvia", "vintage_story");
    public static final DeferredItem<Item> DISC_MIDNIGHT = registerDisc("midnight", "vintage_story");
    public static final DeferredItem<Item> DISC_MIRROR = registerDisc("mirror", "vintage_story");
    public static final DeferredItem<Item> DISC_NADIYA_FALL = registerDisc("nadiya_fall", "vintage_story");
    public static final DeferredItem<Item> DISC_NADIYA_SPRING = registerDisc("nadiya_spring", "vintage_story");
    public static final DeferredItem<Item> DISC_NADIYA_SUMMER = registerDisc("nadiya_summer", "vintage_story");
    public static final DeferredItem<Item> DISC_NADIYA_WINTER = registerDisc("nadiya_winter", "vintage_story");
    public static final DeferredItem<Item> DISC_NIGHT_TO_DAY = registerDisc("night_to_day", "vintage_story");
    public static final DeferredItem<Item> DISC_NOSTALGIC = registerDisc("nostalgic", "vintage_story");
    public static final DeferredItem<Item> DISC_PEACEFUL_VILLAGE = registerDisc("peaceful_village", "vintage_story");
    public static final DeferredItem<Item> DISC_QUIRKYTAVERN = registerDisc("quirkytavern", "vintage_story");
    public static final DeferredItem<Item> DISC_RADIANCEANDRUST = registerDisc("radianceandrust", "vintage_story");
    public static final DeferredItem<Item> DISC_ROOTS = registerDisc("roots", "vintage_story");
    public static final DeferredItem<Item> DISC_SAFETY_OF_A_WARM_FIRE = registerDisc("safety_of_a_warm_fire", "vintage_story");
    public static final DeferredItem<Item> DISC_SETTING_SUN = registerDisc("setting_sun", "vintage_story");
    public static final DeferredItem<Item> DISC_SPRING = registerDisc("spring", "vintage_story");
    public static final DeferredItem<Item> DISC_SUBSTITUTE_EPITAPH = registerDisc("substitute_epitaph", "vintage_story");
    public static final DeferredItem<Item> DISC_SUMMER_DAY = registerDisc("summer_day", "vintage_story");
    public static final DeferredItem<Item> DISC_SUMMER_STROLL = registerDisc("summer_stroll", "vintage_story");
    public static final DeferredItem<Item> DISC_SUNNY_VILLAGE = registerDisc("sunny_village", "vintage_story");
    public static final DeferredItem<Item> DISC_SUNNY_VILLAGE_CREATE = registerDisc("sunny_village_create", "vintage_story");
    public static final DeferredItem<Item> DISC_SUNNY_VILLAGE_SURVIVE = registerDisc("sunny_village_survive", "vintage_story");
    public static final DeferredItem<Item> DISC_THE_LAZARET = registerDisc("the_lazaret", "vintage_story");
    public static final DeferredItem<Item> DISC_THE_SERAPHS = registerDisc("the_seraphs", "vintage_story");
    public static final DeferredItem<Item> DISC_THEINVENTION = registerDisc("theinvention", "vintage_story");
    public static final DeferredItem<Item> DISC_THERESONANCEARCHIVES_BARREN = registerDisc("theresonancearchives_barren", "vintage_story");
    public static final DeferredItem<Item> DISC_THERESONANCEARCHIVES_EIDOLON = registerDisc("theresonancearchives_eidolon", "vintage_story");
    public static final DeferredItem<Item> DISC_THERESONANCEARCHIVES_LIBRARY = registerDisc("theresonancearchives_library", "vintage_story");
    public static final DeferredItem<Item> DISC_THROUGH_THE_GRASS_CREATE = registerDisc("through_the_grass_create", "vintage_story");
    public static final DeferredItem<Item> DISC_THROUGH_THE_GRASS_SURVIVE = registerDisc("through_the_grass_survive", "vintage_story");
    public static final DeferredItem<Item> DISC_TO_DAWN = registerDisc("to_dawn", "vintage_story");
    public static final DeferredItem<Item> DISC_TOBIAS = registerDisc("tobias", "vintage_story");
    public static final DeferredItem<Item> DISC_VINTAGESTORY = registerDisc("vintagestory", "vintage_story");
    public static final DeferredItem<Item> DISC_WINDY_VILLAGE = registerDisc("windy_village", "vintage_story");
    public static final DeferredItem<Item> DISC_WINTER = registerDisc("winter", "vintage_story");
    public static final DeferredItem<Item> DISC_NOAH_TES_BAN = registerDisc("noah_tes_ban", "temoria");
}
