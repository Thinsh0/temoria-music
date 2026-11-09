package com.temoria.disc;

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
