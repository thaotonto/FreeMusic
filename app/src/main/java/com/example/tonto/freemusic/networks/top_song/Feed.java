package com.example.tonto.freemusic.networks.top_song;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tonto on 5/30/2017.
 */

public class Feed {
    @SerializedName("entry")
    private List<EntryObject> entryObjectList;

    public List<EntryObject> getEntryObjectList() {
        return entryObjectList;
    }

    public void setEntryObjectList(List<EntryObject> entryObjectList) {
        this.entryObjectList = entryObjectList;
    }
}
