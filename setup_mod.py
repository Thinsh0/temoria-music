import os
import re
import json
import struct
import zipfile
import subprocess
import shutil
import colorsys
from PIL import Image

BASE_VS = os.path.expandvars(r'%APPDATA%\Vintagestory\assets')
MINECRAFT_JAR = os.path.expandvars(r'%APPDATA%\ModrinthApp\meta\versions\1.21.1\1.21.1.jar')
TEMORIA_MP3 = r'C:\Users\thinsho\Downloads\Noah tes ban !.mp3'
ROOT = os.path.abspath(os.path.dirname(__file__))

GEN_DIR = os.path.join(ROOT, "src", "generated")
CORE_DIR = os.path.join(GEN_DIR, "core")
VOL1_DIR = os.path.join(GEN_DIR, "vol1")
VOL2_DIR = os.path.join(GEN_DIR, "vol2")

JAVA_ROOT = os.path.join(ROOT, "src", "main", "java", "com", "temoria", "disc")
JAVA_CORE = JAVA_ROOT
JAVA_VOL1 = os.path.join(JAVA_ROOT, "vol1")
JAVA_VOL2 = os.path.join(JAVA_ROOT, "vol2")

# Clean old generated
if os.path.exists(GEN_DIR):
    shutil.rmtree(GEN_DIR)

for d in [CORE_DIR, VOL1_DIR, VOL2_DIR, JAVA_CORE, JAVA_VOL1, JAVA_VOL2]:
    os.makedirs(d, exist_ok=True)

def get_ogg_duration(path):
    with open(path, 'rb') as f:
        data = f.read()
    idx = data.find(b'\x01vorbis')
    if idx == -1: return 120.0
    rate = struct.unpack('<I', data[idx+7+4+1:idx+7+4+1+4])[0]
    last_oggs = data.rfind(b'OggS')
    if last_oggs == -1: return 120.0
    granule = struct.unpack('<q', data[last_oggs+6:last_oggs+14])[0]
    return round(granule / rate, 1) if rate > 0 else 120.0

def get_tags(p):
    tags = {}
    with open(p, 'rb') as f:
        data = f.read(16384)
    idx = data.find(b'\x03vorbis')
    if idx != -1:
        pos = idx + 7
        if pos + 4 <= len(data):
            vlen = struct.unpack('<I', data[pos:pos+4])[0]
            pos += 4 + vlen
            if pos + 4 <= len(data):
                count = struct.unpack('<I', data[pos:pos+4])[0]
                pos += 4
                for _ in range(min(count, 50)):
                    if pos + 4 > len(data): break
                    clen = struct.unpack('<I', data[pos:pos+4])[0]
                    pos += 4
                    if pos + clen > len(data): break
                    c = data[pos:pos+clen].decode('utf-8', 'ignore')
                    pos += clen
                    if '=' in c:
                        k, v = c.split('=', 1)
                        tags[k.upper()] = v.strip()
    return tags

OVERRIDES = {
    'chilly-village.ogg': 'Chilly Village',
    'substitute-epitaph.ogg': 'Substitute Epitaph',
    'tobias.ogg': 'Tobias',
    'windy-village.ogg': 'Windy Village',
    'fall-o\'-croft.ogg': "Fall-o'-croft",
}

# 1. Collect Vintage Story tracks
vs_tracks = []
for root, dirs, files in os.walk(BASE_VS):
    if 'deepunderground' in root: continue
    if 'music' in root:
        for f in files:
            if f.endswith('.ogg'):
                p = os.path.join(root, f)
                dur = get_ogg_duration(p)
                tags = get_tags(p)
                title = OVERRIDES.get(f, tags.get('TITLE', ''))
                if not title:
                    title = f.replace('.ogg', '').replace('-', ' ').title()
                artist = tags.get('ARTIST', 'Lo-Phi')
                if not artist:
                    artist = 'Lo-Phi'
                
                clean_id = f.replace('.ogg', '').lower().replace(' ', '_').replace('-', '_').replace('\'', '')
                clean_id = re.sub(r'[^a-z0-9_]', '', clean_id)
                vs_tracks.append({
                    'id': clean_id,
                    'album': 'vintage_story',
                    'file': f,
                    'path': p,
                    'duration': dur,
                    'title': title,
                    'artist': artist,
                    'is_mp3': False
                })

vs_tracks.sort(key=lambda t: t['id'])
half = len(vs_tracks) // 2
vol1_tracks = vs_tracks[:half]
vol2_tracks = vs_tracks[half:]

# 2. Temoria custom track
temoria_tracks = [
    {
        'id': 'noah_tes_ban',
        'album': 'temoria',
        'file': 'noah_tes_ban.ogg',
        'path': TEMORIA_MP3,
        'duration': 116.1,
        'title': "Noah t'es ban !",
        'artist': 'Temoria',
        'is_mp3': True
    }
]

print(f"Total: {len(vs_tracks)} VS tracks (Vol1: {len(vol1_tracks)}, Vol2: {len(vol2_tracks)}) and {len(temoria_tracks)} Temoria tracks.")

# Base disc texture from Minecraft jar
base_disc = None
with zipfile.ZipFile(MINECRAFT_JAR, 'r') as z:
    with z.open('assets/minecraft/textures/item/music_disc_cat.png') as f:
        base_disc = Image.open(f).convert('RGBA')

# Palettes for all 52 VS tracks
vs_palettes = []
for i in range(len(vs_tracks)):
    hue = (i * 0.618033988749895) % 1.0
    sat = 0.65 + (i % 3) * 0.12
    val = 0.85 + (i % 2) * 0.10
    r, g, b = colorsys.hsv_to_rgb(hue, sat, val)
    r_hi, g_hi, b_hi = colorsys.hsv_to_rgb(hue, sat * 0.5, min(1.0, val * 1.2))
    r_sh, g_sh, b_sh = colorsys.hsv_to_rgb(hue, min(1.0, sat * 1.1), val * 0.65)
    vs_palettes.append({
        'base': (int(r * 255), int(g * 255), int(b * 255), 255),
        'hi': (int(r_hi * 255), int(g_hi * 255), int(b_hi * 255), 255),
        'sh': (int(r_sh * 255), int(g_sh * 255), int(b_sh * 255), 255),
    })

label_coords = {
    (6, 6): 'hi', (7, 6): 'hi', (8, 6): 'hi', (9, 6): 'base',
    (5, 7): 'hi', (6, 7): 'base', (9, 7): 'sh', (10, 7): 'sh',
    (6, 8): 'base', (7, 8): 'sh', (8, 8): 'sh', (9, 8): 'sh',
}

# Function to write package resources
def build_module_resources(target_dir, mod_id, mod_name, desc, tracks_list, palettes_list, is_core=False, deps=[]):
    sounds_dest = os.path.join(target_dir, "assets", "temoria_disc", "sounds", "music")
    textures_dest = os.path.join(target_dir, "assets", "temoria_disc", "textures", "item")
    models_dest = os.path.join(target_dir, "assets", "temoria_disc", "models", "item")
    jukebox_dest = os.path.join(target_dir, "data", "temoria_disc", "jukebox_song")
    tags_dest = os.path.join(target_dir, "data", "minecraft", "tags", "item")
    lang_dest = os.path.join(target_dir, "assets", "temoria_disc", "lang")
    meta_dest = os.path.join(target_dir, "META-INF")

    for d in [sounds_dest, textures_dest, models_dest, jukebox_dest, tags_dest, lang_dest, meta_dest]:
        os.makedirs(d, exist_ok=True)

    # 1. neoforge.mods.toml
    toml = f'''modLoader="javafml"
loaderVersion="[4,)"
license="All Rights Reserved"

[[mods]]
modId="{mod_id}"
version="1.0.0"
displayName="{mod_name}"
authors="Thinsho"
description=\'\'\'{desc}\'\'\'

[[dependencies.{mod_id}]]
modId="neoforge"
type="required"
versionRange="[21.1.0,)"
ordering="NONE"
side="BOTH"

[[dependencies.{mod_id}]]
modId="minecraft"
type="required"
versionRange="[1.21.1,1.22)"
ordering="NONE"
side="BOTH"
'''
    for dep in deps:
        toml += f'''
[[dependencies.{mod_id}]]
modId="{dep}"
type="required"
versionRange="[1.0.0,)"
ordering="AFTER"
side="BOTH"
'''
    with open(os.path.join(meta_dest, "neoforge.mods.toml"), 'w', encoding='utf-8') as f:
        f.write(toml)

    sounds_json = {}
    en_lang = {}
    fr_lang = {}
    tag_values = []

    if is_core:
        en_lang["itemGroup.temoria_disc.vintage_story"] = "Vintage Story OST Discs"
        en_lang["itemGroup.temoria_disc.temoria"] = "Temoria Discs"
        fr_lang["itemGroup.temoria_disc.vintage_story"] = "Disques - Vintage Story"
        fr_lang["itemGroup.temoria_disc.temoria"] = "Disques - Temoria"

    for i, track in enumerate(tracks_list):
        tid = track['id']
        disc_item_id = f"music_disc_{tid}"
        tag_values.append(f"temoria_disc:{disc_item_id}")

        dest_ogg = os.path.join(sounds_dest, f"{tid}.ogg")
        if track['is_mp3']:
            cmd = [
                "ffmpeg", "-y", "-i", track['path'],
                "-c:a", "libvorbis", "-q:a", "6", dest_ogg
            ]
            subprocess.run(cmd, check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        else:
            with open(track['path'], 'rb') as src, open(dest_ogg, 'wb') as dst:
                dst.write(src.read())

        # Disc texture
        tex = base_disc.copy()
        pal = palettes_list[i]
        for (lx, ly), ctype in label_coords.items():
            tex.putpixel((lx, ly), pal[ctype])
        tex.save(os.path.join(textures_dest, f"{disc_item_id}.png"))

        # Model JSON
        model_json = {
            "parent": "minecraft:item/generated",
            "textures": {
                "layer0": f"temoria_disc:item/{disc_item_id}"
            }
        }
        with open(os.path.join(models_dest, f"{disc_item_id}.json"), 'w') as f:
            json.dump(model_json, f, indent=2)

        # sounds.json
        sounds_json[f"music.{tid}"] = {
            "sounds": [
                {
                    "name": f"temoria_disc:music/{tid}",
                    "stream": True
                }
            ]
        }

        # jukebox_song JSON
        jukebox_data = {
            "sound_event": f"temoria_disc:music.{tid}",
            "description": {
                "translate": f"jukebox_song.temoria_disc.{tid}"
            },
            "length_in_seconds": track['duration'],
            "comparator_output": 15
        }
        with open(os.path.join(jukebox_dest, f"{tid}.json"), 'w') as f:
            json.dump(jukebox_data, f, indent=2)

        # Lang entries
        en_lang[f"item.temoria_disc.{disc_item_id}"] = "Music Disc"
        en_lang[f"jukebox_song.temoria_disc.{tid}"] = f"{track['artist']} - {track['title']}"

        fr_lang[f"item.temoria_disc.{disc_item_id}"] = "Disque de musique"
        fr_lang[f"jukebox_song.temoria_disc.{tid}"] = f"{track['artist']} - {track['title']}"

    with open(os.path.join(target_dir, "assets", "temoria_disc", "sounds.json"), 'w') as f:
        json.dump(sounds_json, f, indent=2)

    with open(os.path.join(tags_dest, "music_discs.json"), 'w') as f:
        json.dump({"values": tag_values}, f, indent=2)

    with open(os.path.join(lang_dest, "en_us.json"), 'w') as f:
        json.dump(en_lang, f, indent=2, ensure_ascii=False)

    with open(os.path.join(lang_dest, "fr_fr.json"), 'w') as f:
        json.dump(fr_lang, f, indent=2, ensure_ascii=False)

# Build resources for each module
temoria_pal = [{
    'base': (220, 20, 30, 255),
    'hi': (255, 215, 0, 255),
    'sh': (120, 0, 15, 255),
}]

build_module_resources(CORE_DIR, "temoria_disc", "Temoria Discs", "Core mod providing creative tabs and Temoria music discs.", temoria_tracks, temoria_pal, is_core=True)
build_module_resources(VOL1_DIR, "temoria_disc_vs_vol1", "Temoria Discs - Vintage Story Vol 1", "Vintage Story OST Music Discs (Volume 1)", vol1_tracks, vs_palettes[:half], is_core=False, deps=["temoria_disc"])
build_module_resources(VOL2_DIR, "temoria_disc_vs_vol2", "Temoria Discs - Vintage Story Vol 2", "Vintage Story OST Music Discs (Volume 2)", vol2_tracks, vs_palettes[half:], is_core=False, deps=["temoria_disc"])

print("Built all module resources.")

# Generate Java code

# 1. CORE JAVA
# ModSounds.java
with open(os.path.join(JAVA_CORE, "ModSounds.java"), 'w', encoding='utf-8') as f:
    f.write('''package com.temoria.disc;

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
''')

# ModItems.java
with open(os.path.join(JAVA_CORE, "ModItems.java"), 'w', encoding='utf-8') as f:
    f.write('''package com.temoria.disc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TemoriaDiscMod.MOD_ID);

    public static final DeferredItem<Item> DISC_NOAH_TES_BAN = ITEMS.registerItem(
            "music_disc_noah_tes_ban",
            Item::new,
            new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).jukeboxPlayable(
                    ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, "noah_tes_ban"))
            )
    );
}
''')

# ModCreativeTabs.java
with open(os.path.join(JAVA_CORE, "ModCreativeTabs.java"), 'w', encoding='utf-8') as f:
    f.write('''package com.temoria.disc;

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
''')

# TemoriaDiscMod.java
with open(os.path.join(JAVA_CORE, "TemoriaDiscMod.java"), 'w', encoding='utf-8') as f:
    f.write('''package com.temoria.disc;

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
''')

# 2. VOL1 JAVA
def generate_volume_java(vol_id, class_suffix, pkg, java_dir, tracks):
    # ModSounds
    sounds_code = f'''package {pkg};

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModSounds{class_suffix} {{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, "temoria_disc");

    public static final List<DeferredHolder<SoundEvent, SoundEvent>> ALL_SOUNDS = new ArrayList<>();

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {{
        DeferredHolder<SoundEvent, SoundEvent> sound = SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("temoria_disc", name)));
        ALL_SOUNDS.add(sound);
        return sound;
    }}

'''
    for t in tracks:
        field_name = t['id'].upper()
        sounds_code += f'    public static final DeferredHolder<SoundEvent, SoundEvent> {field_name} = registerSound("music.{t["id"]}");\n'
    sounds_code += "}\n"

    with open(os.path.join(java_dir, f"ModSounds{class_suffix}.java"), 'w', encoding='utf-8') as f:
        f.write(sounds_code)

    # ModItems
    items_code = f'''package {pkg};

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModItems{class_suffix} {{
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("temoria_disc");

    public static final List<DeferredItem<Item>> DISCS = new ArrayList<>();

    private static DeferredItem<Item> registerDisc(String songId) {{
        ResourceKey<JukeboxSong> songKey = ResourceKey.create(
                Registries.JUKEBOX_SONG,
                ResourceLocation.fromNamespaceAndPath("temoria_disc", songId)
        );

        DeferredItem<Item> item = ITEMS.registerItem(
                "music_disc_" + songId,
                Item::new,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(songKey)
        );
        DISCS.add(item);
        return item;
    }}

'''
    for t in tracks:
        field_name = "DISC_" + t['id'].upper()
        items_code += f'    public static final DeferredItem<Item> {field_name} = registerDisc("{t["id"]}");\n'
    items_code += "}\n"

    with open(os.path.join(java_dir, f"ModItems{class_suffix}.java"), 'w', encoding='utf-8') as f:
        f.write(items_code)

    # Mod class
    mod_code = f'''package {pkg};

import com.temoria.disc.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod("{vol_id}")
public class TemoriaDisc{class_suffix}Mod {{
    public static final String MOD_ID = "{vol_id}";

    public TemoriaDisc{class_suffix}Mod(IEventBus modEventBus) {{
        ModSounds{class_suffix}.SOUND_EVENTS.register(modEventBus);
        ModItems{class_suffix}.ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
    }}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {{
        if (event.getTabKey().equals(ModCreativeTabs.VINTAGE_STORY_TAB_KEY)) {{
            for (var disc : ModItems{class_suffix}.DISCS) {{
                event.accept(disc.get());
            }}
        }}
    }}
}}
'''
    with open(os.path.join(java_dir, f"TemoriaDisc{class_suffix}Mod.java"), 'w', encoding='utf-8') as f:
        f.write(mod_code)

generate_volume_java("temoria_disc_vs_vol1", "Vol1", "com.temoria.disc.vol1", JAVA_VOL1, vol1_tracks)
generate_volume_java("temoria_disc_vs_vol2", "Vol2", "com.temoria.disc.vol2", JAVA_VOL2, vol2_tracks)

print("Java files generated successfully.")
