package com.example.usphuong.flicks.api;


import com.example.usphuong.flicks.entity.NowPlaying;
import com.example.usphuong.flicks.entity.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String API_KEY = "bb65eb0482ae97c522075b1fadd87d61";
    String API_KEY_YOUTUBE = "AIzaSyC02zjRvLlIC7-ii0rMdWBEsZ1QyNLpuL8";
    String BASE_URL = "http://api.themoviedb.org";
    String BASE_IMAGES_URL = "http://image.tmdb.org/t/p/";
    String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    String POSTER_SIZE = "w200";
    String BACKDROP_SIZE = "w400";
    Double AVER_RATING = 7.5;

    @GET("/3/movie/now_playing")
    Call<NowPlaying> getNowPlaying(@Query("api_key") String API_KEY);

    @GET("/3/movie/{movie_id}/videos")
    Call<Videos> getVideo(@Path("movie_id") int id,
                          @Query("api_key") String API_KEY);
}
