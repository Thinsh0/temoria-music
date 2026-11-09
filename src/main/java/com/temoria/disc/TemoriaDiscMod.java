package com.temoria.disc;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(TemoriaDiscMod.MOD_ID)
public class TemoriaDiscMod {
    public static final String MOD_ID = "temoria_disc";

    public TemoriaDiscMod(IEventBus modEventBus) {
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
