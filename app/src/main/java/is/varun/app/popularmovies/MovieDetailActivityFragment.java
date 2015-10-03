package is.varun.app.popularmovies;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;


/**
 * Detail activity fragment
 */
public class MovieDetailActivityFragment extends Fragment {

    private static String FAV_KEY = "FavMovies";

    Set<String> movieIDSet;


    private OnFragmentInteractionListener mListener;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        final TMDBMovie theMovie;

        // Set context
        Context mContext = rootView.getContext();

        // Receive the intent.
        Intent intent = getActivity().getIntent();

        // Create new TMDBMovie object to init with getSerializableExtra from the intent
        if (intent.hasExtra(MoviePosterFragment.SER_KEY)){
            theMovie = intent.getParcelableExtra(MoviePosterFragment.SER_KEY);
        } else if ( getArguments() != null ){
            theMovie = getArguments().getParcelable(MoviePosterFragment.SER_KEY);
        } else {
            return null;
        }

        // Title
        TextView movieTitleView = (TextView) rootView.findViewById( R.id.movie_title);
        movieTitleView.setText(theMovie.getMovieTitle());

        mListener.setActionBarTitleSub(theMovie.getMovieTitle(), theMovie.getMovieTagline());

        // Release year
        TextView movieRelDateView = (TextView) rootView.findViewById( R.id.movie_year );
        movieRelDateView.setText( theMovie.getMovieReleaseYear() );

        // Rating
        TextView movieVoteView = (TextView) rootView.findViewById( R.id.movie_rating );
        movieVoteView.setText( theMovie.getMovieVote() );

        // Overview
        TextView movieOverviewView = (TextView) rootView.findViewById( R.id.movie_desc );
        movieOverviewView.setText(theMovie.getMovieOverview());

        // Runtime
        TextView movieRuntimeView = (TextView) rootView.findViewById( R.id.movie_runtime );
        movieRuntimeView.setText(theMovie.getMovieRuntime());

        // Poster
        ImageView moviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);

        Glide.with(mContext).load( theMovie.getMoviePosterURL() )
                .centerCrop()
                .override(185, 278)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_holder)
                .into(moviePosterView);

        Button trailer_btn = (Button) rootView.findViewById(R.id.button);

        // Trailer button
        if (theMovie.getMovieTrailers().isEmpty()){

            // Remove the button if trailers do not exist
            trailer_btn.setVisibility(View.GONE);

        } else {

            // Else keep the button and apply onClick to open the first trailer
            trailer_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("vnd.youtube:" + theMovie.getMovieTrailers().get(0)));

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + theMovie.getMovieTrailers().get(0)));
                        startActivity(intent);
                    }

                }
            });

        }

        // This section adds movie to favorite
        final CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.fav_checkBox);

        movieIDSet =  prefs.getStringSet(FAV_KEY, new HashSet<String>());

            if (movieIDSet.contains(theMovie.getMovieID()) ) {
                checkBox.setChecked(true);
                checkBox.setText("Favorite");
            }

        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editSharedPref = prefs.edit();
                // Is the checkbox checked?
                if ( ((CheckBox) v).isChecked() ) {
                    theMovie.setMovieFavoriteAs(1);
                    movieIDSet.add( theMovie.getMovieID() );
                    editSharedPref.putStringSet(FAV_KEY, movieIDSet);
                    editSharedPref.commit();
                    checkBox.setText("Favorite");

                } else {
                    theMovie.setMovieFavoriteAs(0);
                    movieIDSet.remove( theMovie.getMovieID() );
                    editSharedPref.putStringSet( FAV_KEY, movieIDSet );
                    editSharedPref.commit();
                    checkBox.setText("Add to Favorites");
                }


            }
        });



        TextView movieReview = (TextView) rootView.findViewById( R.id.movie_review );
        TextView movieReviewTitle = (TextView) rootView.findViewById( R.id.review_title );

        if (theMovie.getMovieReviews().isEmpty()){

            // Remove the review if it does not exist
            movieReview.setVisibility(View.GONE);
            movieReviewTitle.setVisibility(View.GONE);

        } else {
            movieReview.setText( theMovie.getMovieReviews().get(0) );
            movieReview.setMovementMethod(new ScrollingMovementMethod());
        }

        mListener.setIMDBLink( theMovie.getMovieIMDBLink() );

        return rootView;
    }

    public interface OnFragmentInteractionListener {
        void setActionBarTitleSub(String title, String subtitle);
        void setIMDBLink(String link);
        void aMoviePosterClicked(TMDBMovie clickedMovie);
    }
}
