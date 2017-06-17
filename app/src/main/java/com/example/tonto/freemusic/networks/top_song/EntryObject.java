package com.example.tonto.freemusic.networks.top_song;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tonto on 5/30/2017.
 */

public class EntryObject {
    @SerializedName("im:name")
    private NameObject nameObject;
    @SerializedName("im:image")
    private List<ImageObject> imageObjects;
    @SerializedName("im:artist")
    private ArtistObject artistObject;

    public NameObject getNameObject() {
        return nameObject;
    }

    public void setNameObject(NameObject nameObject) {
        this.nameObject = nameObject;
    }

    public List<ImageObject> getImageObjects() {
        return imageObjects;
    }

    public void setImageObjects(List<ImageObject> imageObjects) {
        this.imageObjects = imageObjects;
    }

    public ArtistObject getArtistObject() {
        return artistObject;
    }

    public void setArtistObject(ArtistObject artistObject) {
        this.artistObject = artistObject;
    }
}
