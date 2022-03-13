// self packages
import dataio.DataIO;
import model.list.*;
import model.movie.Movie;
import model.user.*;

// java packages
import java.util.*;

import com.google.gson.*;

public class Main {
    final private static AccountManager manager = new AccountManager();
    final private static DataIO dataIO = new DataIO();

    public static void autosave() {
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                User currentUser = manager.getCurrentUser();
                if (currentUser != null) {
                    dataIO.saveUser(currentUser);     // save user
                    // System.out.println("User saved.");
                }
//                else {
//                    System.out.println("No user");
//                }
            }
        };
        t.scheduleAtFixedRate(
                task ,
                0,      //delay before first execution
                1000L); //time between executions
    }

    public static void main(String[] args) {
        // background thread, autosaving user data
        autosave();

        // load data from library
        final MovieLibrary library = new MovieLibrary(dataIO);
        System.out.printf("Movie library loaded with %d items.\n", library.getSize());


        System.exit(0);
    }
}
