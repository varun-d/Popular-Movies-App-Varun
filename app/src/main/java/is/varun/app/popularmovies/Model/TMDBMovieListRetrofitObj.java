package is.varun.app.popularmovies.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/5/15
 * Not using getter and setter. If this works for Retrofit, I do not want to complicate anything!
 * @author Varun D
 *
 */
public class TMDBMovieListRetrofitObj {

    // Keeping page for testing reasons
    public String page;

    // We do not need total_pages for now
    // public String total_pages;

    // We do not need total_results for now
    // public String total_results;

    // Movie results array is under this key
    public List<MovieResult> results;

    public class MovieResult{

        public MovieResult() {
            results = new ArrayList<>();
        }

        public String id;
        public String original_title;
        public String overview;
        public String release_date;
        public String poster_path;
        public String popularity;
        public String vote_average;



    }
}
