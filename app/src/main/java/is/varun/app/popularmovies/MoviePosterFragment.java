package is.varun.app.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Collections;


/**
 * A placeholder fragment containing a simple view.
 */

public class MoviePosterFragment extends Fragment {

    public final static String EXTRA_MESSAGE = "is.varun.app.popularmovies.MESSAGE";
    private final static String LOG_TAG = "MoviePosterFragment";

    public MovieAdapter mImageAdapter;

    public MoviePosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Context thisContext = getActivity().getApplicationContext();

        // Assign View rootView the inflated fragment_main xml template
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Assign poster_gv with the poster_gridview by finding it within the rootView
        GridView poster_gv = (GridView) rootView.findViewById(R.id.poster_gridview);

        mImageAdapter = new MovieAdapter(thisContext);

        // Set an Adapter on the poster_gv. This adapter is an ImageAdapted extended from the BaseAdapter class
        poster_gv.setAdapter(mImageAdapter);

        FetchMovieTask getMoviesNow = new FetchMovieTask();
        getMoviesNow.execute();

        // On click to open Toast for now. TODO: Open MovieDetailActivity in the future through intent
        poster_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String constrMesssage = "Tes test test test test You are looking at the message from PosterPage. Clicked on element " + position;

                Toast.makeText(thisContext, "" + constrMesssage, Toast.LENGTH_SHORT).show();

                // Create a new Intent object with current activity context and detailActivity as the class
                Intent movieDetailActivityintent = new Intent(getActivity(), MovieDetailActivity.class);

                // putExtra: Add message for the intent activity with Key called EXTRA_MESSAGE defined as static value of current package .MESSAGE
                movieDetailActivityintent.putExtra(EXTRA_MESSAGE, constrMesssage);

                // Call method stratActivity with the just created Intent object
                startActivity(movieDetailActivityintent);
            }
        });


        return rootView;
    }


    public class MovieAdapter extends BaseAdapter {

        // Keeping this for historic reasons. Unused
        private Integer[] mThumbIds = {
                R.drawable.sample_8,
                R.drawable.sample_7,
                R.drawable.sample_6,
                R.drawable.sample_5,
                R.drawable.sample_4
        };

        // Create a new myMoviesList list of object type TMDBMovie
        ArrayList<TMDBMovie> myMoviesList;

        // Create a private member Context variable mContext
        private Context mContext;

        /** MovieAdapter constructor
         * Initialize the myMoviesList ArrayList that will contain the movie data
         * assign mContext
         */
        public MovieAdapter (Context c) {
            mContext = c;
            myMoviesList = new ArrayList<>();

            // Populate the myMoviesList with one default placeholder
            Resources res = c.getResources();

            // Create a sample movie object
            TMDBMovie sample = new TMDBMovie(res.getString(R.string.movie_id_sample));
            sample.setMovieTitle(res.getString(R.string.movie_title_sample));
            sample.setMovieOverview(res.getString(R.string.movie_overview_sample));
            sample.setMovieVote(res.getString(R.string.movie_vote_sample));
            sample.setMovieReleaseDate(res.getString(R.string.movie_rel_sample));
            sample.setMoviePop(res.getString(R.string.movie_pop_sample));
            sample.setMoviePosterURI(res.getString(R.string.movie_posterURL_sample));

            // Insert the sample movie object into the myMoviesList
            myMoviesList.add(sample);
        }

        // getCount gets the size of myMoviesList
        @Override
        public int getCount() {
            return myMoviesList.size();
        }

        // getItem gets the item at position (int) from myMoviesList
        @Override
        public Object getItem(int position){
            return myMoviesList.get(position);
        }

        // getItemId gets the item id at the position. Uses the getID() function of our custom TMDB object :)
        @Override
        public long getItemId(int position){
            return myMoviesList.get(position).getMovieID();
        }

        // A custom Add function to add Movies to the myMoviesList
        public void addMovies (TMDBMovie[] movies) {
            Collections.addAll(myMoviesList,movies);
            this.notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // TODO: Fix how we are displaying the image. Either create this view in XML or fix it here

            // Declare an ImageView
            ImageView imageView;

            // If not recycled there then initialize the new imageView with attributes
            if (convertView == null) {

                // Create a new ImageView for the current context
                imageView = new ImageView(mContext);

                // Set this to true if you want the ImageView to adjust its bounds to preserve the aspect ratio of its drawable.
                imageView.setAdjustViewBounds(true);

                //Scale the image uniformly (and can bleed out) while preserving aspect ratio
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                //setPadding only on Right and Bottom
                imageView.setPadding(0,1,1,0);

            } else {

                // Else recycle the view from convertView
                imageView = (ImageView) convertView;
            }

            // This commnet is for testing: imageView.setImageResource(mThumbIds[position]);

            // Load image URL from the myMoviesList corresponding to the current position of the view
            Glide.with(mContext).load(myMoviesList.get(position).getMoviePosterURL())
                    .centerCrop()
                    .override(185, 278)
                   .into(imageView);


            return imageView;
        }

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

            JSONObject moviesJsonObj = new JSONObject(moviesJsonStr);
            JSONArray movieResultsArray = moviesJsonObj.getJSONArray(TAG_RESULTS);

            TMDBMovie[] myMovies = new TMDBMovie[movieResultsArray.length()];

            for (int i = 0; i < movieResultsArray.length(); i++) {

                // Get the JSON object representing the movie at position i
                JSONObject thisMovie = movieResultsArray.getJSONObject(i);

                // Create a new movie in the myMovies Object Array with the constructor value of ID
                myMovies[i] = new TMDBMovie(thisMovie.getString(TAG_ID));

                // Set rest of the values through it's setter methods
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
            // Get preferences from SharedPreferences summary
            // String sortSetting = sharedPref.getString("sortby", "");

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String rawMoviesJsonStr = null;

            // This is where we define our array of myMovies of class Movie
            TMDBMovie[] myMovies = null;

            try {
                // Create a new URL builder called builder
                Uri.Builder builder = new Uri.Builder();

                // Let the builder build the URL in the format of http://api.themoviedb.org/3/movie/popular?api_key=bb2676cea1c31da46a38029b13b86eaf
                // TODO: Move API_KEY to settings in the future
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath("popular")
                        .appendQueryParameter("api_key", "bb2676cea1c31da46a38029b13b86eaf");

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
                Log.v(LOG_TAG, rawMoviesJsonStr);

                //return myMovies;

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

        // Method to do some work as doInBackground finishes
        protected void onPostExecute(TMDBMovie[] movieArray) {
            if (movieArray != null) {

                for (TMDBMovie singleMovie : movieArray) {
                    Log.d(LOG_TAG, String.valueOf(singleMovie.getMovieID()));
                }

                mImageAdapter.addMovies(movieArray);
            }

        }

    }
}

