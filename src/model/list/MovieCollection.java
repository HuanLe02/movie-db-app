package model.list;

import model.movie.Movie;

import java.util.LinkedHashSet;
import java.util.Set;


public class MovieCollection {
    // fields
    private String name;
    private final LinkedHashSet<String> movieSet;    // set of all movies (key = imdbID)

    /**
     * Constructor, creating custom collection with given name
     * @param name: name for collection
     */
    public MovieCollection(String name) {
        this.name = name;
        this.movieSet = new LinkedHashSet<>();
    }

    /**
     * get name of collection
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * rename collection
     * @param newName: new name of collection
     */
    public void rename(String newName) {
        this.name = newName;
    }

    /**
     * add a movie w/ imdb to collection
     * @param imdbID: String
     */
    public void addMovie(String imdbID) throws RuntimeException {
        // throws error if movie is in collection
        if (this.movieSet.contains(imdbID)) {
            throw new RuntimeException("Movie already in collection");
        }
        // movie not in set
        this.movieSet.add(imdbID);
    }

    /**
     * remove movie in collection
     * @param imdbID: imdb ID of movie to be removed
     * @precondiion collection has movie with imdbID
     */
    public void removeMovie(String imdbID) {
        this.movieSet.remove(imdbID);
    }

    /**
     * idSet()
     * @return set of movie IDs in collection
     */
    public Set<String> idSet() {
        return new LinkedHashSet<String>(this.movieSet);   // copy of movieSet
    }

}
