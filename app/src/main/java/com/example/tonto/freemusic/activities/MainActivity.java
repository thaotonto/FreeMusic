package com.example.tonto.freemusic.activities;

import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tonto.freemusic.R;
import com.example.tonto.freemusic.adapters.PagerAdapter;
import com.example.tonto.freemusic.databases.models.TopSongModel;
import com.example.tonto.freemusic.events.LoadUIPlayer;
import com.example.tonto.freemusic.events.OnClickMiniPlayer;
import com.example.tonto.freemusic.events.OnClickTopSong;
import com.example.tonto.freemusic.fragments.MainPlayerFragment;
import com.example.tonto.freemusic.managers.MusicManager;
import com.example.tonto.freemusic.managers.ScreenManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TopSongModel topSongModel;
    @BindView(R.id.tb_main)
    Toolbar tbMain;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.rl_miniplayer)
    RelativeLayout rlMiniPlayer;
    @BindView(R.id.sb_miniplayer)
    SeekBar sbMini;
    @BindView(R.id.iv_miniplayer)
    ImageView ivMini;
    @BindView(R.id.fb_miniplayer)
    FloatingActionButton fbMini;
    @BindView(R.id.tv_mini_singer)
    TextView tvMiniSinger;
    @BindView(R.id.tv_mini_song)
    TextView tvMiniSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        EventBus.getDefault().register(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupUI() {
        ButterKnife.bind(this);

        tbMain.setTitle("Free Music");
        tbMain.setTitleTextColor(getResources().getColor(R.color.primary_text));

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_dashboard_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_file_download_black_24dp));

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.icon_selected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(getResources().getColor(R.color.icon_selected), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sbMini.setPadding(0, 0, 0, 0);
        fbMini.setOnClickListener(this);
        rlMiniPlayer.setOnClickListener(this);
    }

    @Subscribe
    public void onTopSongClick(OnClickTopSong onClickTopSong) {
        rlMiniPlayer.setVisibility(View.VISIBLE);
        TopSongModel topSongModel = onClickTopSong.getTopSongModel();
        MusicManager.loadSearchSong(this, topSongModel);

    }

    @Subscribe
    public void onLoadMiniPlayerUI(LoadUIPlayer loadUIPlayer) {
        topSongModel = loadUIPlayer.getTopSongModel();
        tvMiniSong.setText(topSongModel.getSongName());
        tvMiniSinger.setText(topSongModel.getArtist());
        Picasso.with(this).load(topSongModel.getSmallImage()).transform(new CropCircleTransformation()).into(ivMini);

        MusicManager.updateSongRealTime(fbMini, sbMini, null, null, null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_miniplayer) {
            MusicManager.playOrPause();
        } else {
            EventBus.getDefault().postSticky(new OnClickMiniPlayer(topSongModel));
            ScreenManager.openFragment(getSupportFragmentManager(), new MainPlayerFragment(), R.id.rl_main, true, true);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();

        } else {
            moveTaskToBack(true);
        }

    }
}
