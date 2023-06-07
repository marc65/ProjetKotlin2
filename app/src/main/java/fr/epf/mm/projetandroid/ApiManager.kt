package fr.epf.mm.projetandroid
import TmdbApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val tmdbApiService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}
