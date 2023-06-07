package fr.epf.mm.projetandroid

import MovieAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteActivity  : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private var favoriteMovies: List<Movie> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(favoriteMovies)
        recyclerView.adapter = adapter

        val extras = intent.extras
        if (extras != null) {
            favoriteMovies = extras.getSerializable("favorite_movies") as? List<Movie> ?: emptyList()
            adapter.setData(favoriteMovies)
        }
    }
}