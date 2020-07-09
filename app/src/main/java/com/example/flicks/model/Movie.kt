package com.example.flicks.model

import android.os.Parcelable
import com.example.flicks.api.ApiService
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    var id: Int,
    @SerializedName("vote_average")
    var voteAverage: Double,
    var title: String?,
    @SerializedName("poster_path")
    var posterPath: String?,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    var overview: String?,
    @SerializedName("release_date")
    var releaseDate: String?
): Parcelable {

    fun posterUrl(): String? {
        if (posterPath == null) return null
        val stringBuilder = StringBuilder()
        stringBuilder.append(ApiService.BASE_IMAGES_URL)
        stringBuilder.append(ApiService.POSTER_SIZE)
        stringBuilder.append(posterPath)
        return stringBuilder.toString()
    }

    fun backdropUrl(): String? {
        if (backdropPath == null) return null
        val stringBuilder = StringBuilder()
        stringBuilder.append(ApiService.BASE_IMAGES_URL)
        stringBuilder.append(ApiService.BACKDROP_SIZE)
        stringBuilder.append(backdropPath)
        return stringBuilder.toString()
    }
}