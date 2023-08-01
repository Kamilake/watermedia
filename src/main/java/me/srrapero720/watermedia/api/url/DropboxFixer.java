package me.srrapero720.watermedia.api.url;

import java.net.URL;

public class DropboxFixer extends AbstractFixer {
    @Override
    public boolean isValid(URL url) {
        String q;
        return url.getHost().contains("dropbox.com") && ((q = url.getQuery()) != null) && q.contains("dl=0");
    }

    @Override
    public URL patch(URL url) throws PatchingUrlException {
        super.patch(url);
        try {
            return new URL(url.toString().replace("dl=0", "dl=1"));
        } catch (Exception e) {
            throw new PatchingUrlException(url, e);
        }
    }
}