package is.varun.app.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.math.BigDecimal;

/**
 * MoviePosterFragment
 * This fragment fetches all movie data from the interwebs and displays the movie posters
 * in a GridView using Glide. The fetching and creation of TMDBMovie object is done in
 * doInBackground.
 *
 * Addition of fetched movies is done on onPostExecute
 *
 * This project was used to solidify my understanding of the powerful BaseAdapter!
 * Extending BaseAdapter is the sexiest thing I have learnt!
 *
 * @author Varun D
 * @version 1.0
 */

public class MoviePosterFragment extends Fragment {

    // SER_KEY is used to deserialize the Bundle sent onItemClick
    public final static String SER_KEY = "is.varun.app.popularmovies.SER_KEY";

    // LOG_TAG used for logging
    private final static String LOG_TAG = "MoviePosterFragment";

    // Key used for onSavedInstanceState
    private final static String SAVED_MOVIES_KEY = "MOVIES_KEY";

    //Declaring global adapter of custom MovieAdapter object that extends from BaseAdapter
    public MovieAdapter mMovieAdapter;

    // Declare a new ArrayList of TMDBMovie called savedList for restoration needs
    public ArrayList<TMDBMovie> savedList;

    // getMoviesNow AsyncTast to fetch all movies and .addMovies onPostExecute
    FetchMovieTask getMoviesAsyncTask = new FetchMovieTask();

    // Lonely empty constructor. I could write a whole novel about this guy (or not)
    public MoviePosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // I really need to understand what in gods name is this context thing
        final Context thisContext = getActivity().getApplicationContext();

        // Declare rootView of View type. Init with inflated fragment_main file
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Declare a new GridView poster_gv and init with poster_gridview view
        GridView poster_gv = (GridView) rootView.findViewById(R.id.poster_gridview);

        // Set number of columns dynamically based on pixel density!
        // TODO: This *may* change for tablet view
        poster_gv.setNumColumns(calculatedColumns(thisContext));

        // Init the mMovieAdapter with the newMovieAdapter
        mMovieAdapter = new MovieAdapter(thisContext);

        // Set the new adapter on the GridView
        poster_gv.setAdapter(mMovieAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVED_MOVIES_KEY)) {
            // If savedInstanceState is null and we do not find our movies, then run our async task
            // to fetch movies
            getMoviesAsyncTask.execute();

        } else {
            // Declare savedList with our MoviesArrayList bundle from savedInstanceState bunde
            savedList = savedInstanceState.getParcelableArrayList(SAVED_MOVIES_KEY);

            // Add the savedList movies back into our Adapter .addMovies notifies of data change
            mMovieAdapter.addMovies(savedList);
        }

        // serOnItemClickListener for our sweet GridView
        poster_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Get the TMDBMovie object for the item that was clicked
                TMDBMovie clickedMovie = mMovieAdapter.getItem(position);

                // Create a new Intent object with current activity context and MovieDetailActivity class
                Intent movieDetailActivityintent = new Intent(getActivity(), MovieDetailActivity.class);

                // Create a bundle so we can send it off to the other activity/fragment
                Bundle mBundle = new Bundle();

                // Insert the Object in the mBundle through putSerializable
                // Remember that for this to work TMDBMovie class has to implement Serializable
                // and use private static final long serialVersionUID = 1337L;
                mBundle.putParcelable(SER_KEY, clickedMovie);

                // putExtras, add the mBundle to the new Intent
                movieDetailActivityintent.putExtras(mBundle);

                // Toast for testing needs. Remove from production
                // Toast.makeText(thisContext, "TOAST: " + clickedMovie.getMovieTitle(), Toast.LENGTH_SHORT).show();

                // Call method stratActivity with the just created Intent object
                startActivity(movieDetailActivityintent);
            }
        });

        return rootView;
    }

    /**
     * Temporary sexy method to calculate number of GridView columns dynamically
     * @param c is our current application context
     * @return calculated number of columns
     */
    private int calculatedColumns(Context c) {
        float scalefactor = getResources().getDisplayMetrics().density * 100;
        int number = c.getResources().getDisplayMetrics().widthPixels;
        return (int) ((float) number / scalefactor);
    }

    @Override
    public void onSaveInstanceState(Bundle saveBundle) {
        // Calling superman
        super.onSaveInstanceState(saveBundle);

        // Put our array of movies from our Adapter. God I love adapters!
        saveBundle.putParcelableArrayList(SAVED_MOVIES_KEY, mMovieAdapter.getMyMoviesList());
    }

    /** onResume is specifically for situation when user
     * has returned from the Settings Screen
     */
    @Override
    public void onResume() {

        // Calling superman as usual
        super.onResume();

        // Declare SharedPreferences prefs and init with getDefaultSharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        // Get user's sort preference string which is either 1 or 0
        String pref_sort_opt = prefs.getString("pref_sort_by", "");

        // Use the adapters sortMovies method to sort our movies accordingly. w00t!
        mMovieAdapter.sortMovies(pref_sort_opt);
    }

    private class FetchMovieTask extends AsyncTask<String, Integer, TMDBMovie[]> {

        private TMDBMovie[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TAG_RESULTS = "results";
            final String TAG_ID = "id";
            final String TAG_TITLE = "original_title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_RELDATE = "release_date";
            final String TAG_POSTER_URI = "poster_path";
            final String TAG_VOTE = "vote_average";
            final String TAG_POP = "popularity";

            // Declare and init a new JSONObject through the moviesJsonStr we just received
            JSONObject moviesJsonObj = new JSONObject(moviesJsonStr);

            // getJSONArray within "results" as per July 10 2015 TMDB API
            JSONArray movieResultsArray = moviesJsonObj.getJSONArray(TAG_RESULTS);

            // Declare and init a new TMDBMovie object the length of our "result" array
            TMDBMovie[] myMovies = new TMDBMovie[movieResultsArray.length()];

            // Run through the results array
            for (int i = 0; i < movieResultsArray.length(); i++) {

                // Get the JSON object representing the movie at position i
                JSONObject thisMovie = movieResultsArray.getJSONObject(i);

                // Create a new movie in the myMovies Object Array with the constructor value of ID
                myMovies[i] = new TMDBMovie(thisMovie.getString(TAG_ID));

                // Set rest of the values through it's setter methods. This may need to be updated later
                myMovies[i].setMovieTitle(thisMovie.getString(TAG_TITLE));
                myMovies[i].setMovieOverview(thisMovie.getString(TAG_OVERVIEW));
                myMovies[i].setMovieReleaseDate(thisMovie.getString(TAG_RELDATE));
                myMovies[i].setMovieVote(thisMovie.getString(TAG_VOTE));
                myMovies[i].setMoviePop(thisMovie.getString(TAG_POP));
                myMovies[i].setMoviePosterURI(thisMovie.getString(TAG_POSTER_URI));
            }

            return myMovies;
        }

        protected TMDBMovie[] doInBackground(String... sParams) {

            // urlConnection and reader declared outside the try/catch so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String rawMoviesJsonStr;

            try {
                // Create a new URL builder called builder. Duh!
                Uri.Builder builder = new Uri.Builder();

                // Let the builder build the URL in the format of http://api.themoviedb.org/3/movie/popular?api_key=bb2676cea1c31da46a38029b13b86eaf
                // TODO: Move API_KEY to settings in the future
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter("api_key", "bb2676cea1c31da46a38029b13b86eaf")
                        .appendQueryParameter("sort_by", "popularity.desc");

                // Build the URL, toString it and send it over to 'url' to be processed
                String myUrl = builder.build().toString();
                URL url = new URL(myUrl);

                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    rawMoviesJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    rawMoviesJsonStr = null;
                }

                rawMoviesJsonStr = buffer.toString();

                // Log rawMoviesJsonStr to check if we have received any reply
                Log.d(LOG_TAG, rawMoviesJsonStr);

                try {
                    // Use the helper function to turn JSON into Movies object using our TMDBMovie class
                    return getMoviesDataFromJson(rawMoviesJsonStr);

                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error running doInBackground.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream. Stuck in dryer.", e);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(TMDBMovie[] movieArray) {

            // If we receive some stuff in movieArray. Future: What happens on else?
            if (movieArray != null) {

                // Call the addMovies function to send over our array of TMDBMovie list to the Adapter
                // Remember that the addMovies will also call this.notifyDataSetChanged()
                // Beautiful, isn't it? Look at this work of art.
                mMovieAdapter.addMovies(new ArrayList<>(Arrays.asList(movieArray)));
            }

        }

    }
}

