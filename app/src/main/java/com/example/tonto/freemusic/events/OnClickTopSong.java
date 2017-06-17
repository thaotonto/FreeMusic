package com.example.tonto.freemusic.events;

import com.example.tonto.freemusic.databases.models.TopSongModel;

/**
 * Created by tonto on 6/6/2017.
 */

public class OnClickTopSong {
    private TopSongModel topSongModel;

    public OnClickTopSong(TopSongModel topSongModel) {
        this.topSongModel = topSongModel;
    }

    public TopSongModel getTopSongModel() {
        return topSongModel;
    }
}
