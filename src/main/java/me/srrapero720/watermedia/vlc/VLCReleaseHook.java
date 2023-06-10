package me.srrapero720.watermedia.vlc;

import me.lib720.caprica.vlcj.factory.MediaPlayerFactory;

import static me.srrapero720.watermedia.WaterMedia.LOGGER;

class VLCReleaseHook extends Thread {
    public final MediaPlayerFactory factory;
    VLCReleaseHook(MediaPlayerFactory factory) {
        super();
        this.setDaemon(true);
        this.setName("VLC-ReleaseHook");
        this.factory = factory;
    }

    @Override
    public void run() {
        LOGGER.info("Shutdown VLC");
        if (factory != null) factory.release();
        LOGGER.info("Shutdown finished");
    }
}