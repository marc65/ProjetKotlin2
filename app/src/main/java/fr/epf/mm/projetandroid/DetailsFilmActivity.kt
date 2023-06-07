package fr.epf.mm.projetandroid
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import fr.epf.mm.projetandroid.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import fr.epf.mm.projetandroid.ApiManager
import fr.epf.mm.projetandroid.Movie


class DetailsFilmActivity : AppCompatActivity() {

    private lateinit var movieTitleTextView: TextView
    private lateinit var originalLanguageTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var popularityTextView: TextView
    private lateinit var productionCompaniesTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var voteAverageTextView: TextView
    private lateinit var posterImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_film)

        movieTitleTextView = findViewById(R.id.movieTitleTextView)
        originalLanguageTextView = findViewById(R.id.originalLanguageTextView)
        overviewTextView = findViewById(R.id.overviewTextView)
        popularityTextView = findViewById(R.id.popularityTextView)
        productionCompaniesTextView = findViewById(R.id.productionCompaniesTextView)
        releaseDateTextView = findViewById(R.id.releaseDateTextView)
        durationTextView = findViewById(R.id.durationTextView)
        voteAverageTextView = findViewById(R.id.voteAverageTextView)
        posterImageView = findViewById(R.id.posterImageView)


        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId != -1) {
            fetchMovieDetails(movieId)
        } else {
            // Gérer le cas où l'ID du film n'est pas valide
        }
    }

    private fun fetchMovieDetails(movieId: Int) {
        val apiKey = "0b96ae7ab4d6f3ff74468ec58e787def" // Votre clé API TMDB
        val service = ApiManager.tmdbApiService

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = service.getMovieDetails(movieId, apiKey)
                if (response.isSuccessful) {
                    val movie = response.body()
                    if (movie != null) {
                        withContext(Dispatchers.Main) {
                            showMovieDetails(movie)
                        }
                    } else {
                        // Gérer le cas où les détails du film sont null
                    }
                } else {
                    // Gérer les erreurs de la requête
                    val errorMessage = response.message()
                    // Afficher ou traiter l'erreur
                }
            } catch (e: Exception) {
                // Gérer les exceptions
                e.printStackTrace()
            }
        }
    }

    private fun showMovieDetails(movie: MovieDetails) {
        movieTitleTextView.text = movie.title
        originalLanguageTextView.text = "Original Language: ${movie.originalLanguage}"
        overviewTextView.text = "Overview: ${movie.overview}"
        popularityTextView.text = "Popularity: ${movie.popularity}"
        productionCompaniesTextView.text = "Production Companies: ${getProductionCompaniesString(movie.productionCompanies)}"
        releaseDateTextView.text = "Release Date: ${movie.releaseDate}"
        durationTextView.text = "Duration: ${movie.runtime} min"
        voteAverageTextView.text = "Vote Average: ${movie.voteAverage}"
        val collection = movie.collection
        val posterPath = collection?.posterPath
        if (posterPath != null) {
            val posterUrl = "https://image.tmdb.org/t/p/w500$posterPath"
            Glide.with(this@DetailsFilmActivity)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .into(posterImageView)
        } else {
            // Utilisez une image de remplacement si le chemin de l'affiche est indisponible
            posterImageView.setImageResource(R.drawable.placeholder)
        }
    }
    private fun getProductionCompaniesString(companies: List<Map<String, String>>): String {
        val stringBuilder = StringBuilder()
        for (company in companies) {
            val companyName = company["name"]
            if (!companyName.isNullOrEmpty()) {
                stringBuilder.append(companyName)
                stringBuilder.append(", ")
            }
        }
        if (stringBuilder.isNotEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length - 2)
        }
        return stringBuilder.toString()
    }

}
