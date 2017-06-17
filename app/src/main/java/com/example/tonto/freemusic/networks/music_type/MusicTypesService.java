package com.example.tonto.freemusic.networks.music_type;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tonto on 5/23/2017.
 */

public interface MusicTypesService {
    @GET("WebObjects/MZStoreServices.woa/ws/genres")
    Call<MusicHolder> getMusicTypes();
}
