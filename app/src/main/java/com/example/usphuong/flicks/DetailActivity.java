package com.example.usphuong.flicks;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.usphuong.flicks.adapter.RecyclerViewAdapter;
import com.example.usphuong.flicks.api.ApiModule;
import com.example.usphuong.flicks.api.ApiService;
import com.example.usphuong.flicks.entity.NowPlaying;
import com.example.usphuong.flicks.entity.Videos;
import com.example.usphuong.flicks.mapper.DataMapper;
import com.example.usphuong.flicks.model.Movie;
import com.example.usphuong.flicks.model.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DetailActivity extends YouTubeBaseActivity {
    @BindView(R.id.img_poster_detail) ImageView img_poster_detail;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_detail_release_date) TextView tv_detail_release_date;
    @BindView(R.id.rating_bar) RatingBar rating_bar;
    @BindView(R.id.tv_overview_detail) TextView tv_overview_detail;
    @BindView(R.id.youtube_player) YouTubePlayerView youtube_player;
    @BindView(R.id.tv_rating) TextView tv_rating;
    protected List<Trailer> trailersList;
    protected DataMapper mapper;
    protected Movie movie;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_land_activity);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        movie = (Movie) (bundle != null ? bundle.getParcelable("movie") : null);


        setupData();
        getTrailer();
    }

    private void getTrailer() {

        mapper = new DataMapper();
        ApiModule.getInstance().getVideo(movie.getId(), ApiService.API_KEY)
                .enqueue(new Callback<Videos>() {
                    @Override
                    public void onResponse(Call<Videos> call, Response<Videos> response) {
                        if(response.body() != null){
                            trailersList = mapper.transform(response.body());
                            String url = trailersList.get(0).getKey();
                            youtubePlayer(url);

                        }
                    }

                    @Override
                    public void onFailure(Call<Videos> call, Throwable t) {
                    }
                });

    }

    private void youtubePlayer(final String key) {
        youtube_player.initialize(ApiService.API_KEY_YOUTUBE,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        if (movie.getVoteAverage() >= ApiService.AVER_RATING)
                            youTubePlayer.loadVideo(key); // .loadVideo: Play now - .cueVideo: only load
                        else youTubePlayer.cueVideo(key);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void setupData() {

        tv_title.setText(movie.getTitle());
        rating_bar.setRating(movie.getVoteAverage().floatValue());
        rating_bar.setIsIndicator(true);
        tv_detail_release_date.setText(movie.getReleaseDate());
        tv_overview_detail.setText(movie.getOverview());
        tv_rating.setText(movie.getVoteAverage().toString());

        Glide.with(DetailActivity.this).load(movie.getPosterPath()).apply(new RequestOptions()
                .fitCenter())
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(img_poster_detail);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.detail_land_activity);
            setupData();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.detail_activity);
            setupData();
        }
    }
}
