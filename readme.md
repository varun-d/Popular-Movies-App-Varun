# Welcome

This public repo is for a practice project under the Udacity's Andoird NanoD program.

# Popular Movies App

This app was submitted on July 13th 2015

# What does the app do

I have tried to comment everyline of code for easy understanding of the process.

1. The main screen showing latest movie posters from the The Movie Database.
2. OnClick of each poster takes you to the detail page displaying:
    a. Movie Title
    b. Movie Poster
    c. Release date
    d. Average Vote
    e. Overview text

# Topics covered

1. Main Activity (Fragment) *has* GridView *which impliments* MoveAdapter *which is an extention of* BaseAdapter.
2. Main Activity Fragment's OnCreateView *initiates the* FetchMoviesTask (Async Task) *that fethes data from TMDB API and sends it to the* MovieAdapter's addMovies method *on* onPostExecute *to add movies to the* GridView
3. OnItemClick *of* GridView *get the clicked item from* MovieAdapter *which sends over the* TMDBMovie object *in a* Serialized Bundle *through an* Intent *sent to the* DetailActivity (DetailFragment) *which populates the respective* Views.

# General Notes on the movie API

Main URL of the API: http://api.themoviedb.org/3/discover/movie&sort_by=popularity.desc&API_KEY=[Your API Key Here from TMDB]

Every poster image needs to have the main URL as: http://image.tmdb.org/t/p/
Add a size URI like /w185/ which specifies the max width of our image.

Sample poster URL: http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg

Returned JSON object with all the information in it. The following key's are important for us under **results**:

* id: Movie id on TMDB for keepsakes
* original_title: The title of this movie
* overview: A short description of the movie. (1000 or less chars)
* release_date: Release date in format "2015-06-12"
* poster_path: The poster of this movie in the format of "/xxx". This needs to be appended to http://image.tmdb.org/t/p/ with the max width we require, i.e.w185. : http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg
* vote_average: out of /10 average vote rating
* popularity: out of 100

## Next steps after code review from the Nanodegree team: July 13th 2015

onCreateView: Take advantage of savedInstanceState! It won't be null if you use onSaveInstanceState method from Activity class. You can keep important values, such artists' and tracks' list, too rebuild the fragment once the device rotates.

You could have even used 2 columns for your portrait layout, but 2 columns in landscape looks pretty strange. There's a lot of room to use to display the posters! So it would improve your interface if you managed to display at least 3 columns in portrait and up to 5 in landscape :D

How to check device's orientation? http://stackoverflow.com/questions/2795833/check-orientation-on-android-phone

Your request process is right! But right now you are redoing it everytime the activity is recreated (device is rotated, for example). It would be awesome if you implemented a way to save your results to bundle at onSaveInstanceState() and retrieve it at onCreate() or onRestoreInstanceState().

You can take a look at this links:

Serialize a parsable array: http://stackoverflow.com/questions/12503836/how-to-save-custom-arraylist-on-android-screen-rotate

Impliment a Parsable Object: http://developer.android.com/reference/android/os/Parcelable.html

## Note on JSON Parsing using Retrifit
http://square.github.io/retrofit/

It requires 3 types of files:

### 1. A Rest interface
It's a file that will have all your endpoints as java methods signatures. It will be created automagically by Retrofit later, all you have to do is to tell its name, arguments and return type.

It doesn't matter which name you choose. You will use annotations to tell the route you want that method to access, so it would look something like:

```java
public interface GitHubService {
  @GET("/users/{user}/repos")
  List<Repo> listRepos(@Path("user") String user);
}
```

Where @GET is an annotation to use this method as a GET request, List<Repo> is the type of object listRepos() will return and @Path() is an annotation to replace {user} at the @GET path by a String given in String user argument.

###2. An implementation
After having your interface declared, you will need to implement that. It's really easy, you can create a separate class (I like to call it Webservice.java) and use:

```java
Retrofit retrofit = new Retrofit.Builder()
    .setEndpoint("https://api.github.com")
    .build();
```

```java
GitHubService service = retrofit.create(GitHubService.class);
```

Now, you have a variable service that accounts for every single method you have declared in your interface. You can access all of them, they will work!

```java
List<Repo> repos = service.listRepos("octocat");
```

###3. Models
You can see the example above uses a Repo class. It is the representation of the returning JSON data. Remind yourself that JSON will be serialized to Java as following:
Objects becomes Custom Objects
Arrays = ArrayList<T>
Integers = Integer
Strings = String
Null = null

So a Repo class is a representation of a returning JSON source, but in Java. It makes your app looks very organized and help you to build better network interfaces.

For more information, I recommend taking a look at the official documentation. There's a lot of examples there that you could take advantage of!
​
