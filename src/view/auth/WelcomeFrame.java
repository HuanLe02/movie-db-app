package view.auth;

// self packages
import model.user.AccountManager;
import view.app.AppFrame;

// java packages
import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {
    // universal account manager
    private final AccountManager accManager;

    /**
     * construct new Welcome Frame
     * @param accManager: AccountManager object to be passed to panels
     */
    public WelcomeFrame(AccountManager accManager) {
        // precond: user is not logged in
        assert !accManager.isLoggedIn();

        this.accManager = accManager;

        // home container
//        JPanel homeContainer = new JPanel();
//        homeContainer.setLayout(new BoxLayout(homeContainer, BoxLayout.LINE_AXIS));


        // add panels
        JPanel leftPanel = new LoginPanel(this);
        JPanel rightPanel = new SignupPanel(this);

        // split pane
        JSplitPane homeContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);

        // frame attributes
        this.add(homeContainer);
        this.setResizable(false);
        this.setTitle("Welcome!");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * @return current AccountManager
     */
    AccountManager currManager() {
        return this.accManager;
    }

    /**
     * Transition to app view
     */
    void transitionToApp() {
        // new Frame
        JFrame appFrame = new AppFrame(accManager);
        appFrame.setVisible(true);

        // dispose this frame
        this.dispose();
    }



}
