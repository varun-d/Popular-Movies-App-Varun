package is.varun.app.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class to store movie data. This is a general Parcelable holder for more than just the
 * name, release date, etc.
 *
 * @author Varun D
 * @version 1.2
 * @since Sep 12, 2105
 *
 */
public class TMDBMovie implements Parcelable {

    private static final long serialVersionUID = 1337L;

    private static final String LOG_TAG = "TMDBMovie";

    private String movieID;             // '0000' REQUIRED
    private String movieTitle;          // '(Untitled)'
    private String movieOverview;       // 'Awaiting movie description'
    private String movieReleaseDate;    // '25th June 2015'
    private String moviePosterURI;      // /uXZYawqUsChGSj54wcuBtEdUJbh.jpg
    private String movieVote;           // '0.0' String.format("%.5g%n", 0.912385);
    private String moviePop;            // '00.00/100'

    // The following member Variables are taken from the second detailed call using URL:
    // /3/movie/{movie_id}?api_key={api_key}&append_to_response=reviews,trailers

    private String movieRuntime;
    private String movieTagline;
    private String movieIMDBLink;
    private int movieReviewNum;
    private ArrayList<String> movieReviews = new ArrayList<>();
    private int movieTrailerNum;
    private ArrayList<String> movieTrailers = new ArrayList<>();

    // Constructor
    public TMDBMovie(String newID) { setMovieID(newID); }

    // Movie ID
    public void setMovieID (String newID) { movieID = newID; }

    public Long getMovieID () { return Long.valueOf( movieID ); }

    // Movie Title
    public void setMovieTitle (String newTitle) { movieTitle = newTitle.isEmpty() ? "(Untitled)" : newTitle; }

    public String getMovieTitle () { return movieTitle; }

    // Movie Overview
    public void setMovieOverview (String newOverview)
    {
        movieOverview = newOverview.isEmpty() ? "Google the movie name to find out more!" : newOverview;
    }

    public String getMovieOverview () { return movieOverview; }

    // Movie Release Date
    public void setMovieReleaseDate (String newMovieReleaseDate)
    {
        movieReleaseDate = newMovieReleaseDate.isEmpty() ? "0000-00-00" : newMovieReleaseDate;
    }

    public String getMovieReleaseDate () { return movieReleaseDate; }

    public String getMovieReleaseYear () { return "Release: " + movieReleaseDate.substring(0, Math.min(movieReleaseDate.length(), 4) );
    }

    // Movie Vote
    public void setMovieVote (String newMovieVote) { movieVote = newMovieVote.isEmpty() ? "10/10" : newMovieVote; }

    public String getMovieVote () { return movieVote; }

    // Movie Popularity
    public void setMoviePop (String newMoviePop) { moviePop = newMoviePop.isEmpty() ? "100/100" : newMoviePop; }

    public String getMoviePop () { return moviePop; }

    // Movie Poser URI/URL
    public void setMoviePosterURI (String posterURI) { moviePosterURI = posterURI; }

    public String getMoviePosterURI () { return moviePosterURI; }

    public String getMoviePosterURL () { return "http://image.tmdb.org/t/p/w185" + moviePosterURI; }

    // Movie Runtime
    public void setMovieRuntime (String _var) { movieRuntime = _var; }

    public String getMovieRuntime () { return movieRuntime + " m"; }

    // Movie Tagline
    public void setMovieTagline (String _var) { movieTagline = _var; }

    public String getMovieTagline () { return movieTagline; }

    // Movie's IMDB link
    public void setMovieIMDBLink (String _var) { movieIMDBLink = _var; }

    public String getMovieIMDBLink () { return movieIMDBLink; }

    // Movie Reviews. Must always be used as this inits the movieReviews Array to apr length
    public void setMovieReviewNum (int _var) { movieReviewNum = _var; }

    public int getMovieReviewNum () { return movieReviewNum; }

    public void setMovieReviews (ArrayList<String> args) { movieReviews.addAll(args); }

    public ArrayList<String> getMovieReviews () {return movieReviews;}

    // Movie Trailers movieTrailerNum

    public void setMovieTrailerNum (int _var) { movieTrailerNum = _var; }

    public int getMovieTrailerNum () { return movieTrailerNum; }

    public void setMovieTrailers (ArrayList<String> args) { movieTrailers.addAll(args); }

    public ArrayList<String> getMovieTrailers () {return movieTrailers;}





    /* * * * * * * * * * * * * * *
     * Override Parcelable's describeContents() method
     * @return 0 for now
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Override writeToParcel method
     *
     * @param dest default argument for our Parcel
     * @param flags default argument for our Parcel
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieID);
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(movieReleaseDate);
        dest.writeString(moviePosterURI);
        dest.writeString(movieVote);
        dest.writeString(moviePop);
        dest.writeString(movieRuntime);
        dest.writeString(movieTagline);
        dest.writeString(movieIMDBLink);
        dest.writeInt(movieReviewNum);
        dest.writeInt(movieTrailerNum);
        dest.writeStringList(movieReviews);
        dest.writeStringList(movieTrailers);
    }

    private TMDBMovie(Parcel in) {
        movieID = in.readString();
        movieTitle = in.readString();
        movieOverview = in.readString();
        movieReleaseDate = in.readString();
        moviePosterURI = in.readString();
        movieVote = in.readString();
        moviePop = in.readString();
        movieRuntime = in.readString();
        movieTagline = in.readString();
        movieIMDBLink = in.readString();
        movieReviewNum = in.readInt();
        movieTrailerNum = in.readInt();
        in.readStringList(movieReviews);
        in.readStringList(movieTrailers);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TMDBMovie createFromParcel(Parcel in) {
            return new TMDBMovie(in);
        }
        public TMDBMovie[] newArray(int size) {
            return new TMDBMovie[size];
        }
    };




    /* * * * * * * * * * * * * * *
     * Debug function. Verbose Log all captured data for a given movie
     */
    public void debugMovieInfo ()
    {
        Log.v("Debug Title: ", movieTitle);
        Log.v("Debug ID: ", movieID);
        Log.v("Debug Overview: ", movieOverview);
        Log.v("Debug Vote: ", movieVote);
        Log.v("Debug Release: ", movieReleaseDate);
        Log.v("Debug Poster URI: ", moviePosterURI);
        Log.v("Debug Popularity: ", moviePop);
        Log.v("Debug # of Reviews: ", Integer.toString(movieReviews.size()) );
        Log.v("Debug # of Trailers: ", Integer.toString(movieTrailers.size()) );
    }
}
