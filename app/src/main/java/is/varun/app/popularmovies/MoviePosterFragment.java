package is.varun.app.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */

public class MoviePosterFragment extends Fragment {

    public final static String EXTRA_MESSAGE = "is.varun.app.popularmovies.MESSAGE";
    private final static String LOG_TAG = "MoviePosterFragment";

    public MoviePosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Context thisContext = getActivity().getApplicationContext();

        // Assign View rootView the inflated fragment_main xml template
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Assign poster_gv with the poster_gridview by finding it within the rootView
        GridView poster_gv = (GridView) rootView.findViewById(R.id.poster_gridview);

        // Set an Adapter on the poster_gv. This adapter is an ImageAdapted extended from the BaseAdapter class
        poster_gv.setAdapter( new ImageAdapter(thisContext) );

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

    public class ImageAdapter extends BaseAdapter {

        // Create a private member Context variable mContext
        private Context mContext;

        // Init the ImageAdapter with the private Context mContext
        public ImageAdapter (Context c) {
            mContext = c;
        }

        // The Adapter class has to overide getCount
        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        // The Adapter class has to overide getItem
        @Override
        public Object getItem(int position){
            return null;
        }

        // the Adapter class has to overide getItemId
        @Override
        public long getItemId(int position){
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // Create an undeclared variable ImageView
            ImageView imageView;

            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                // Create a new ImageView object with the current context passed on as variable
                imageView = new ImageView(mContext);
                //setLayoutParams sets the width and height of the image so each image is resized and cropped to fit in these dimensions, as appropriate.
                imageView.setAdjustViewBounds(true);
                //setScaleType to center crop w00t
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //setPadding on all sides. TODO: Fix image issues later
                imageView.setPadding(0,0,0,0);
            } else {
                imageView = (ImageView) convertView;
            }

            // TODO: Understand what this does
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to sample images
        private Integer[] mThumbIds = {
                R.drawable.sample_8, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7,
                R.drawable.sample_0, R.drawable.sample_1,
                R.drawable.sample_2, R.drawable.sample_8,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7,
                R.drawable.sample_8, R.drawable.sample_1,
                R.drawable.sample_2, R.drawable.sample_8,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7
        };

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
        // TODO: onPostExec will get two items...

        protected void onPostExecute(TMDBMovie[] movieArray) {
            for (int i = 0; i < movieArray.length; i++){
                Log.v(LOG_TAG,movieArray[0].getMovieID());
            }


            /* Do something here if movieResults is not null
            if (movieResults != null) {
                mForecastAdapter.clear();

                for (String datForecastStr : movieResults) {
                    mForecastAdapter.add(datForecastStr);
                }
            }*/
        }

    }
}

