package com.example.tonto.freemusic.networks.music_type;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tonto on 5/23/2017.
 */

public class MusicTypes {
    @SerializedName("name")
    private String translation_key;
    private String id;

    public MusicTypes(String id, String translation_key) {
        this.id = id;
        this.translation_key = translation_key;
    }

    public String getId() {
        return id;
    }

    public String getTranslation_key() {
        return translation_key;
    }

    @Override
    public String toString() {
        return "MusicTypes{" +
                "id='" + id + '\'' +
                ", translation_key='" + translation_key + '\'' +
                '}';
    }
}
