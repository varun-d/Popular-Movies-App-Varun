package is.varun.app.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Varun on 7/17/15.
 * I can taste the sweetness of extending BaseAdapter in my mouth!
 * Still need to learn about view holders and updating multiple views on getView
 */

public class MovieAdapter extends BaseAdapter {

    // Declare a new myMoviesList ArrayList of object type TMDBMovie
    ArrayList<TMDBMovie> myMoviesList;

    // Declare a Context private member variable mContext. Not init'ed with any value
    private Context mContext;

    /**
     * MovieAdapter constructor
     * Initialize the myMoviesList ArrayList that will contain the movie data
     * assign mContext
     */
    public MovieAdapter(Context c) {

        // Init mContext by consuming c
        mContext = c;

        // Init myMoviesList new ArrayList within the constructor. Empty.
        myMoviesList = new ArrayList<>();

    }

    // getCount gets the size of myMoviesList
    @Override
    public int getCount() {
        return myMoviesList.size();
    }

    // getItem gets the item at position (int) from myMoviesList
    @Override
    public TMDBMovie getItem(int position) {
        return myMoviesList.get(position);
    }

    // getItemId gets the item id at the position. Uses the getID() function of our custom TMDB object :)
    @Override
    public long getItemId(int position) {
        return myMoviesList.get(position).getMovieID();
    }

    /**
     * A custom Add function to add Movies to the myMoviesList. This method also notifies of data changes!
     *
     * @param newMovies the list of movies to add
     */
    public void addMovies(ArrayList<TMDBMovie> newMovies) {

        // Add all the recently fetched movies to our myMoviesList arraylist
        myMoviesList.addAll(newMovies);

        /** After adding in the movies we sort the movies according to user's prefs
         * even though movies are sorted by popularity by default.
         */

        // Declare SharedPreferences prefs and init with getDefaultSharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        // Get user's sort preference string which is either 1 or 0
        String pref_sort_opt = prefs.getString("pref_sort_by", "");

        // Sort according to the pref
        this.sortMovies(pref_sort_opt);

        // Inform the adapter our data has changed
        this.notifyDataSetChanged();
    }

    /**
     * A custom sort function to sort Movies in the myMoviesList.
     * This method also notifies of data changes
     *
     * Note: Uses Comparator on BigDecimal. Need to import java.math.BigDecimal
     *
     * @param sort_by should be either "1" for popular or "0" for vote
     */
    public void sortMovies(String sort_by) {

        // If equals 1, sort by Popularity
        if (sort_by.equals("1")) {
            Collections.sort(myMoviesList, new Comparator<TMDBMovie>() {
                @Override
                public int compare(TMDBMovie m1, TMDBMovie m2) {
                    BigDecimal bg1, bg2;
                    bg1 = new BigDecimal(m1.getMoviePop());
                    bg2 = new BigDecimal(m2.getMoviePop());
                    return bg2.compareTo(bg1);
                }
            });
            this.notifyDataSetChanged();
        }
        // If equals 0, sort by Vote
        if (sort_by.equals("0")) {
            Collections.sort(myMoviesList, new Comparator<TMDBMovie>() {
                @Override
                public int compare(TMDBMovie m1, TMDBMovie m2) {
                    BigDecimal bg1, bg2;
                    bg1 = new BigDecimal(m1.getMovieVote());
                    bg2 = new BigDecimal(m2.getMovieVote());
                    return bg2.compareTo(bg1);
                }
            });
            this.notifyDataSetChanged();
        }
    }

    /**
     * Returns the myMoviesList object
     *
     * @return an ArrayList of TMDBMovies object
     */
    public ArrayList<TMDBMovie> getMyMoviesList(){
        return myMoviesList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Not an ideal way to show poster images
        // Could possibly use an xml layout file with one ImageView. Oh well...

        // Declare an ImageView... sigh...
        ImageView imageView;

        // If not recycled there then initialize the new imageView with attributes
        if (convertView == null) {

            // Create a new ImageView within the current context? What the hell is this context
            imageView = new ImageView(mContext);

            // Set this to true if you want the ImageView to adjust its bounds to preserve the aspect ratio of its drawable.
            imageView.setAdjustViewBounds(true);

            //Scale the image uniformly (and can bleed out) while preserving aspect ratio
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //setPadding only on Right and Bottom
            imageView.setPadding(0, 1, 1, 0);

        } else {

            // Else recycle the view from convertView. Go green!
            imageView = (ImageView) convertView;
        }

        // This is for testing: imageView.setImageResource(mThumbIds[position]);

        // Load image URL from the myMoviesList corresponding to the current position of the view
        Glide.with(mContext).load(myMoviesList.get(position).getMoviePosterURL())
                .centerCrop()
                .override(185, 278)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_holder)
                .into(imageView);

        return imageView;
    }

}
