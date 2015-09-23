package is.varun.app.popularmovies;

import java.net.URI;

import is.varun.app.popularmovies.Model.TMDBMovieDetailsRetrofitObj;
import is.varun.app.popularmovies.Model.TMDBMovieListRetrofitObj;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 *
 * @author Varun D
 */

public interface TMDBApiRetrofit {

    /** This HTTP URI gets a list of at least 20 movies as provided by default TMDB pagination
     *
     * @param key is the API KEY
     * @param sortBy parameter to define how results must be sorted, ex: "popularity.desc"
     * @return list of movies
     */
    @GET("/3/discover/movie")
    TMDBMovieListRetrofitObj getMovies(

            // Param used for {api_key}
            @Query("api_key") String key,

            // Param sort_by usually used for popularity.desc
            @Query("sort_by") String sortBy );



    /** This HTTP URI gets reviews and videos for a {movie_id) using append_to_respond
     * We avoid making multiple calls to /reviews and /trailers
     *
     * @param movieID of the movie of type String ex. "76341"
     * @param key is the API KEY
     * @param commaSepList are the extra info that you want to request like "reviews,trailers"
     * @return movie details (with reviews and trailers appended)
     */

    @GET("/3/movie/{movie_id}")
    TMDBMovieDetailsRetrofitObj getMovieDetails(

            @Path("movie_id") String movieID,

            // Param used for {api_key}
            @Query("api_key") String key,

            // Param sort_by usually used for popularity.desc
            @Query("append_to_response") String commaSepList );
}
