package com.example.tonto.freemusic.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tonto.freemusic.R;
import com.example.tonto.freemusic.databases.models.TopSongModel;
import com.example.tonto.freemusic.events.OnClickMiniPlayer;
import com.example.tonto.freemusic.events.OnClickTopSong;
import com.example.tonto.freemusic.managers.MusicManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPlayerFragment extends Fragment {
    @BindView(R.id.iv_blur)
    ImageView ivBlur;
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.tv_song)
    TextView tvSong;
    @BindView(R.id.tv_singer)
    TextView tvSinger;
    @BindView(R.id.fb_main)
    FloatingActionButton fbMain;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.sb_main_1)
    SeekBar sbMain1;
    @BindView(R.id.sb_main_2)
    SeekBar sbMain2;


    public MainPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_player, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Subscribe(sticky = true)
    public void onReceivedTopsong(OnClickMiniPlayer onClickMiniPlayer) {
        TopSongModel topSongModel = onClickMiniPlayer.getTopSongModel();

        updateUIMainPlayer(topSongModel);
    }

    public void updateUIMainPlayer(TopSongModel topSongModel) {
        sbMain1.setPadding(0, 0, 0, 0);
        sbMain2.setPadding(0, 0, 0, 0);
        tvSinger.setText(topSongModel.getArtist());
        tvSong.setText(topSongModel.getSongName());

        Picasso.with(getContext()).load(topSongModel.getLargeImage()).transform(new BlurTransformation(getContext(), 5)).into(ivBlur);
        Picasso.with(getContext()).load(topSongModel.getLargeImage()).into(ivMain);


        fbMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicManager.playOrPause();
            }
        });
        MusicManager.updateSongRealTime(fbMain, sbMain1, sbMain2, tvCurrent, tvDuration);
    }

}
