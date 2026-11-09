package com.temoria.disc;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
                    .icon(() -> {
                        var vsDisc = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                                ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, "music_disc_vintagestory")
                        );
                        if (vsDisc != Items.AIR) {
                            return new ItemStack(vsDisc);
                        }
                        for (var item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                            var key = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
                            if (key != null && key.getNamespace().equals(TemoriaDiscMod.MOD_ID) && key.getPath().startsWith("music_disc_")) {
                                return new ItemStack(item);
                            }
                        }
                        return new ItemStack(Items.MUSIC_DISC_CAT);
                    })
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TEMORIA_TAB =
            CREATIVE_MODE_TABS.register("temoria_discs", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.temoria_disc.temoria"))
                    .icon(() -> new ItemStack(ModItems.DISC_NOAH_TES_BAN.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.DISC_NOAH_TES_BAN.get());
                    })
                    .build());
}
