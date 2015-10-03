package is.varun.app.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements MovieDetailActivityFragment.OnFragmentInteractionListener  {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    String mimdbLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.id_fragment_movie_detail) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.id_fragment_movie_detail, new MovieDetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //If the option, action_settings is clicked, start new activity, SettingsActivity
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setActionBarTitleSub(String title, String subtitle) {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    @Override
    public void setIMDBLink(String link) {
        mimdbLink = link;
    }

    @Override
    public void aMoviePosterClicked(TMDBMovie clickedMovie){

        // Create a bundle so we can send it off to the other activity/fragment
        Bundle mBundle = new Bundle();

        // Insert the Object in the mBundle through putSerializable
        // Remember that for this to work TMDBMovie class has to implement Serializable
        // and use private static final long serialVersionUID = 1337L;
        mBundle.putParcelable( MoviePosterFragment.SER_KEY, clickedMovie );

        if (mTwoPane) {
            Log.d("Pane Mode", "Clicked in Two Pane Mode");

            MovieDetailActivityFragment newFragment = new MovieDetailActivityFragment();
            newFragment.setArguments(mBundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.id_fragment_movie_detail, newFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();


        } else {
            Log.d("Pane Mode", "Clicked in One Pane Mode");

            // Create a new Intent object with current activity context and MovieDetailActivity class
            Intent movieDetailActivityIntent = new Intent(getApplicationContext(), MovieDetailActivity.class);


            // putExtras, add the mBundle to the new Intent
            movieDetailActivityIntent.putExtras(mBundle);

            startActivity(movieDetailActivityIntent);
        }

    }
}
