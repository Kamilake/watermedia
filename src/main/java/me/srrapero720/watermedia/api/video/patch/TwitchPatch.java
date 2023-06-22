package me.srrapero720.watermedia.api.video.patch;

import me.srrapero720.watermedia.api.video.patch.util.twitch.TwitchUtil;
import org.jetbrains.annotations.NotNull;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;

// Planned
public class TwitchPatch extends AbstractURLPatch {

    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    @Override
    public boolean isValid(@NotNull URL url) {
        return (url.getHost().equals("www.twitch.tv") || url.getHost().equals("twitch.tv")) && url.getPath().startsWith("/");
    }

    @Override
    public String build(@NotNull URL url) throws PatchingUrlException {
        super.build(url);
        try {
            return TwitchUtil.getStream(url.getPath().substring(1)).get(0).getUrl();
        } catch (Exception e) {
            throw new PatchingUrlException(url, e);
        }
    }
}
