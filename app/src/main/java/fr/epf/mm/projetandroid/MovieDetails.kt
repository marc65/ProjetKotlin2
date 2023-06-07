package fr.epf.mm.projetandroid

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    val overview: String,
    val popularity: Float,
    @SerializedName("production_companies")
    val productionCompanies: List<Map<String, String>>,
    @SerializedName("release_date")
    val releaseDate: String,
    val runtime: Int,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("belongs_to_collection")
    val collection: Collection?
    ){
    data class Collection(
        val id: Int,
        val name: String,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("backdrop_path")
        val backdropPath: String?
    )
}

