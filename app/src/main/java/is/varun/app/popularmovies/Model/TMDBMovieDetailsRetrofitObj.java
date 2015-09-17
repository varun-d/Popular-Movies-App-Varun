package is.varun.app.popularmovies.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/5/15
 * Check this for all possible values:
 * http://docs.themoviedb.apiary.io/#reference/movies/movieid/get?console=1
 * Not using getter and setter. If this works for Retrofit, I do not want to complicate anything!
 * @author Varun D
 *
 */
public class TMDBMovieDetailsRetrofitObj {

    // Runtime available in movie/id endpoint
    public String runtime;

    // Good for our UI
    public String tagline;

    // IMDB ID good for UI
    public String imdb_id;

    // The public Reviews obj
    public Reviews reviews;

    public class Reviews{

        public Reviews() { reviews = new Reviews(); }

        // To know how many reviews we have got!
        public String total_results;

        public ArrayList<ReviewResults> results;

        public class ReviewResults{

            public ReviewResults() {
                results = new ArrayList<>();
            }

            // We may not use id
            public String id;
            public String author;
            public String content;
            public String url;
        }
    }

    // The public Trailers obj
    public Trailers trailers;

    public class Trailers{

        public Trailers() { trailers = new Trailers(); }

        public List<YouTubeResults> youtube;

        public class YouTubeResults{

            public YouTubeResults() {
                youtube = new ArrayList<>();
            }

            public String source;
            public String type;
        }
    }


}
