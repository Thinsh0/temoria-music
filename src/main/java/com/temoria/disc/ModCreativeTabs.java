package com.temoria.disc;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TemoriaDiscMod.MOD_ID);

    public static final ResourceKey<CreativeModeTab> VINTAGE_STORY_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, "vintage_story_discs"));

    public static final ResourceKey<CreativeModeTab> TEMORIA_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, "temoria_discs"));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VINTAGE_STORY_TAB =
            CREATIVE_MODE_TABS.register("vintage_story_discs", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.temoria_disc.vintage_story"))
                    .icon(() -> new ItemStack(ModItems.DISC_VINTAGESTORY.get()))
                    .displayItems((params, output) -> {
                        for (var disc : ModItems.VINTAGE_STORY_DISCS) {
                            output.accept(disc.get());
                        }
                    })
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TEMORIA_TAB =
            CREATIVE_MODE_TABS.register("temoria_discs", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.temoria_disc.temoria"))
                    .icon(() -> new ItemStack(ModItems.DISC_NOAH_TES_BAN.get()))
                    .displayItems((params, output) -> {
                        for (var disc : ModItems.TEMORIA_DISCS) {
                            output.accept(disc.get());
                        }
                    })
                    .build());
}
