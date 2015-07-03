package is.varun.app.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Receive the intent. TODO: Impliment Try/Catch in case intent does not exist.
        Intent intent = getActivity().getIntent();

        // Create new String to store getStringExtra from the intent
        String message = intent.getStringExtra(MoviePosterFragment.EXTRA_MESSAGE);

        // Create a TexView controller for our text id to be changed
        TextView titleTextCtrl = (TextView) rootView.findViewById(R.id.movie_title);

        // setText to the above intent message (String)
        titleTextCtrl.setText(message);

        return rootView;
    }
}
