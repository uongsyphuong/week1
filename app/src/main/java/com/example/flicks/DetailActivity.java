package com.example.flicks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flicks.api.ApiModule;
import com.example.flicks.api.ApiService;
import com.example.flicks.entity.Videos;
import com.example.flicks.mapper.DataMapper;
import com.example.flicks.model.Movie;
import com.example.flicks.model.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DetailActivity extends YouTubeBaseActivity {

    ImageView img_poster_detail;
    TextView tv_title;
    TextView tv_release_date;
    TextView tv_detail_release_date;
    RatingBar rating_bar;
    TextView tv_overview_detail;
    YouTubePlayerView youtube_player;
    TextView tv_rating;

    protected List<Trailer> trailersList;
    protected DataMapper mapper;
    protected Movie movie;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        img_poster_detail = findViewById(R.id.img_poster_detail);
        tv_title = findViewById(R.id.tv_title);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_detail_release_date = findViewById(R.id.tv_detail_release_date);
        rating_bar = findViewById(R.id.rating_bar);
        tv_overview_detail = findViewById(R.id.tv_overview_detail);
        youtube_player = findViewById(R.id.youtube_player);
        tv_rating = findViewById(R.id.tv_rating);

        // lấy dữ liệu từ bên màn hình cũ
        Bundle bundle = getIntent().getExtras();
        movie = (Movie) (bundle != null ? bundle.getParcelable("movie") : null);

        setupData();
        getTrailer();
    }

    private void getTrailer() {

        mapper = new DataMapper();

        // lấy link video trailer youtube
        ApiModule.getInstance().getVideo(movie.getId(), ApiService.API_KEY)
                .enqueue(new Callback<Videos>() {
                    @Override
                    //gọi thành công
                    public void onResponse(Call<Videos> call, Response<Videos> response) {
                        if(response.body() != null){
                            trailersList = mapper.transform(response.body());
                            String url = trailersList.get(0).getKey();
                            youtubePlayer(url);

                        }
                    }
                    @Override
                    //gọi thất bại
                    public void onFailure(Call<Videos> call, Throwable t) {
                    }
                });

    }

    private void youtubePlayer(final String key) {
        //setup link video để chạy youtube
        youtube_player.initialize(ApiService.API_KEY_YOUTUBE,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    //set link thành công
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        // nếu rating phim > 7.5 thì play video luôn không thì chỉ load
                        if (movie.getVoteAverage() >= ApiService.AVER_RATING) {
                            youTubePlayer.loadVideo(key); // chạy video
                            youTubePlayer.setShowFullscreenButton(false);
                        } else youTubePlayer.cueVideo(key); //load video
                    }

                    @Override
                    //load video thất bại
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void setupData() {

        //set dữ liệu phim vào từng view
        tv_title.setText(movie.getTitle());
        rating_bar.setRating(movie.getVoteAverage().floatValue());
        rating_bar.setIsIndicator(true);
        tv_detail_release_date.setText(movie.getReleaseDate());
        tv_overview_detail.setText(movie.getOverview());
        tv_rating.setText(movie.getVoteAverage().toString());

        // xài thư viện Glide để load hình
        Glide.with(DetailActivity.this).load(movie.getPosterPath()).apply(new RequestOptions()
                .fitCenter())
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(img_poster_detail);

    }
}
