package com.temoria.disc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, TemoriaDiscMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> NOAH_TES_BAN =
            SOUND_EVENTS.register("music.noah_tes_ban",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, "music.noah_tes_ban")));
}
