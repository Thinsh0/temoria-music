package com.temoria.disc;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(TemoriaDiscMod.MOD_ID)
public class TemoriaDiscMod {
    public static final String MOD_ID = "temoria_disc";

    public TemoriaDiscMod(IEventBus modEventBus, Dist dist) {
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        if (dist.isClient()) {
            TemoriaMusicManager.init();
            modEventBus.addListener(this::onAddPackFinders);
        }
    }

    private void onAddPackFinders(AddPackFindersEvent event) {
        TemoriaMusicManager.onAddPackFinders(event);
    }
}
