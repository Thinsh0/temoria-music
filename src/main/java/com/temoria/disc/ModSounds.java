package com.temoria.disc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, TemoriaDiscMod.MOD_ID);

    public static final List<DeferredHolder<SoundEvent, SoundEvent>> ALL_SOUNDS = new ArrayList<>();

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        DeferredHolder<SoundEvent, SoundEvent> sound = SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, name)));
        ALL_SOUNDS.add(sound);
        return sound;
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> ADVENTURING = registerSound("music.adventuring");
    public static final DeferredHolder<SoundEvent, SoundEvent> ARCTICWINDS = registerSound("music.arcticwinds");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUILDING = registerSound("music.building");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHILLY_VILLAGE = registerSound("music.chilly_village");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREATING = registerSound("music.creating");
    public static final DeferredHolder<SoundEvent, SoundEvent> CULTUREDTAVERN = registerSound("music.culturedtavern");
    public static final DeferredHolder<SoundEvent, SoundEvent> DAYLIGHT = registerSound("music.daylight");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEVASTATION_BASELAYER = registerSound("music.devastation_baselayer");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEVASTATION_BELLSANDRUST = registerSound("music.devastation_bellsandrust");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEVASTATION_ERELFIGHT = registerSound("music.devastation_erelfight");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEVASTATION_ERELHARRASS = registerSound("music.devastation_erelharrass");
    public static final DeferredHolder<SoundEvent, SoundEvent> FALL = registerSound("music.fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> FALL_O_CROFT = registerSound("music.fall_o_croft");
    public static final DeferredHolder<SoundEvent, SoundEvent> GROOVE = registerSound("music.groove");
    public static final DeferredHolder<SoundEvent, SoundEvent> HALLOWCROFT = registerSound("music.hallowcroft");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTBEAT_CREATE = registerSound("music.heartbeat_create");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTBEAT_SURVIVE = registerSound("music.heartbeat_survive");
    public static final DeferredHolder<SoundEvent, SoundEvent> LATVIA = registerSound("music.latvia");
    public static final DeferredHolder<SoundEvent, SoundEvent> MIDNIGHT = registerSound("music.midnight");
    public static final DeferredHolder<SoundEvent, SoundEvent> MIRROR = registerSound("music.mirror");
    public static final DeferredHolder<SoundEvent, SoundEvent> NADIYA_FALL = registerSound("music.nadiya_fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> NADIYA_SPRING = registerSound("music.nadiya_spring");
    public static final DeferredHolder<SoundEvent, SoundEvent> NADIYA_SUMMER = registerSound("music.nadiya_summer");
    public static final DeferredHolder<SoundEvent, SoundEvent> NADIYA_WINTER = registerSound("music.nadiya_winter");
    public static final DeferredHolder<SoundEvent, SoundEvent> NIGHT_TO_DAY = registerSound("music.night_to_day");
    public static final DeferredHolder<SoundEvent, SoundEvent> NOSTALGIC = registerSound("music.nostalgic");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEACEFUL_VILLAGE = registerSound("music.peaceful_village");
    public static final DeferredHolder<SoundEvent, SoundEvent> QUIRKYTAVERN = registerSound("music.quirkytavern");
    public static final DeferredHolder<SoundEvent, SoundEvent> RADIANCEANDRUST = registerSound("music.radianceandrust");
    public static final DeferredHolder<SoundEvent, SoundEvent> ROOTS = registerSound("music.roots");
    public static final DeferredHolder<SoundEvent, SoundEvent> SAFETY_OF_A_WARM_FIRE = registerSound("music.safety_of_a_warm_fire");
    public static final DeferredHolder<SoundEvent, SoundEvent> SETTING_SUN = registerSound("music.setting_sun");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPRING = registerSound("music.spring");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUBSTITUTE_EPITAPH = registerSound("music.substitute_epitaph");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUMMER_DAY = registerSound("music.summer_day");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUMMER_STROLL = registerSound("music.summer_stroll");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUNNY_VILLAGE = registerSound("music.sunny_village");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUNNY_VILLAGE_CREATE = registerSound("music.sunny_village_create");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUNNY_VILLAGE_SURVIVE = registerSound("music.sunny_village_survive");
    public static final DeferredHolder<SoundEvent, SoundEvent> THE_LAZARET = registerSound("music.the_lazaret");
    public static final DeferredHolder<SoundEvent, SoundEvent> THE_SERAPHS = registerSound("music.the_seraphs");
    public static final DeferredHolder<SoundEvent, SoundEvent> THEINVENTION = registerSound("music.theinvention");
    public static final DeferredHolder<SoundEvent, SoundEvent> THERESONANCEARCHIVES_BARREN = registerSound("music.theresonancearchives_barren");
    public static final DeferredHolder<SoundEvent, SoundEvent> THERESONANCEARCHIVES_EIDOLON = registerSound("music.theresonancearchives_eidolon");
    public static final DeferredHolder<SoundEvent, SoundEvent> THERESONANCEARCHIVES_LIBRARY = registerSound("music.theresonancearchives_library");
    public static final DeferredHolder<SoundEvent, SoundEvent> THROUGH_THE_GRASS_CREATE = registerSound("music.through_the_grass_create");
    public static final DeferredHolder<SoundEvent, SoundEvent> THROUGH_THE_GRASS_SURVIVE = registerSound("music.through_the_grass_survive");
    public static final DeferredHolder<SoundEvent, SoundEvent> TO_DAWN = registerSound("music.to_dawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOBIAS = registerSound("music.tobias");
    public static final DeferredHolder<SoundEvent, SoundEvent> VINTAGESTORY = registerSound("music.vintagestory");
    public static final DeferredHolder<SoundEvent, SoundEvent> WINDY_VILLAGE = registerSound("music.windy_village");
    public static final DeferredHolder<SoundEvent, SoundEvent> WINTER = registerSound("music.winter");
    public static final DeferredHolder<SoundEvent, SoundEvent> NOAH_TES_BAN = registerSound("music.noah_tes_ban");
}
