package me.srrapero720.watermedia.core.videolan;


import me.lib720.caprica.vlcj.factory.MediaPlayerFactory;
import me.lib720.caprica.vlcj.factory.discovery.provider.CustomDirectoryProvider;
import me.srrapero720.watermedia.Util;
import me.srrapero720.watermedia.api.WaterMediaAPI;
import me.srrapero720.watermedia.api.external.ThreadUtil;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.util.zip.GZIPOutputStream;

import static me.srrapero720.watermedia.WaterMedia.LOGGER;

public class VideoLAN {
    protected static final Marker IT = MarkerFactory.getMarker("VideoLAN");
    private static MediaPlayerFactory factory;
    public static MediaPlayerFactory defaultFactory() { return factory; }

    public static boolean init(Path workingDir, Path gameDir) {
        if (factory != null) return true;

        // PATHS
        var logs = workingDir.resolve("logs/vlc/");
        var path = workingDir.resolve("vlc/");

        // LOGGER INIT
        if (!Files.exists(logs.toAbsolutePath())) logs.toFile().mkdirs();
        else compressAndDeleteLogFile(logs.resolve("latest.log"));

        // INIT
        CustomDirectoryProvider.init(path);
        VLCBinaries.init(path);

        // Check if we need to update binaries
        var version = VLCBinaries.installedVersion();
        boolean fresh = false;
        if (version == null || !version.equals(VLCBinaries.resVersion())) {
            // CLEAR
            LOGGER.warn(IT, "Running deletion for VLC Files");
            VLCBinaries.cleanup();

            // EXTRACT
            LOGGER.warn(IT, "Running extraction for VLC Files");
            for (var binary : VLCBinaries.values()) binary.extract();

            // SET LOCAL VERSION
            try {
                var config = path.resolve("version.cfg");
                if (!Files.exists(config.getParent())) Files.createDirectories(config.getParent());
                Files.writeString(config, VLCBinaries.resVersion());
            } catch (Exception e) {
                LOGGER.error(IT, "Could not write to configuration file", e);
            }
            fresh = true;
        } else LOGGER.warn(IT, "Detected WaterMedia's VLC installation");

        // Integrity check
        if (!fresh) {
            LOGGER.info(IT, "Running integrity check");
            for (var binary : VLCBinaries.values()) binary.checkIntegrity();
        }

        factory = ThreadUtil.tryAndReturnNull(
                defaultVar -> WaterMediaAPI.createVLCFactory(Util.getArrayStringFromRes("vlc/command-line.json")), e -> LOGGER.error(IT, "Failed to load VLC", e)
        );

        return factory != null;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void compressAndDeleteLogFile(Path logFilePath) {
        File logFile = logFilePath.toFile();
        if (!logFile.exists() || !logFile.isFile()) return;

        // Output for new gZIP
        var date = new Date(System.currentTimeMillis()).toLocalDate().toString();
        String compressedFilePath = logFile.getParent() + "/" + date + ".log.gz";

        int count = 0;
        while (new File(compressedFilePath).exists()) compressedFilePath = logFile.getParent() + "/" + date + "-" + (count++) + ".log.gz";

        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(compressedFilePath)); InputStream inputStream = new FileInputStream(logFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) gzipOutputStream.write(buffer, 0, bytesRead);
        } catch (Exception e) {
            LOGGER.error(IT, "Failed to compress logs/vlc/latest.log");
        }
        logFile.delete();
    }
}