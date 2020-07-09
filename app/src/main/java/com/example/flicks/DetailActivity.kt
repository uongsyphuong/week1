package com.example.flicks

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.flicks.api.ApiModule
import com.example.flicks.api.ApiService
import com.example.flicks.entity.Videos
import com.example.flicks.model.Movie
import com.example.flicks.model.Trailer
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : YouTubeBaseActivity() {
    var img_poster_detail: ImageView? = null
    var tv_title: TextView? = null
    var tv_release_date: TextView? = null
    var tv_detail_release_date: TextView? = null
    var rating_bar: RatingBar? = null
    var tv_overview_detail: TextView? = null
    var youtube_player: YouTubePlayerView? = null
    var tv_rating: TextView? = null
    private var trailersList: List<Trailer>? = null
    private var movie: Movie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        img_poster_detail = findViewById(R.id.img_poster_detail)
        tv_title = findViewById(R.id.tv_title)
        tv_release_date = findViewById(R.id.tv_release_date)
        tv_detail_release_date = findViewById(R.id.tv_detail_release_date)
        rating_bar = findViewById(R.id.rating_bar)
        tv_overview_detail = findViewById(R.id.tv_overview_detail)
        youtube_player = findViewById(R.id.youtube_player)
        tv_rating = findViewById(R.id.tv_rating)

        // lấy dữ liệu từ bên màn hình cũ
        val bundle = intent.extras
        movie = bundle?.getParcelable<Parcelable>("movie") as Movie?
        setupData()
        getTrailer()
    }

    // lấy link video trailer youtube
    private fun getTrailer() {
        // lấy link video trailer youtube
        ApiModule.instance?.getVideo(movie!!.id, ApiService.API_KEY)
                ?.enqueue(object : Callback<Videos?> {
                    //gọi thành công
                    override fun onResponse(call: Call<Videos?>, response: Response<Videos?>) {
                        if (response.body() != null) {
                            val url = response.body()?.results?.get(0)?.key
                            youtubePlayer(url)
                        }
                    }

                    override fun onFailure(call: Call<Videos?>, t: Throwable) {

                    }
                })
    }

    private fun youtubePlayer(key: String?) {
        //setup link video để chạy youtube
        youtube_player!!.initialize(ApiService.API_KEY_YOUTUBE,
                object : YouTubePlayer.OnInitializedListener {
                    //set link thành công
                    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                        // nếu rating phim > 7.5 thì play video luôn không thì chỉ load
                        if (movie!!.voteAverage >= ApiService.AVER_RATING) {
                            youTubePlayer.loadVideo(key) // chạy video
                            youTubePlayer.setShowFullscreenButton(false)
                        } else youTubePlayer.cueVideo(key) //load video
                    }

                    //load video thất bại
                    override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {}
                })
    }

    @SuppressLint("SetTextI18n")
    private fun setupData() {

        //set dữ liệu phim vào từng view
        tv_title!!.text = movie!!.title
        rating_bar!!.rating = movie!!.voteAverage.toFloat()
        rating_bar!!.setIsIndicator(true)
        tv_detail_release_date!!.text = movie!!.releaseDate
        tv_overview_detail!!.text = movie!!.overview
        tv_rating!!.text = movie!!.voteAverage.toString()

        // xài thư viện Glide để load hình
        Glide.with(this@DetailActivity).load(movie!!.posterUrl()).apply(RequestOptions()
                .fitCenter())
                .into(img_poster_detail!!)
    }
}