import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.mm.projetandroid.MainActivity
import fr.epf.mm.projetandroid.Movie
import fr.epf.mm.projetandroid.R

class MovieAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var onItemClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        holder.favoriteCheckBox.setOnCheckedChangeListener(null)
        holder.favoriteCheckBox.isChecked = movie.isFavorite

        holder.favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            movie.isFavorite = isChecked
            (holder.itemView.context as MainActivity).updateFavoriteMovies()
        }
    }


    override fun getItemCount(): Int {
        return movies.size
    }

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
        val favoriteCheckBox: CheckBox = itemView.findViewById(R.id.favoriteCheckBox)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = movies[position]
                    onItemClickListener?.invoke(movie)
                }
            }
        }

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
            val voteAverage = movie.voteAverage ?: 0f
            ratingBar.rating = voteAverage / 2
            val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // Image de remplacement en cas de chargement ou d'erreur
                .into(posterImageView)

            favoriteCheckBox.setOnCheckedChangeListener(null)
            favoriteCheckBox.isChecked = movie.isFavorite

            favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
                movie.isFavorite = isChecked
                // Ne mettez pas à jour les films favoris ici
            }
        }
    }
    fun setData(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
    //fun setFavoriteMovies(favoriteMovies: List<Movie>) {
    //    this.favoriteMovies = favoriteMovies
    //    notifyDataSetChanged()
    //}


}
