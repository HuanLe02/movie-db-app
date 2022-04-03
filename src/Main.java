import dataio.DataIO;
import model.user.*;
import view.auth.WelcomeFrame;

import java.util.*;

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
//                    System.out.println("User saved");
                }
//                else {
//                    System.out.println("No user");
//                }
            }
        };
        t.scheduleAtFixedRate(
                task ,
                0,      //delay before first execution
                100L); //time between executions (every 100 ms)
    }

    public static void main(String[] args) {
        // background thread, autosave user data
        autosave();
//
//        // load data from library
//        final MovieLibrary library = new MovieLibrary(dataIO);
//        System.out.printf("Movie library loaded with %d items.\n", library.getSize());
//        System.out.println(library.getMovie("tt0298148").getField("Year"));

        WelcomeFrame frame = new WelcomeFrame(manager);
        frame.setVisible(true);


//        System.exit(0);
    }
}
