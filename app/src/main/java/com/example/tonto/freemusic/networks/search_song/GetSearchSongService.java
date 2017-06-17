package com.example.tonto.freemusic.networks.search_song;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tonto on 5/30/2017.
 */

public interface GetSearchSongService {
    @GET("http://api.mp3.zing.vn/api/mobile/search/song")
    Call<MainObject> getSearchSong(@Query("requestdata") String request);
}
