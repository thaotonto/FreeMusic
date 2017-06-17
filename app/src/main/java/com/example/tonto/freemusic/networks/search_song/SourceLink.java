package com.example.tonto.freemusic.networks.search_song;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonto on 5/30/2017.
 */

public class SourceLink {
    @SerializedName("128")
    private String linkSource;

    public String getLinkSource() {
        return linkSource;
    }

    public void setLinkSource(String linkSource) {
        this.linkSource = linkSource;
    }
}
