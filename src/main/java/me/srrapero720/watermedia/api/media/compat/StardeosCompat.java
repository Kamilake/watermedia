package me.srrapero720.watermedia.api.media.compat;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.srrapero720.watermedia.network.StardeosApi;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import static me.srrapero720.watermedia.WaterMedia.LOGGER;

// Planned
public class StardeosCompat extends CompatVideoUrl {
    private static final Pattern regex = Pattern.compile("\\/video\\/([a-zA-Z0-9]+)$");

    @Override
    public boolean isValid(@NotNull URL url) {
        return url.getPath().contains("stardeos.com/video/");
    }

    @Override
    public String build(@NotNull URL url) {
        super.build(url);

        // OBTEN EL VIDEO ID
        var matcher = regex.matcher(url.toString());
        if (!matcher.find()) return null;
        var videoId = matcher.group(1);

        // INICIA CONEXION CON EL SERVIDOR EXTERNO
        var call = StardeosApi.NET.getVideoInfo(videoId);
        try {
            var res = call.execute();
            if (res.isSuccessful() && res.body() != null) {
                for (var file: res.body().files) {
                    if (file.fileUrl.contains("high") && !file.alert) return file.fileUrl;
                }
            } else {
                LOGGER.error("Detected Stardeos.com but request to get real URL wasn't successfuly \n\nStatus code: {}\n\nResponse: {}", res.code(), res.body());
            }
        } catch (Exception e) {
            LOGGER.error("Detected Stardeos.com but request to get real URL wasn't successfuly");
        }

        return null;
    }
}
