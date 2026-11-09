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

RES_DIR = os.path.join(ROOT, "src", "main", "resources")
JAVA_DIR = os.path.join(ROOT, "src", "main", "java", "com", "temoria", "disc")

SOUNDS_DEST = os.path.join(RES_DIR, "assets", "temoria_disc", "sounds", "music")
TEXTURES_DEST = os.path.join(RES_DIR, "assets", "temoria_disc", "textures", "item")
MODELS_DEST = os.path.join(RES_DIR, "assets", "temoria_disc", "models", "item")
JUKEBOX_DEST = os.path.join(RES_DIR, "data", "temoria_disc", "jukebox_song")
TAGS_DEST = os.path.join(RES_DIR, "data", "minecraft", "tags", "item")
LANG_DEST = os.path.join(RES_DIR, "assets", "temoria_disc", "lang")
META_DEST = os.path.join(RES_DIR, "META-INF")

for d in [SOUNDS_DEST, TEXTURES_DEST, MODELS_DEST, JUKEBOX_DEST, TAGS_DEST, LANG_DEST, META_DEST, JAVA_DIR]:
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

# 1. Vintage Story tracks
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

all_tracks = vs_tracks + temoria_tracks
print(f"Loaded {len(vs_tracks)} Vintage Story tracks and {len(temoria_tracks)} Temoria tracks.")

# Base disc texture from Minecraft jar
base_disc = None
with zipfile.ZipFile(MINECRAFT_JAR, 'r') as z:
    with z.open('assets/minecraft/textures/item/music_disc_cat.png') as f:
        base_disc = Image.open(f).convert('RGBA')

# Distinct palettes for VS tracks
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

temoria_palettes = [
    {
        'base': (220, 20, 30, 255),
        'hi': (255, 215, 0, 255),
        'sh': (120, 0, 15, 255),
    }
]

label_coords = {
    (6, 6): 'hi', (7, 6): 'hi', (8, 6): 'hi', (9, 6): 'base',
    (5, 7): 'hi', (6, 7): 'base', (9, 7): 'sh', (10, 7): 'sh',
    (6, 8): 'base', (7, 8): 'sh', (8, 8): 'sh', (9, 8): 'sh',
}

# 1. neoforge.mods.toml
with open(os.path.join(META_DEST, "neoforge.mods.toml"), 'w', encoding='utf-8') as f:
    f.write('''modLoader="javafml"
loaderVersion="[4,)"
license="All Rights Reserved"

[[mods]]
modId="temoria_disc"
version="1.0.0"
displayName="Temoria Discs"
authors="Thinsho"
description=\'\'\'Adds music discs with the Vintage Story OST and exclusive Temoria tracks.\'\'\'

[[dependencies.temoria_disc]]
modId="neoforge"
type="required"
versionRange="[21.1.0,)"
ordering="NONE"
side="BOTH"

[[dependencies.temoria_disc]]
modId="minecraft"
type="required"
versionRange="[1.21.1,1.22)"
ordering="NONE"
side="BOTH"
''')

sounds_json = {}
en_lang = {
    "itemGroup.temoria_disc.vintage_story": "Vintage Story OST Discs",
    "itemGroup.temoria_disc.temoria": "Temoria Discs"
}
fr_lang = {
    "itemGroup.temoria_disc.vintage_story": "Disques - Vintage Story",
    "itemGroup.temoria_disc.temoria": "Disques - Temoria"
}
tag_values = []

for i, track in enumerate(all_tracks):
    tid = track['id']
    disc_item_id = f"music_disc_{tid}"
    tag_values.append(f"temoria_disc:{disc_item_id}")

    # Copy audio ONLY for Temoria tracks (VS tracks are loaded via external music pack)
    dest_ogg = os.path.join(SOUNDS_DEST, f"{tid}.ogg")
    if track['is_mp3']:
        cmd = [
            "ffmpeg", "-y", "-i", track['path'],
            "-c:a", "libvorbis", "-q:a", "6", dest_ogg
        ]
        subprocess.run(cmd, check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

    # Custom disc texture
    tex = base_disc.copy()
    if track['album'] == 'temoria':
        pal = temoria_palettes[0]
    else:
        pal = vs_palettes[i]
        
    for (lx, ly), ctype in label_coords.items():
        tex.putpixel((lx, ly), pal[ctype])
    tex.save(os.path.join(TEXTURES_DEST, f"{disc_item_id}.png"))

    # Model JSON
    model_json = {
        "parent": "minecraft:item/generated",
        "textures": {
            "layer0": f"temoria_disc:item/{disc_item_id}"
        }
    }
    with open(os.path.join(MODELS_DEST, f"{disc_item_id}.json"), 'w') as f:
        json.dump(model_json, f, indent=2)

    # sounds.json entry
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
    with open(os.path.join(JUKEBOX_DEST, f"{tid}.json"), 'w') as f:
        json.dump(jukebox_data, f, indent=2)

    # Lang entries
    en_lang[f"item.temoria_disc.{disc_item_id}"] = "Music Disc"
    en_lang[f"jukebox_song.temoria_disc.{tid}"] = f"{track['artist']} - {track['title']}"

    fr_lang[f"item.temoria_disc.{disc_item_id}"] = "Disque de musique"
    fr_lang[f"jukebox_song.temoria_disc.{tid}"] = f"{track['artist']} - {track['title']}"

# Write sounds.json
with open(os.path.join(RES_DIR, "assets", "temoria_disc", "sounds.json"), 'w') as f:
    json.dump(sounds_json, f, indent=2)

# Write tags
with open(os.path.join(TAGS_DEST, "music_discs.json"), 'w') as f:
    json.dump({"values": tag_values}, f, indent=2)

# Write lang files
with open(os.path.join(LANG_DEST, "en_us.json"), 'w') as f:
    json.dump(en_lang, f, indent=2, ensure_ascii=False)

with open(os.path.join(LANG_DEST, "fr_fr.json"), 'w') as f:
    json.dump(fr_lang, f, indent=2, ensure_ascii=False)

print("Generated all resources (textures, models, jukebox songs, sounds.json, lang).")

# Java generation
# 1. ModSounds.java
java_sounds = """package com.temoria.disc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, TemoriaDiscMod.MOD_ID);

    public static final List<DeferredHolder<SoundEvent, SoundEvent>> ALL_SOUNDS = new ArrayList<>();

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        DeferredHolder<SoundEvent, SoundEvent> sound = SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, name)));
        ALL_SOUNDS.add(sound);
        return sound;
    }

"""
for t in all_tracks:
    field_name = t['id'].upper()
    java_sounds += f"    public static final DeferredHolder<SoundEvent, SoundEvent> {field_name} = registerSound(\"music.{t['id']}\");\n"

java_sounds += "}\n"

with open(os.path.join(JAVA_DIR, "ModSounds.java"), 'w', encoding='utf-8') as f:
    f.write(java_sounds)

# 2. ModItems.java
java_items = """package com.temoria.disc;

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

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TemoriaDiscMod.MOD_ID);

    public static final List<DeferredItem<Item>> ALL_DISCS = new ArrayList<>();
    public static final List<DeferredItem<Item>> VINTAGE_STORY_DISCS = new ArrayList<>();
    public static final List<DeferredItem<Item>> TEMORIA_DISCS = new ArrayList<>();

    private static DeferredItem<Item> registerDisc(String songId, String album) {
        ResourceKey<JukeboxSong> songKey = ResourceKey.create(
                Registries.JUKEBOX_SONG,
                ResourceLocation.fromNamespaceAndPath(TemoriaDiscMod.MOD_ID, songId)
        );

        DeferredItem<Item> item = ITEMS.registerItem(
                "music_disc_" + songId,
                Item::new,
                new Item.Properties().stacksTo(1).rarity(album.equals("temoria") ? Rarity.EPIC : Rarity.RARE).jukeboxPlayable(songKey)
        );
        ALL_DISCS.add(item);
        if ("temoria".equals(album)) {
            TEMORIA_DISCS.add(item);
        } else {
            VINTAGE_STORY_DISCS.add(item);
        }
        return item;
    }

"""
for t in all_tracks:
    field_name = "DISC_" + t['id'].upper()
    java_items += f"    public static final DeferredItem<Item> {field_name} = registerDisc(\"{t['id']}\", \"{t['album']}\");\n"

java_items += "}\n"

with open(os.path.join(JAVA_DIR, "ModItems.java"), 'w', encoding='utf-8') as f:
    f.write(java_items)

# 3. ModCreativeTabs.java
java_tabs = """package com.temoria.disc;

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
"""
with open(os.path.join(JAVA_DIR, "ModCreativeTabs.java"), 'w', encoding='utf-8') as f:
    f.write(java_tabs)

# 4. TemoriaMusicManager.java
java_manager = """package com.temoria.disc;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class TemoriaMusicManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String DOWNLOAD_URL =
            "https://github.com/Thinsh0/temoria-music/releases/download/v1.0.0/vintage_story_music.zip";

    private static Path zipPath;

    public static void init() {
        try {
            String localAppData = System.getenv("LOCALAPPDATA");
            Path baseDir = (localAppData != null && !localAppData.isBlank())
                    ? Paths.get(localAppData, "Temoria", "music")
                    : Paths.get(System.getProperty("user.home"), ".temoria", "music");

            Files.createDirectories(baseDir);
            zipPath = baseDir.resolve("vintage_story_music.zip");

            if (Files.exists(zipPath) && Files.size(zipPath) > 400L * 1024 * 1024) {
                LOGGER.info("[TemoriaDiscs] Vintage Story music pack found at {}", zipPath);
                return;
            }

            LOGGER.info("[TemoriaDiscs] Downloading Vintage Story music pack from GitHub Releases...");
            downloadMusicPack(zipPath);
        } catch (Exception e) {
            LOGGER.error("[TemoriaDiscs] Failed to initialize music pack", e);
        }
    }

    private static void downloadMusicPack(Path target) {
        Path tempFile = target.resolveSibling("vintage_story_music.tmp");
        try {
            URL url = URI.create(DOWNLOAD_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent", "TemoriaDiscs-MinecraftMod");

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == 307 || status == 308) {
                String newUrl = conn.getHeaderField("Location");
                conn = (HttpURLConnection) URI.create(newUrl).toURL().openConnection();
                conn.setRequestProperty("User-Agent", "TemoriaDiscs-MinecraftMod");
            }

            long contentLength = conn.getContentLengthLong();
            LOGGER.info("[TemoriaDiscs] Downloading {} bytes from {}", contentLength, url);

            try (InputStream in = new BufferedInputStream(conn.getInputStream());
                 FileOutputStream out = new FileOutputStream(tempFile.toFile())) {
                byte[] buffer = new byte[65536];
                int read;
                long total = 0;
                long lastLog = System.currentTimeMillis();

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                    total += read;
                    if (System.currentTimeMillis() - lastLog > 3000) {
                        lastLog = System.currentTimeMillis();
                        int percent = contentLength > 0 ? (int) (total * 100 / contentLength) : 0;
                        LOGGER.info("[TemoriaDiscs] Download progress: {}% ({} MB)", percent, total / (1024 * 1024));
                    }
                }
            }

            Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("[TemoriaDiscs] Music pack download complete!");
        } catch (Exception e) {
            LOGGER.error("[TemoriaDiscs] Download failed", e);
            try { Files.deleteIfExists(tempFile); } catch (Exception ignored) {}
        }
    }

    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
        if (zipPath == null || !Files.exists(zipPath)) return;

        try {
            PackLocationInfo info = new PackLocationInfo(
                    "temoria_vs_music",
                    Component.literal("Temoria - Vintage Story OST"),
                    PackSource.BUILT_IN,
                    Optional.empty()
            );
            PackSelectionConfig config = new PackSelectionConfig(true, Pack.Position.TOP, false);
            Pack pack = Pack.readMetaAndCreate(
                    info,
                    new FilePackResources.FileResourcesSupplier(zipPath),
                    PackType.CLIENT_RESOURCES,
                    config
            );
            if (pack != null) {
                event.addRepositorySource(consumer -> consumer.accept(pack));
                LOGGER.info("[TemoriaDiscs] Successfully registered Vintage Story music resource pack!");
            }
        } catch (Exception e) {
            LOGGER.error("[TemoriaDiscs] Failed to register music pack in AddPackFindersEvent", e);
        }
    }
}
"""
with open(os.path.join(JAVA_DIR, "TemoriaMusicManager.java"), 'w', encoding='utf-8') as f:
    f.write(java_manager)

# 5. TemoriaDiscMod.java
java_mod = """package com.temoria.disc;

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
"""
with open(os.path.join(JAVA_DIR, "TemoriaDiscMod.java"), 'w', encoding='utf-8') as f:
    f.write(java_mod)

print("Generated all files successfully.")
