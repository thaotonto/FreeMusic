package com.example.tonto.freemusic.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tonto.freemusic.R;
import com.example.tonto.freemusic.adapters.MusicTypeAdapter;
import com.example.tonto.freemusic.databases.models.MusicTypeModel;
import com.example.tonto.freemusic.events.OnClickMusicTypeModel;
import com.example.tonto.freemusic.managers.ScreenManager;
import com.example.tonto.freemusic.networks.music_type.MediaTypes;
import com.example.tonto.freemusic.networks.music_type.MusicHolder;
import com.example.tonto.freemusic.networks.music_type.MusicTypes;
import com.example.tonto.freemusic.networks.music_type.MusicTypesService;
import com.example.tonto.freemusic.networks.RetrofitFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicTypesFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rv_music_types)
    RecyclerView rvMusicTypes;
    List<MusicTypeModel> musicTypeModelList = new ArrayList<>();;
    private MusicTypeAdapter musicTypeAdapter;

    public MusicTypesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_types, container, false);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);

        musicTypeAdapter = new MusicTypeAdapter(musicTypeModelList, getContext());

        rvMusicTypes.setAdapter(musicTypeAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });

        rvMusicTypes.setLayoutManager(gridLayoutManager);
        musicTypeAdapter.setOnItemClickListener(this);
    }

    private void loadData() {
        RetrofitFactory.getInstance().createService(MusicTypesService.class).getMusicTypes().enqueue(new Callback<MusicHolder>() {
            @Override
            public void onResponse(Call<MusicHolder> call, Response<MusicHolder> response) {
                if (response.code() == 200) {
                    System.out.println("its work");
                    MediaTypes mediaTypes = response.body().getMediaTypes();
                    List<MusicTypes> musicTypesList = mediaTypes.getSubgenres().getMusicTypesList();
                    for (MusicTypes musicTypes : musicTypesList) {
                        MusicTypeModel musicTypeModel = new MusicTypeModel();
                        musicTypeModel.setId(musicTypes.getId());
                        musicTypeModel.setKey(musicTypes.getTranslation_key());
                        Log.d(TAG, "onResponse: " + musicTypes);
                        musicTypeModel.setIdImage(getResources().getIdentifier("genre_x2_" + musicTypes.getId(), "raw", getActivity().getPackageName()));
                        if (!(musicTypes.getId().equals(10000) || musicTypes.getId().equals(1197) || musicTypes.getId().equals(1289) ||
                                musicTypes.getId().equals(1290) || musicTypes.getId().equals(1291)))
                            musicTypeModelList.add(musicTypeModel);
                    }
                    musicTypeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MusicHolder> call, Throwable t) {
                System.out.println("fail");
                MusicTypeModel musicTypeModel = new MusicTypeModel();
                musicTypeModel.setId("");
                musicTypeModel.setKey("All");
                musicTypeModel.setIdImage(getResources().getIdentifier("genre_x2_" + musicTypeModel.getId(), "raw", getActivity().getPackageName()));
                musicTypeModelList.add(musicTypeModel);
                musicTypeAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT);
            }
        });


    }

    @Override
    public void onClick(View v) {
        MusicTypeModel musicTypeModel = (MusicTypeModel) v.getTag();

        ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new TopSongsFragment(), R.id.rl_container, true, false);

        EventBus.getDefault().postSticky(new OnClickMusicTypeModel(musicTypeModel));
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
