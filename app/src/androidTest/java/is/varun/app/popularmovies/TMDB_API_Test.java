package is.varun.app.popularmovies;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import is.varun.app.popularmovies.Model.TMDBMovieDetailsRetrofitObj;
import is.varun.app.popularmovies.Model.TMDBMovieListRetrofitObj;
import retrofit.RestAdapter;

/**
 * Test suite for TMDB Retrofit API testing
 */
public class TMDB_API_Test  extends ApplicationTestCase<Application> {

    public TMDB_API_Test() {
        super(Application.class);
    }

    private static final String LOG_TAG = "TEST LOG:";

    private final String TEST_URL = "https://api.themoviedb.org";

    private TMDBApiRetrofit TMDBservice = new RestAdapter.Builder()
            .setEndpoint(TEST_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build()
            .create(TMDBApiRetrofit.class);

//    public void testGetMovies() throws Exception {
//        TMDBMovieListRetrofitObj movies = TMDBservice.getMovies("bb2676cea1c31da46a38029b13b86eaf","popularity.desc");
//
//        /**
//         *  TODO // Need to supply this into a local ArrayList of TMDBMovie because that object
//         *  TODO // will also hold the reviews and stuff
//         */
//        Log.d(TEST_LOG, movies.page);
//        Log.d(TEST_LOG, Integer.toString(movies.results.size()));
//
//        TMDBMovieListRetrofitObj.MovieResult res = movies.results.get(1);
//
//        Log.d(TEST_LOG, res.id);
//        Log.d(TEST_LOG, res.original_title);
//        Log.d(TEST_LOG, res.overview);
//        Log.d(TEST_LOG, res.release_date);
//        Log.d(TEST_LOG, res.poster_path);
//    }

    /**
     * This function gets the remining information from the API like reviews and trailer details
     * @throws Exception
     */
    public void testGetMovieDetails() throws Exception {
        Log.d(LOG_TAG, "testGetMovieDetails");

        TMDBMovieDetailsRetrofitObj dataMovieDetails = TMDBservice.getMovieDetails("76341", "bb2676cea1c31da46a38029b13b86eaf", "reviews,trailers");


        Log.d(LOG_TAG, dataMovieDetails.runtime);
        Log.d(LOG_TAG, dataMovieDetails.tagline);
        Log.d(LOG_TAG, dataMovieDetails.reviews.total_results);

        if (Integer.parseInt(dataMovieDetails.reviews.total_results) > 1){
            Log.d(LOG_TAG, "We have reviews:" + Integer.toString( dataMovieDetails.reviews.results.size()) );
            TMDBMovieDetailsRetrofitObj.Reviews.ReviewResults rev = dataMovieDetails.reviews.results.get(1);

            Log.d(LOG_TAG,rev.content );
        }

        //TMDBMovieDetailsRetrofitObj.Reviews dataReviews = dataMovieDetails..get(1);

    }
}
