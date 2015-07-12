# Welcome

This public repo is for a practice project under the Udacity's Andoird NanoD program.

# Popular Movies App

## TODO

1. Create the settings section

> When setting is updated, onResume needs to sort the movies by Vote or Pop and display again hmm.
> 
> Could be done through a new BaseAdapter custom function called sortby, which would also internally call this.notifydatachangethingy
> 


# Personal Notes

posterURL append: http://image.tmdb.org/t/p/
Sample: http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg

Main URL: http://api.themoviedb.org/3/discover/movie
 Changed this to /discover from July 11nth
 &sort_by=popularity.desc

1. Get popular movies from the above URL
    How many array objects are returned? (20 generally, which works for now)

2. Returned: JSON object with all the information in it. The following key's are important for us:

Under the main object, **results** has arrays of key value pair movie information

* id: Movie id on TMDB for keepsakes
* original_title: The title of this movie
* overview: A short description of the movie. (1000 or less chars)
* release_date: Release date in format "2015-06-12"
* poster_path: The poster of this movie in the format of "/xxx". This needs to be appended to http://image.tmdb.org/t/p/ with the max width we require, i.e.w185. : http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg
* vote_average: out of /10 average vote rating
* popularity: out of 100

3. Creare a Movie Object that will be arrayed and store the stuff we got from the interwebs into an array. Limit this to max 20.

4. for each movie in Movies getMoviePosterURL('w185') and send into Glide library to load the images.

## Settings notes

1. Create the settings section

> When setting is updated, onResume needs to sort the movies by Vote or Pop and display again hmm.
> 
> Could be done through a new BaseAdapter custom function called sortby, which would also internally call this.notifydatachangethingy
> 
