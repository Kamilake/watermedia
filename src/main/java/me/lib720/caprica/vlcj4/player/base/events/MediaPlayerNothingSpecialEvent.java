/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package me.lib720.caprica.vlcj4.player.base.events;

import me.lib720.caprica.vlcj4.player.base.MediaPlayer;
import me.lib720.caprica.vlcj4.player.base.MediaPlayerEventListener;

/**
 * Encapsulation of a media player nothing special event.
 */
final class MediaPlayerNothingSpecialEvent extends MediaPlayerEvent {

    MediaPlayerNothingSpecialEvent(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        // This event will never fire
    }

}