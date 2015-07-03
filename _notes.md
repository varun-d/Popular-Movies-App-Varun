# Next steps for the app
api_key = "bb2676cea1c31da46a38029b13b86eaf"
posterURL append: http://image.tmdb.org/t/p/
Main URL: 

http://api.themoviedb.org/3/movie/popular
?api_key=bb2676cea1c31da46a38029b13b86eaf
&sort_by=popularity.asc
> OR &sort_by=vote_average.asc

1. Get popular movies from the above URL
    
    How many array objects are returned? (18 generally)

2. Returned: JSON object with all the information in it. The following key's are important for us:

Under the main object, "results" has arrays of key value pair movie information

* original_title: The title of this movie
* overview: A short description of the movie. (1000 or less chars)
* release_date: Release date in format "2015-06-12"
* poster_path: The poster of this movie in the format of "/xxx". This needs to be appended to http://image.tmdb.org/t/p/ with the max width we require, i.e.w185. : http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg
* vote_average: out of /10 average vote rating
