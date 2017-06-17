package com.example.tonto.freemusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonto.freemusic.R;
import com.example.tonto.freemusic.databases.models.TopSongModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by tonto on 5/30/2017.
 */

public class TopSongAdapter extends RecyclerView.Adapter<TopSongAdapter.ViewHolder> {
    private Context context;
    private View.OnClickListener onClickListener;
    private List<TopSongModel> topSongModelList;

    public TopSongAdapter(Context context, List<TopSongModel> topSongModelList) {
        this.context = context;
        this.topSongModelList = topSongModelList;
    }

    public void setOnItemClick(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_top_songs, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(topSongModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return topSongModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_top_song_item)
        ImageView ivSong;
        @BindView(R.id.tv_song_name)
        TextView tvSongName;
        @BindView(R.id.tv_artist_name)
        TextView tvArtist;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(TopSongModel topSongModel) {

            Picasso.with(context).load(topSongModel.getSmallImage()).transform(new CropCircleTransformation()).into(ivSong);
            tvSongName.setText(topSongModel.getSongName());
            tvArtist.setText(topSongModel.getArtist());
        }
    }
}
