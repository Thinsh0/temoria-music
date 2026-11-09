package com.temoria.disc.vol2;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModSoundsVol2 {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, "temoria_disc");

    public static final List<DeferredHolder<SoundEvent, SoundEvent>> ALL_SOUNDS = new ArrayList<>();

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        DeferredHolder<SoundEvent, SoundEvent> sound = SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("temoria_disc", name)));
        ALL_SOUNDS.add(sound);
        return sound;
    }

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
}
