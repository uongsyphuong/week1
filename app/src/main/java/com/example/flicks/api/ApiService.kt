package com.example.flicks.api

import com.example.flicks.entity.NowPlaying
import com.example.flicks.entity.Videos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //lấy phim
    @GET("/3/movie/now_playing")
    fun getNowPlaying(@Query("api_key") API_KEY: String?): Call<NowPlaying?>

    //lấy video để chạy youtube
    @GET("/3/movie/{movie_id}/videos")
    fun getVideo(@Path("movie_id") id: Int,
                 @Query("api_key") API_KEY: String?): Call<Videos?>?

    companion object {
        const val API_KEY = "bb65eb0482ae97c522075b1fadd87d61"
        const val API_KEY_YOUTUBE = "AIzaSyC02zjRvLlIC7-ii0rMdWBEsZ1QyNLpuL8"
        const val BASE_URL = "http://api.themoviedb.org"
        const val BASE_IMAGES_URL = "http://image.tmdb.org/t/p/"
        const val BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v="
        const val POSTER_SIZE = "w200"
        const val BACKDROP_SIZE = "w400"
        const val AVER_RATING = 7.5
    }
}