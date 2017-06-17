package com.example.tonto.freemusic.events;

import com.example.tonto.freemusic.databases.models.TopSongModel;

/**
 * Created by tonto on 6/11/2017.
 */

public class LoadUIPlayer {
    private TopSongModel topSongModel;

    public LoadUIPlayer(TopSongModel topSongModel) {
        this.topSongModel = topSongModel;
    }

    public TopSongModel getTopSongModel() {
        return topSongModel;
    }
}
