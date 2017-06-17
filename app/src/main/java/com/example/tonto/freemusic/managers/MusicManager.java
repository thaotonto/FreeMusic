package com.example.tonto.freemusic.managers;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonto.freemusic.R;
import com.example.tonto.freemusic.databases.models.TopSongModel;
import com.example.tonto.freemusic.events.LoadUIPlayer;
import com.example.tonto.freemusic.networks.RetrofitFactory;
import com.example.tonto.freemusic.networks.search_song.DocObject;
import com.example.tonto.freemusic.networks.search_song.GetSearchSongService;
import com.example.tonto.freemusic.networks.search_song.MainObject;
import com.example.tonto.freemusic.services.MusicNotification;
import com.example.tonto.freemusic.utils.FuzzyMatch;
import com.example.tonto.freemusic.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hybridmediaplayer.HybridMediaPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tonto on 5/30/2017.
 */

public class MusicManager {
    private static boolean isPrepared;
    public static HybridMediaPlayer hybridMediaPlayer;
    private static boolean keepUpdate;

    public static void loadSearchSong(final Context context, final TopSongModel topSongModel) {
        GetSearchSongService getSearchSongService = RetrofitFactory.getInstance().createService(GetSearchSongService.class);

        final String dataSong = topSongModel.getSongName() + " " + topSongModel.getArtist();
        String requestData = "{\"q\":\"" + dataSong + "\", \"sort\":\"hot\", \"start\":\"0\", \"length\":\"10\"}";
        getSearchSongService.getSearchSong(requestData).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                if (response.code() == 200) {
                    if (response.body().getDocs().size() != 0) {
                        List<Integer> ratioList = new ArrayList<Integer>();
                        for (DocObject docObject : response.body().getDocs()) {
                            int ratio = FuzzyMatch.getRatio(dataSong, docObject.getTitle() + " " + docObject.getArtist(), false);
                            ratioList.add(ratio);
                        }

                        for (int i = 0; i < ratioList.size(); i++) {
                            if (ratioList.get(i) == Collections.max(ratioList)) ;
                            {
                                topSongModel.setLinkSource(response.body().getDocs().get(i).getSource().getLinkSource());
                                playMusic(context, topSongModel);
                            }
                        }
                    } else {
                        Toast.makeText(context, "Not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });
    }

    public static void playMusic(final Context context, final TopSongModel topSongModel) {
        isPrepared = false;
        if (hybridMediaPlayer != null) {
            hybridMediaPlayer.release();
        }
        hybridMediaPlayer = HybridMediaPlayer.getInstance(context);

        hybridMediaPlayer.setDataSource(topSongModel.getLinkSource());
        hybridMediaPlayer.prepare();

        hybridMediaPlayer.setOnPreparedListener(new HybridMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(HybridMediaPlayer hybridMediaPlayer) {
                isPrepared = true;
                MusicNotification.setupNotification(context, topSongModel);
                hybridMediaPlayer.play();

                EventBus.getDefault().post(new LoadUIPlayer(topSongModel));
            }
        });
    }

    public static void playOrPause() {
        if (isPrepared) {
            if (hybridMediaPlayer.isPlaying()) {
                hybridMediaPlayer.pause();
            } else {
                hybridMediaPlayer.play();
            }
            MusicNotification.updateNotification(hybridMediaPlayer.isPlaying());
        }
    }

    public static void updateSongRealTime(final FloatingActionButton floatingActionButton, final SeekBar seekBar1,
                                          final SeekBar seekBar2, final TextView tvCurrent, final TextView tvDuration) {
        keepUpdate = true;
        final Handler handler = new Handler();
        Runnable autoUpdate = new Runnable() {
            @Override
            public void run() {
                if (keepUpdate) {
                    if (hybridMediaPlayer.isPlaying()) {
                        floatingActionButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    } else {
                        floatingActionButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }

                    seekBar1.setMax(hybridMediaPlayer.getDuration());
                    seekBar1.setProgress(hybridMediaPlayer.getCurrentPosition());


                    if (seekBar2 != null) {
                        seekBar2.setMax(hybridMediaPlayer.getDuration());
                        seekBar2.setProgress(hybridMediaPlayer.getCurrentPosition());
                    }

                    if (tvCurrent != null) {
                        tvCurrent.setText(Utils.convertTime(hybridMediaPlayer.getCurrentPosition()));
                        tvDuration.setText(Utils.convertTime(hybridMediaPlayer.getDuration()));
                    }
                }
                    handler.postDelayed(this, 100);

            }
        };

        autoUpdate.run();

        final int[] currentProgress = new int[2];
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress[0] = progress;
                if (seekBar2 != null) {
                    seekBar2.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                keepUpdate = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hybridMediaPlayer.seekTo(currentProgress[0]);
                keepUpdate = true;
            }
        });

        if (seekBar2 != null) {
            seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    currentProgress[1] = progress;
                    seekBar1.setProgress(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    keepUpdate = false;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    hybridMediaPlayer.seekTo(currentProgress[1]);
                    keepUpdate = true;
                }
            });
        }
    }
}
