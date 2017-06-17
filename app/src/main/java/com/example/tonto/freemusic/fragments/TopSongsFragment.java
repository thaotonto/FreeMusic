package com.example.tonto.freemusic.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonto.freemusic.R;
import com.example.tonto.freemusic.adapters.TopSongAdapter;
import com.example.tonto.freemusic.databases.models.MusicTypeModel;
import com.example.tonto.freemusic.databases.models.TopSongModel;
import com.example.tonto.freemusic.events.OnClickMusicTypeModel;
import com.example.tonto.freemusic.events.OnClickTopSong;
import com.example.tonto.freemusic.managers.MusicManager;
import com.example.tonto.freemusic.managers.ScreenManager;
import com.example.tonto.freemusic.networks.RetrofitFactory;
import com.example.tonto.freemusic.networks.top_song.EntryObject;
import com.example.tonto.freemusic.networks.top_song.Feed;
import com.example.tonto.freemusic.networks.top_song.GetTopSongService;
import com.example.tonto.freemusic.networks.top_song.MainObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopSongsFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.iv_music_type)
    ImageView ivMusicType;
    @BindView(R.id.tv_top_song)
    TextView tvMusicType;
    @BindView(R.id.rv_top_songs)
    RecyclerView rvTopSongs;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<TopSongModel> topSongModelList = new ArrayList<>();
    private TopSongAdapter topSongAdapter;

    public TopSongsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_songs, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        topSongAdapter = new TopSongAdapter(getContext(), topSongModelList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvTopSongs.setAdapter(topSongAdapter);
        rvTopSongs.setLayoutManager(manager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvTopSongs.addItemDecoration(dividerItemDecoration);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getActivity().getSupportFragmentManager());
            }
        });

        topSongAdapter.setOnItemClick(this);
    }

    @Subscribe(sticky = true)
    public void onReceivedMusicType(OnClickMusicTypeModel onClickMusicTypeModel) {
        MusicTypeModel musicTypeModel = onClickMusicTypeModel.getMusicTypeModel();

        tvMusicType.setText(musicTypeModel.getKey());
        ivMusicType.setImageResource(musicTypeModel.getIdImage());

        loadData(musicTypeModel);
    }

    private void loadData(MusicTypeModel musicTypeModel) {
        RetrofitFactory.createService(GetTopSongService.class).getTopSongs(musicTypeModel.getId()).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                Feed feed = mainObject.getFeed();
                for (EntryObject entry : feed.getEntryObjectList()) {
                    TopSongModel topSongModel = new TopSongModel();
                    topSongModel.setSongName(entry.getNameObject().getLabel());
                    topSongModel.setSmallImage(entry.getImageObjects().get(0).getLabel());
                    topSongModel.setLargeImage(entry.getImageObjects().get(2).getLabel());
                    topSongModel.setArtist(entry.getArtistObject().getLabel());

                    topSongModelList.add(topSongModel);
                }

                topSongAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int position = rvTopSongs.getChildLayoutPosition(v);
        TopSongModel topSongModel = topSongModelList.get(position);

        EventBus.getDefault().post(new OnClickTopSong(topSongModel));
    }
}
