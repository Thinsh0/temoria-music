package com.temoria.disc.vol1;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModSoundsVol1 {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, "temoria_disc");

    public static final List<DeferredHolder<SoundEvent, SoundEvent>> ALL_SOUNDS = new ArrayList<>();

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        DeferredHolder<SoundEvent, SoundEvent> sound = SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("temoria_disc", name)));
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
}
