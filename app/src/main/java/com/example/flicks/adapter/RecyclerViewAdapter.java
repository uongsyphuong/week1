package com.example.flicks.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flicks.R;
import com.example.flicks.api.ApiService;
import com.example.flicks.model.Movie;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> mData;
    private IClickListener listener;

    public RecyclerViewAdapter(Context mContext, List<Movie> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setData(List<Movie> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getVoteAverage() >= ApiService.AVER_RATING)
            return 1;
        else
            return 0;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == 1) {
            view = inflater.inflate(R.layout.movie_row_item_popular, parent, false);
        } else view = inflater.inflate(R.layout.movie_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            Glide.with(mContext).load(mData.get(position).getBackdropPath()).apply(new RequestOptions()
                    .fitCenter())
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(holder.img_backdrop_popular);
        } else {
            holder.tv_name.setText(mData.get(position).getTitle());
            holder.tv_overview.setText(mData.get(position).getOverview());
            Glide.with(mContext).load(mData.get(position).getPosterPath()).apply(new RequestOptions()
                    .fitCenter())
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(holder.img_poster);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface IClickListener {
        void onItemClick(Movie movie);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        TextView tv_overview;
        ImageView img_poster;
        ImageView img_backdrop_popular;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.rowname);
            tv_overview = itemView.findViewById(R.id.overview);
            img_poster = itemView.findViewById(R.id.poster);
            img_backdrop_popular = itemView.findViewById(R.id.backdrop_popular);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(mData.get(getAdapterPosition()));
        }
    }

    public void setListener(IClickListener listener) {
        this.listener = listener;
    }
}
