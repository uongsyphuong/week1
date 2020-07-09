package com.example.flicks.entity

import com.example.flicks.model.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NowPlaying {
    @SerializedName("results")
    @Expose
    val results: List<Movie>? = null

    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null


}