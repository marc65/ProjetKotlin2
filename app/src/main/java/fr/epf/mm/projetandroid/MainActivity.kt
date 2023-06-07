package fr.epf.mm.projetandroid
//import DetailsFilmActivity
import MovieAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.projetandroid.ApiManager
import fr.epf.mm.projetandroid.Movie
import fr.epf.mm.projetandroid.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: Button
    private var movies: List<Movie> = emptyList()
    private val handler = Handler(Looper.getMainLooper())
    private var favoriteMovies: MutableList<Movie> = mutableListOf()
    private lateinit var adapter: MovieAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch = findViewById(R.id.buttonSearch)

        loadFavorites()

        buttonSearch.setOnClickListener {
            val query = editTextSearch.text.toString()
            searchMovies(query)
        }
        //updateFavoriteMovies()
    }

    private fun searchMovies(query: String) {
        val apiKey = "0b96ae7ab4d6f3ff74468ec58e787def" // Votre clé API TMDB
        val service = ApiManager.tmdbApiService

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = service.searchMovies(apiKey, query)
                if (response.isSuccessful) {
                    val searchResult = response.body()
                    val movies = searchResult?.results
                    if (movies != null) {
                        withContext(Dispatchers.Main) {
                            showMovies(movies)
                        }
                    } else {
                        // Gérer le cas où la liste de films est null
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

    private fun showMovies(movies: List<Movie>) {
        this.movies = movies
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(movies)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { movie ->
            val intent = Intent(this@MainActivity, DetailsFilmActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            intent.putExtra("favorite_movies", ArrayList(movies.filter { it.isFavorite }))
            startActivity(intent)
        }
    }

    fun showFavorites(view: View) {
        //val favoriteMovies = movies.filter { it.isFavorite }
        val intent = Intent(this, FavoriteActivity::class.java)
        intent.putExtra("favorite_movies", ArrayList(favoriteMovies))
        startActivity(intent)
        //val favoriteMovies = intent.getSerializableExtra("favorite_movies") as? ArrayList<Movie>
        //val intent = Intent(this, FavoriteActivity::class.java)
        //intent.getSerializableExtra("favorite_movies") as? ArrayList<Movie>
        //startActivity(intent)
    }

    fun updateFavoriteMovies() {
        favoriteMovies.clear()
        favoriteMovies.addAll(movies.filter { it.isFavorite })

        // Mettre à jour la liste des films favoris dans le thread principal
        handler.post {
            adapter.notifyDataSetChanged()
            saveFavorites()
        }
    }
    private fun saveFavorites() {
        val favoriteIds = favoriteMovies.map { it.id } // Récupérer les identifiants des films favoris
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("favoriteIds", favoriteIds.map { it.toString() }.toSet()) // Enregistrer les identifiants dans les préférences partagées
        editor.apply()
    }
    private fun loadFavorites() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val favoriteIds = sharedPreferences.getStringSet("favoriteIds", emptySet()) ?: emptySet()
        val favoriteMovieIds = favoriteIds.mapNotNull { it.toIntOrNull() }
        favoriteMovies.addAll(movies.filter { it.id in favoriteMovieIds })
        favoriteMovies.forEach { movie ->
            movie.isFavorite = true
        }
    }
}



