package com.temoria.disc.vol2;

import com.temoria.disc.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod("temoria_disc_vs_vol2")
public class TemoriaDiscVol2Mod {
    public static final String MOD_ID = "temoria_disc_vs_vol2";

    public TemoriaDiscVol2Mod(IEventBus modEventBus) {
        ModSoundsVol2.SOUND_EVENTS.register(modEventBus);
        ModItemsVol2.ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ModCreativeTabs.VINTAGE_STORY_TAB_KEY)) {
            for (var disc : ModItemsVol2.DISCS) {
                event.accept(disc.get());
            }
        }
    }
}
