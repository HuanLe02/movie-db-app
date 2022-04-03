package model.list;

import model.movie.Movie;
import dataio.DataIO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MovieLibrary {
    // hash map: key = imdbValue
    private final LinkedHashMap<String, Movie> movieMap;

    /**
     * Constructor, build MovieLibrary object from a DataIO call
     * @param d: DataIO object used
     */
    public MovieLibrary(DataIO d) {
        // call on DataIO to populate list
        List<Movie> movieList= d.getMovieLibraryList();

        // transfer movieList to hashmap
        this.movieMap = new LinkedHashMap<>();    // linked hashmap to preserve order
        for (Movie mov : movieList) {
            movieMap.put((String) mov.get("imdbID"), mov);
        }
    }

    /**
     * get list of movies in library
     * @return list of movies
     */
    public List<Movie> toList() {
        // new list from values in movieMap
        return new ArrayList<>(movieMap.values());
    }

    /**
     * @return movie with imdbID
     * @precondiion library has movie with imdbID
     */
    public Movie getMovie(String imdbID) {
        return this.movieMap.get(imdbID);
    }

    /**
     * return size of the movie library
     * @return size
     */
    public int getSize() {
        return this.movieMap.size();
    }

}
