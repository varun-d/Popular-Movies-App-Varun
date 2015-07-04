package is.varun.app.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */

//TODO: Within the API: final String TMDB_APIKEY = thisContext.getString(R.string.tmdb_apikey);

public class MoviePosterFragment extends Fragment {

    public final static String EXTRA_MESSAGE = "is.varun.app.popularmovies.MESSAGE";

    public MoviePosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Context thisContext = getActivity().getApplicationContext();

        // Assign View rootView the inflated fragment_main xml template
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Assign poster_gb with the poster_gridview by finding it within the rootView
        GridView poster_gv = (GridView) rootView.findViewById(R.id.poster_gridview);

        // Set an Adapter on the poster_gv. This adapter is an ImageAdapted extended from the BaseAdapter class
        poster_gv.setAdapter( new ImageAdapter(thisContext) );

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
}
