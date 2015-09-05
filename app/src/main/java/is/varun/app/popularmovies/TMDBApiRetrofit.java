package is.varun.app.popularmovies;

import is.varun.app.popularmovies.Model.TMDBMovieListObj;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 *
 * @author Varun D
 */

// TODO Modify this to match TMDBMovies after modifying TMDBMovie!

public interface TMDBApiRetrofit {

    // This HTTP URI gets a list of at least 20 movies as provided by default TMDB pagination
    @GET("/3/discover/movie")
    TMDBMovieListObj getMovies(

            // Param used for {api_key}
            @Query("api_key") String key,

            // Param sort_by usually used for popularity.desc
            @Query("sort_by") String sortBy );
}
