package model.list;

// self packages
import model.movie.Movie;
import dataio.DataIO;

// java packages
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieLibrary implements Iterable<Movie> {
    protected List<Movie> movieList;

    /**
     * Default constructor. Can only be called by the subclass MovieCollection
     */
    protected MovieLibrary() {
        // linked list for faster insert/remove
        this.movieList = new ArrayList<>();
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
     * @return copy of list of movies
     */
    public List<Movie> getMovieList() {
        // return a copy
        return List.copyOf(this.movieList);
    }

    /**
     * return size of the movie library
     * @return size
     */
    public int getSize() {
        return this.movieList.size();
    }

    @Override
    public Iterator<Movie> iterator() {
        Iterator<Movie> it = new Iterator<Movie>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < getSize();
            }

            @Override
            public Movie next() {
                return movieList.get(currentIndex++);
            }
        };
        return it;
    }

}
