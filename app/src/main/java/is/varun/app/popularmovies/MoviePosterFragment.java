package is.varun.app.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Arrays;

import is.varun.app.popularmovies.Model.TMDBMovieDetailsRetrofitObj;
import is.varun.app.popularmovies.Model.TMDBMovieListRetrofitObj;
import retrofit.RestAdapter;

/**
 * MoviePosterFragment
 * This fragment fetches all movie data from the interwebs and displays the movie posters
 * in a GridView using Glide. The fetching and creation of TMDBMovie object is done in
 * doInBackground.
 *
 * Addition of fetched movies to the Adapter is done on onPostExecute
 *
 * This project was used to solidify my understanding of the powerful BaseAdapter!
 * Extending BaseAdapter is the sexiest thing I have learnt!
 *
 * @author Varun D
 * @version 2.0
 */

public class MoviePosterFragment extends Fragment {

    // SER_KEY is used to deserialize the Bundle sent through onItemClick
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

    private MovieDetailActivityFragment.OnFragmentInteractionListener mListener;

    // Lonely empty constructor. I could write a whole novel about this one (or not)
    public MoviePosterFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MovieDetailActivityFragment.OnFragmentInteractionListener) {
            mListener = (MovieDetailActivityFragment.OnFragmentInteractionListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
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
        poster_gv.setNumColumns( calculatedColumns() );

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

                // Call the function that sends this data to the MainActivity
                mListener.aMoviePosterClicked(clickedMovie);

            }
        });

        return rootView;
    }

    /**
     * Temporary sexy method to calculate number of GridView columns dynamically
     * @return calculated number of columns
     */
    private int calculatedColumns() {

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth >= 600) {

            // Use 3 columns for anything larger than sw-600
            return 3;

        } else {

            // Calculate for other smaller sizes. That is, for even landscape!
            return (int) ((float) smallestWidth / scaleFactor);
        }


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

        // Get user's sort preference string which is either 0,1 or 2
        String pref_sort_opt = prefs.getString("pref_sort_by", "");

        // Use the adapters sortMovies method to sort our movies accordingly. w00t!
        mMovieAdapter.sortMovies(pref_sort_opt, prefs );
    }

    private class FetchMovieTask extends AsyncTask<String, Integer, TMDBMovie[]> {

        // Declare a ProgressDialog private class variable
        private ProgressDialog pdia;

        // OnPreExecute Init the progressDialog and show it till doInBackground runs its course
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(MoviePosterFragment.this.getActivity());
            pdia.setMessage("Loading movies...");
            pdia.show();
        }

        /**
         * This version runs both calls in the parent activity.
         * @param sParams
         * @return
         */
        protected TMDBMovie[] doInBackground(String... sParams) {

            // Get API Key from resources strings.xml
            String R_API_KEY = getResources().getString(R.string.api_key);

            // Declare the Retrofit service
            TMDBApiRetrofit TMDBservice;

            // Declare received object here
            TMDBMovieListRetrofitObj movieReplyObject;

            // Declare and init an empty TMDBMovie object array
            TMDBMovie[] myMovies = null;

            // This try statement calls all data within the first loading screen itself... Should implement dialog loading here
            try {
                final String API_URL = "https://api.themoviedb.org";

                TMDBservice = new RestAdapter.Builder()
                        .setEndpoint(API_URL)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build()
                        .create(TMDBApiRetrofit.class);

                movieReplyObject = TMDBservice.getMovies(R_API_KEY, "popularity.desc");

                ArrayList<TMDBMovieListRetrofitObj.MovieResult> movieResults = movieReplyObject.results;

                Log.d(LOG_TAG, Integer.toString(movieResults.size()));

                myMovies = new TMDBMovie[movieResults.size()];

                for (int i = 0; i < movieResults.size(); i++) {

                    ArrayList<String> _tempReviewArray = new ArrayList<>();
                    ArrayList<String> _tempTrailerArray = new ArrayList<>();

                    TMDBMovieListRetrofitObj.MovieResult res = movieResults.get(i);

                    TMDBMovieDetailsRetrofitObj dataMovieDetails = TMDBservice.getMovieDetails(res.id, R_API_KEY, "reviews,trailers");

                    // This shit is mental...
                    ArrayList<TMDBMovieDetailsRetrofitObj.Reviews.ReviewResults> movieReviews = dataMovieDetails.reviews.results;
                    ArrayList<TMDBMovieDetailsRetrofitObj.Trailers.YouTubeResults> movieTrailers = dataMovieDetails.trailers.youtube;

                    myMovies[i] = new TMDBMovie(res.id);
                    myMovies[i].setMovieTitle(res.original_title);
                    myMovies[i].setMovieOverview(res.overview);
                    myMovies[i].setMovieReleaseDate(res.release_date);
                    myMovies[i].setMovieVote(res.vote_average);
                    myMovies[i].setMoviePop(res.popularity);
                    myMovies[i].setMoviePosterURI(res.poster_path);

                    myMovies[i].setMovieIMDBLink(dataMovieDetails.imdb_id);
                    myMovies[i].setMovieRuntime(dataMovieDetails.runtime);
                    myMovies[i].setMovieTagline(dataMovieDetails.tagline);

                    if (movieReviews.size() >= 1) {
                        for (int j = 0; j < movieReviews.size(); j++) {
                            if (movieReviews.get(j).content != null) { _tempReviewArray.add(j, movieReviews.get(j).content); }

                        }
                        myMovies[i].setMovieReviews(_tempReviewArray);
                    }

                    if (movieTrailers.size() >= 1) {
                        for (int j = 0; j < movieTrailers.size(); j++) {
                            if (movieTrailers.get(j).source != null) { _tempTrailerArray.add(j, movieTrailers.get(j).source); }
                        }
                        myMovies[i].setMovieTrailers(_tempTrailerArray);
                    }

                }

            } catch (Exception e){
                Log.d(LOG_TAG,e.toString());
                return null;
            }
            return myMovies;
        }


        protected void onPostExecute(TMDBMovie[] movieArray) {

            // Dismiss the loading dialog if it's still showing
            if (pdia.isShowing()){
                pdia.dismiss();
            }

            // If we receive some stuff in movieArray else show network error message!
            if (movieArray != null) {

                // Call the addMovies function to send over our array of TMDBMovie list to the Adapter
                // Remember that the addMovies will also call this.notifyDataSetChanged()
                // Beautiful, isn't it? Look at this work of art.
                mMovieAdapter.addMovies(new ArrayList<>(Arrays.asList(movieArray)));
            } else {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Notice");
                alertDialog.setMessage("Seems to be a network error my friend.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okie Dokie",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        }

    }
}

