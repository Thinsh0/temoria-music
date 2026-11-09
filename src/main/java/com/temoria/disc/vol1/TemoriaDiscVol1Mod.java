package com.temoria.disc.vol1;

import com.temoria.disc.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod("temoria_disc_vs_vol1")
public class TemoriaDiscVol1Mod {
    public static final String MOD_ID = "temoria_disc_vs_vol1";

    public TemoriaDiscVol1Mod(IEventBus modEventBus) {
        ModSoundsVol1.SOUND_EVENTS.register(modEventBus);
        ModItemsVol1.ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ModCreativeTabs.VINTAGE_STORY_TAB_KEY)) {
            for (var disc : ModItemsVol1.DISCS) {
                event.accept(disc.get());
            }
        }
    }
}
