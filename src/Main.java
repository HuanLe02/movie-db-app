// self packages
import dataio.DataIO;
import model.list.*;
import model.movie.Movie;

// java packages
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataIO d = new DataIO();

        System.out.println(d.getConfigFilePath());
        System.out.println(d.getDataDirPath());
        d.setDataDirPath("data/users");
        d.setDataDirPath("data");


        List<Movie> result = d.getMovieLibraryList();
        System.out.println(result.size());

        MovieLibrary mylibrary = new MovieLibrary(d);
        List<Movie> mlist = mylibrary.getMovieList();
        Movie m1 = mlist.get(59);
        Movie m2 = mlist.get(32);
        System.out.println(m1.getTitle());
        System.out.println(m2.getTitle());

        MovieCollection mycollection = new MovieCollection("Collection 1");
        mycollection.addMovie(m1);
        mycollection.addMovie(m1);
        mycollection.deleteMovie(m2);
        mycollection.addMovie(m2);
        mycollection.rename("Collection 2");
        System.out.println(mycollection.getSize());
        System.out.println(mycollection.getName());

    }
}
