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

    /**
     * Factory to return an iterator over MovieLibrary
     * @return it: an iterator
     */
    @Override
    public Iterator<Movie> iterator() {
        Iterator<Movie> it = new Iterator<>() {
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

    /**
     * Get a movie from list, also support negative index from the back
     * @param index: index of movie to be selected
     * @return Movie at index
     */
    public Movie get(int index) {
        // convert to positive if negative index
        int newIndex;
        if (index < 0) {
            newIndex = index + getSize();
        }
        else {
            newIndex = index;
        }
        // indexing
        if (newIndex > getSize() || newIndex < 0) {
            return null;
        }
        else {
            return movieList.get(index);
        }
    }

}
