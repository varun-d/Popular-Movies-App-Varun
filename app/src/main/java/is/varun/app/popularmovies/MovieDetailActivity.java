package is.varun.app.popularmovies;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // TODO: 9/20/15 Pull out IMDB link through Fragment Listener and put it here
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
