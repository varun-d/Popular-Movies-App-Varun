package is.varun.app.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The activity implements an interaction listener to transfer data back to the Activity
 */

public class MovieDetailActivity extends ActionBarActivity implements MovieDetailActivityFragment.OnFragmentInteractionListener {

    String mimdbLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // TODO: CLEANUP 9/23/15 The toolbar back button from detail view SHOULD do a normal back, not reload!
        int id = item.getItemId();

        if (id == R.id.action_imdb) {
            startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + mimdbLink)));
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

}
