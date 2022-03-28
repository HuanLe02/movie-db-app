package view.auth;

// self packages
import model.user.AccountManager;
import view.app.AppFrame;

// java packages
import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {
    private final AccountManager accManager;

    /**
     * construct new Welcome Frame
     * @param accManager: AccountManager object to be passed to panels
     */
    public WelcomeFrame(AccountManager accManager) {
        this.accManager = accManager;

        JPanel homeContainer = new JPanel();
        homeContainer.setLayout(new BoxLayout(homeContainer, BoxLayout.LINE_AXIS));

        // add panels
        JPanel leftPanel = new LoginPanel(accManager, this);
        JPanel rightPanel = new SignupPanel(accManager, this);
        homeContainer.add(Box.createHorizontalStrut(10));
        homeContainer.add(leftPanel);
        homeContainer.add(Box.createHorizontalStrut(10));
        homeContainer.add(new JSeparator(SwingConstants.VERTICAL));
        homeContainer.add(Box.createHorizontalStrut(10));
        homeContainer.add(rightPanel);
        homeContainer.add(Box.createHorizontalStrut(10));

        // frame attributes
        this.add(homeContainer);
        this.setResizable(false);
        this.setTitle("Welcome!");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Transition to app view
     */
    void transitionToApp() {
        JFrame appFrame = new AppFrame(accManager);
//        appFrame.pack();
        appFrame.setSize(500,500);
        appFrame.setLocationRelativeTo(null);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setVisible(true);
        this.dispose();
    }



}
