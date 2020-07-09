package com.example.flicks.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Videos {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("results")
    @Expose
    var results: List<VideoListDTO>? = null

    /**
     * Small Video Data Transfer Object
     * Used to store a video list retrieved from a list of movies from TMDb
     */
    class VideoListDTO {
        var id: String? = null
        var iso6391: String? = null
        var iso31661: String? = null
        var key: String? = null
        var name: String? = null
        var site: String? = null
        var size: Int? = null
        var type: String? = null

    }
}