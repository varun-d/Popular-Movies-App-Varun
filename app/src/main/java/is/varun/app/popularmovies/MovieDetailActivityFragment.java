package is.varun.app.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;


/**
 * Detail activity fragment
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Receive the intent.
        // This will always exist as DetailActivity is never called from anywhere else!
        Intent intent = getActivity().getIntent();
        Context mContext = rootView.getContext();

        // Create new TMDBMovie object to init with getSerializableExtra from the intent
        TMDBMovie mMovie = (TMDBMovie) intent.getParcelableExtra(MoviePosterFragment.SER_KEY);

        // Objectify all views that we need to update
        TextView movieTitleView = (TextView) rootView.findViewById(R.id.movie_title);
        TextView movieRelDateView = (TextView) rootView.findViewById(R.id.movie_year);
        TextView movieVoteView = (TextView) rootView.findViewById(R.id.movie_rating);
        TextView movieOverviewViewLOL = (TextView) rootView.findViewById(R.id.movie_desc);
        //TextView movieRuntimeView = (TextView) rootView.findViewById(R.id.movie_runtime);
        ImageView moviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);

        // setText to the above intent message (String)
        movieTitleView.setText(mMovie.getMovieTitle());
        movieRelDateView.setText("Release: " + mMovie.getMovieReleaseDate().substring(0, Math.min(mMovie.getMovieReleaseDate().length(), 4)));
        movieVoteView.setText(mMovie.getMovieVote());
        movieOverviewViewLOL.setText(mMovie.getMovieOverview());

        // Hiding Runtime for now
       // movieRuntimeView.setVisibility(View.INVISIBLE);

        // Feeling adventurous here...
        Glide.with(mContext).load(mMovie.getMoviePosterURL())
                .centerCrop()
                .override(185, 278)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_holder)
                .into(moviePosterView);

        return rootView;
    }
}
