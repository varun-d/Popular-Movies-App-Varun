package is.varun.app.popularmovies;

import android.util.Log;

import java.io.Serializable;

/**
 * A class to store movies
 *
 * @author Varun Dhanwantri
 * @version 1.0
 *
 */
public class TMDBMovie implements Serializable {

    private static final long serialVersionUID = 1337L;

    private String LOG_TAG = "TMDBMovie ClassLog";
    private String movieID;             // '0000' REQUIRED
    private String movieTitle;          // '(Untitled)'
    private String movieOverview;       // 'Awaiting movie description'
    private String movieReleaseDate;    // '25th June 2015'
    private String moviePosterURI;      // /uXZYawqUsChGSj54wcuBtEdUJbh.jpg
    private String movieVote;           // '0.0' String.format("%.5g%n", 0.912385);
    private String moviePop;            // '00.00/100'

    /**
     * Constructor for objects of class TMDBMovie
     * @param  newID: Movie ID of type String
     */
    public TMDBMovie(String newID)
    {
        // Initialise movie ID and take it from there
        setMovieID(newID);
    }

    /**
     * Set MovieID
     * @param  newID: Movie ID of type String
     */
    public void setMovieID (String newID)
    {
        movieID = newID;
    }

    /**
     * Get MovieID
     * @return  ID of the movie
     */
    public Long getMovieID ()
    {
        return Long.valueOf(movieID);
    }

    /**
     * Set the Movie Title
     *
     * @param  newTitle is the String title of the movie. Some TMDB movies may have unicode titles.
     */
    public void setMovieTitle (String newTitle)
    {
        // If newTitle is null String then assign movie name as '(Untitled)' TODO Unicode.
        if ( newTitle == null ) {
            movieTitle = "(Untitled)";
        } else {
            movieTitle = newTitle;
        }
    }

    /**
     * Get the Movie Title
     *
     * @return the movie title
     */
    public String getMovieTitle ()
    {
        return movieTitle;
    }


    /**
     * Set the Movie Overview
     *
     * @param  newOverview   a sample parameter for a method
     */
    public void setMovieOverview (String newOverview)
    {
        if ( newOverview == null ) {
            movieOverview = "Movie description not found.";
        } else {
            movieOverview = newOverview;
        }
    }

    /**
     * Get the Movie Overview
     *
     * @return  the movie overview
     */
    public String getMovieOverview ()
    {
        return movieOverview;
    }

    /**
     * Set the Movie Release Date
     *
     * @param  newMovieReleaseDate   a sample parameter for a method
     */
    public void setMovieReleaseDate (String newMovieReleaseDate)
    {
        if ( newMovieReleaseDate == null ) {
            movieReleaseDate = "Date Empty";
        } else {
            movieReleaseDate = newMovieReleaseDate;
        }
    }

    /**
     * Get the Movie Release Date
     *
     * @return  the movie release date
     */
    public String getMovieReleaseDate ()
    {
        return movieReleaseDate;
    }

    /**
     * Set the Movie Average Vote out of 10 points
     *
     * @param  newMovieVote   in string format
     */
    public void setMovieVote (String newMovieVote)
    {
        if ( newMovieVote == null ) {
            movieVote = "0/10";
        } else {
            movieVote = newMovieVote;
        }
    }

    /**
     * Get the Movie Average Vote out of 10 points
     *
     * @return the average movie rating
     */
    public String getMovieVote ()
    {
        return movieVote;
    }

    /**
     * Set the Movie Popularity out of 100 points
     *
     * @param  newMoviePop   in string format
     */
    public void setMoviePop (String newMoviePop)
    {
        if ( newMoviePop == null ) {
            moviePop = "0/100";
        } else {
            moviePop = newMoviePop;
        }
    }

    /**
     * Get the Movie Popularity out of 100 points
     *
     * @return the popularity of movie out of 100
     */
    public String getMoviePop ()
    {
        return moviePop;
    }

    /**
     * Set the Movie Poster URL in format of /uXZYawqUsChGSj54wcuBtEdUJbh.jpg
     *
     * @param  posterURI   String should look like "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg"
     */
    public void setMoviePosterURI (String posterURI)
    {
        if ( posterURI == null ) {
            moviePosterURI = "https://placeholdit.imgix.net/~text?txtsize=17&txt=poster%20placeholder&w=185&h=278";
        } else {
            moviePosterURI = posterURI;
        }
    }

    /**
     * Get the Movie Poster URL in format of /uXZYawqUsChGSj54wcuBtEdUJbh.jpg
     *
     * @return the movie poster URL moviePosterURI
     */
    public String getMoviePosterURI ()
    {
        return moviePosterURI;
    }

    public String getMoviePosterURL ()
    {
        return "http://image.tmdb.org/t/p/w185" + moviePosterURI;
    }


    /**
     * Debug function. Verbose Log all captured data for a given movie
     */
    public void debugMovieInfo ()
    {
        Log.v(LOG_TAG, movieTitle);
        Log.v(LOG_TAG, movieID);
        Log.v(LOG_TAG, movieOverview);
        Log.v(LOG_TAG, movieVote);
        Log.v(LOG_TAG, movieReleaseDate);
        Log.v(LOG_TAG, moviePosterURI);
        Log.v(LOG_TAG, moviePop);
    }
}
