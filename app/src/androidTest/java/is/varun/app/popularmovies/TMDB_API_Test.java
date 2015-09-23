package is.varun.app.popularmovies;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
//    public void testGetMovieDetails() throws Exception {
//        Log.d(LOG_TAG, "testGetMovieDetails");
//
//        TMDBMovieDetailsRetrofitObj dataMovieDetails = TMDBservice.getMovieDetails("76341", "bb2676cea1c31da46a38029b13b86eaf", "reviews,trailers");
//
//
//        Log.d(LOG_TAG, dataMovieDetails.runtime);
//        Log.d(LOG_TAG, dataMovieDetails.tagline);
//        Log.d(LOG_TAG, dataMovieDetails.reviews.total_results);
//
//        // Testing reviews
//        if (Integer.parseInt(dataMovieDetails.reviews.total_results) > 1){
//            Log.d(LOG_TAG, "We have reviews:" + Integer.toString( dataMovieDetails.reviews.results.size()) );
//            TMDBMovieDetailsRetrofitObj.Reviews.ReviewResults rev = dataMovieDetails.reviews.results.get(1);
//
//            Log.d(LOG_TAG,rev.content );
//        }
//
//        // Testing Trailer content
//        if (dataMovieDetails.trailers != null){
//            TMDBMovieDetailsRetrofitObj.Trailers.YouTubeResults ytTrailers = dataMovieDetails.trailers.youtube.get(1);
//
//            Log.d(LOG_TAG, ytTrailers.source );
//        }
//
//    }

    // TEST 1: Calls to each ID is done within the main loop itself.
    public void testMakeTMDBObject() throws Exception {

        Log.d(LOG_TAG, "testMakeTMDBMovieObject");

        // This is called once to populate the initial data, i.e. posters.
        TMDBMovieListRetrofitObj movieReplyObject = TMDBservice.getMovies("bb2676cea1c31da46a38029b13b86eaf", "popularity.desc");

        // This is called for each movie id... Should this call be done within the main app screen itself, or should it be done in the detail screen?

        ArrayList<TMDBMovieListRetrofitObj.MovieResult> movieResults = movieReplyObject.results;

        Log.d(LOG_TAG, Integer.toString(movieResults.size()));

        TMDBMovie[] myMovies = new TMDBMovie[movieResults.size()];

        for (int i = 0; i < movieResults.size(); i++) {

            ArrayList<String> _tempReviewArray = new ArrayList<>();
            ArrayList<String> _tempTrailerArray = new ArrayList<>();

            TMDBMovieListRetrofitObj.MovieResult res = movieResults.get(i);

            myMovies[i] = new TMDBMovie ( res.id );

            // Get details for each movie id! This is done within this loop
            TMDBMovieDetailsRetrofitObj dataMovieDetails = TMDBservice.getMovieDetails(res.id, "bb2676cea1c31da46a38029b13b86eaf", "reviews,trailers");

            // This shit is mental...
            ArrayList<TMDBMovieDetailsRetrofitObj.Reviews.ReviewResults> movieReviews = dataMovieDetails.reviews.results;
            ArrayList<TMDBMovieDetailsRetrofitObj.Trailers.YouTubeResults> movieTrailers = dataMovieDetails.trailers.youtube;


            myMovies[i].setMovieTitle(res.original_title);
            myMovies[i].setMovieOverview(res.overview);
            myMovies[i].setMovieReleaseDate(res.release_date);
            myMovies[i].setMovieVote(res.vote_average);
            myMovies[i].setMoviePop(res.popularity);
            myMovies[i].setMoviePosterURI(res.poster_path);

            myMovies[i].setMovieIMDBLink(dataMovieDetails.imdb_id);
            myMovies[i].setMovieTagline(dataMovieDetails.tagline);
            myMovies[i].setMovieRuntime(dataMovieDetails.runtime);


            if (movieReviews.size() >= 1) {
                for (int j = 0; j < movieReviews.size(); j++) {
                    if (movieReviews.get(j).content != null) { _tempReviewArray.add(j, movieReviews.get(j).content); }
                }
                myMovies[i].setMovieReviews(_tempReviewArray);
            }

            if (movieTrailers.size() >= 1) {
                for (int j = 0; j < movieTrailers.size(); j++) {
                    if (movieTrailers.get(j).source != null) { _tempReviewArray.add(j, movieTrailers.get(j).source); }
                }
                myMovies[i].setMovieReviews(_tempTrailerArray);
            }

            myMovies[i].debugMovieInfo();

        }

        // Process the second call here for test 2!

        assertEquals(myMovies.length, movieResults.size());
    }

}
