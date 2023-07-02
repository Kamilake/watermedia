package me.srrapero720.watermedia.api.video.events.common;

import me.srrapero720.watermedia.api.video.VideoPlayer;

public interface MediaVolumeUpdateEvent<P extends VideoPlayer> extends Event<MediaVolumeUpdateEvent.EventData, P> {
    record EventData(int beforeVolume, int afterVolume) {}
}
