package com.example.tonto.freemusic.networks.top_song;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tonto on 5/30/2017.
 */

public interface GetTopSongService {
    @GET("us/rss/topsongs/limit=50/genre={idmusictype}/explicit=true/json")
    Call<MainObject> getTopSongs(@Path("idmusictype") String idMusicType);
}
