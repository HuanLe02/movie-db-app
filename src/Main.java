// self packages
import dataio.DataIO;
import model.list.MovieCollection;
import model.movie.Movie;
import model.list.MovieLibrary;
import model.user.*;
import view.auth.WelcomeFrame;

// java packages
import java.util.*;
import java.util.Timer;

import javax.swing.*;

public class Main {
    final private static DataIO dataIO = new DataIO();
    final private static AccountManager manager = new AccountManager(dataIO);


    /**
     * Helper function to automatically save user data
     */
    private static void autosave() {
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                User currentUser = manager.getCurrentUser();
                if (currentUser != null) {
                    dataIO.saveUser(currentUser);     // save user
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
//        // background thread, autosave user data
//        autosave();
//
//        // load data from library
//        final MovieLibrary library = new MovieLibrary(dataIO);
//        System.out.printf("Movie library loaded with %d items.\n", library.getSize());
//
//        System.out.println(library.get(0).getTitle());
//        manager.createAccount("dummy1", "pass", "abcd", "abcde", false);

        WelcomeFrame frame = new WelcomeFrame(manager);
        frame.setVisible(true);


//        System.exit(0);
    }
}
