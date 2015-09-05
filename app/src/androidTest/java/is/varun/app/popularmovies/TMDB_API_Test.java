package is.varun.app.popularmovies;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import is.varun.app.popularmovies.Model.TMDBMovieListObj;
import retrofit.RestAdapter;

/**
 * Test suite for TMDB Retrofit API testing
 */
public class TMDB_API_Test  extends ApplicationTestCase<Application> {

    public TMDB_API_Test() {
        super(Application.class);
    }

    private final String TEST_URL = "https://api.themoviedb.org";

    private TMDBApiRetrofit TMDBservice = new RestAdapter.Builder()
            .setEndpoint(TEST_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build()
            .create(TMDBApiRetrofit.class);

    public void testGetMovies() throws Exception {
        TMDBMovieListObj movies = TMDBservice.getMovies("bb2676cea1c31da46a38029b13b86eaf","popular.asc");
        Log.d(">>>", Integer.toString(movies.page));
    }
}
