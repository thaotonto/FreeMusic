package com.example.tonto.freemusic.networks.music_type;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonto on 6/15/2017.
 */

public class MusicHolder {
    @SerializedName("34")
    private MediaTypes mediaTypes;

    public MediaTypes getMediaTypes() {
        return mediaTypes;
    }

    public MusicHolder(MediaTypes mediaTypes) {
        this.mediaTypes = mediaTypes;
    }
}
