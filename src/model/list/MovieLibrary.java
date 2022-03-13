package model.list;

// self packages
import model.movie.Movie;
import dataio.DataIO;

// java packages
import java.util.LinkedList;
import java.util.List;

public class MovieLibrary {
    protected List<Movie> movieList;

    /**
     * Default constructor. Can only be called by the subclass MovieCollection
     */
    protected MovieLibrary() {
        // linked list for faster insert/remove
        this.movieList = new LinkedList<>();
    }

    /**
     * Constructor, build MovieLibrary object from a DataIO call
     * @param d: DataIO object used
     */
    public MovieLibrary(DataIO d) {
        // call on DataIO to populate list
        this.movieList = d.getMovieLibraryList();
    }

    /**
     * get list of movies in library
     * return a copy
     */
    public List<Movie> getMovieList() {
        // return a copy
        return List.copyOf(this.movieList);
    }

    /**
     * return size of the movie library
     * @return size
     */
    final public int getSize() {
        return this.movieList.size();
    }

}
