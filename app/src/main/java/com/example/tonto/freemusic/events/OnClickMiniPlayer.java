package com.example.tonto.freemusic.events;

import com.example.tonto.freemusic.databases.models.TopSongModel;

/**
 * Created by tonto on 6/11/2017.
 */

public class OnClickMiniPlayer {
    private TopSongModel topSongModel;

    public OnClickMiniPlayer(TopSongModel topSongModel) {
        this.topSongModel = topSongModel;
    }

    public TopSongModel getTopSongModel() {
        return topSongModel;
    }
}
