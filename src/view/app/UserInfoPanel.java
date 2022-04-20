package view.app;

import dataio.DataIO;
import model.user.AccountManager;
import model.user.User;
import view.LinkButton;
import view.auth.ForgetPassDialog;

// java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInfoPanel extends JPanel implements ActionListener {

    // private account manager
    private final AccountManager accManager;

    // parent frame
    private final AppFrame parentFrame;

    // page elements
    private final JLabel label1 = new JLabel("Username:");
    private final JLabel label2 = new JLabel("Password:");
    private final JLabel label3 = new JLabel("Admin:");
    private final JLabel label4 = new JLabel("# of collections:");
    private final JLabel label5 = new JLabel("Config File Path:");
    private final JLabel label6 = new JLabel("Data Folder Path:");
    private final JLabel userLabel = new JLabel();
    private final JLabel adminLabel = new JLabel();
    private final JLabel collectionNumLabel = new JLabel();
    private final JLabel configLabel = new JLabel();
    private final JLabel pathLabel = new JLabel();
    private final JButton logoutButton = new JButton("Logout");
    private final LinkButton resetPassButton = new LinkButton("Reset Password");
    private final LinkButton resetPathButton = new LinkButton("Reset Path");

    // sub-panels
    private final JPanel left;
    private final JPanel right;

    /**
     * Constructor
     * @param parentFrame: parent frame
     */
    UserInfoPanel(AppFrame parentFrame) {
        assert parentFrame.currManager().isLoggedIn();  // check precondition that user is logged in

        // initialize fields
        this.parentFrame = parentFrame;
        this.accManager = parentFrame.currManager();

        // sub-panels
        left = new JPanel(new GridLayout(8,1, 10, 10));
        right = new JPanel(new GridLayout(8,1, 10, 10));

        // User
        User currUser = parentFrame.currUser();

        // button text alignments
        resetPassButton.setHorizontalAlignment(SwingConstants.LEFT);
        resetPathButton.setHorizontalAlignment(SwingConstants.LEFT);

        // setText for labels
        userLabel.setText(currUser.getUsername());
        adminLabel.setText(currUser.isAdmin() ? "Yes" : "No");
        collectionNumLabel.setText(Integer.toString(currUser.numOfCollections()));
        configLabel.setText(accManager.getDataIO().getConfigFilePath());
        pathLabel.setText(accManager.getDataIO().getDataDirPath());

        // disable reset path button if user is not admin
        if (!currUser.isAdmin()) {
            resetPathButton.setEnabled(false);
            resetPathButton.setVisible(false);
        }

        // set layout
        this.setLayout(new FlowLayout());
        this.add(left);
        this.add(right);

        // add elements + listeners
        addElements();
        addActionEvent();
    }

    /**
     * add elements to panels
     */
    private void addElements() {
        left.add(logoutButton);
        right.add(Box.createGlue());
        left.add(label1);
        right.add(userLabel);
        left.add(label2);
        right.add(resetPassButton);
        left.add(label3);
        right.add(adminLabel);
        left.add(label4);
        right.add(collectionNumLabel);
        left.add(label5);
        right.add(configLabel);
        left.add(label6);
        right.add(pathLabel);
        left.add(Box.createGlue());
        right.add(resetPathButton);
    }

    /**
     * add listener to buttons
     */
    private void addActionEvent() {
        logoutButton.addActionListener(this);
        resetPassButton.addActionListener(this);
        resetPathButton.addActionListener(this);
    }

    /**
     * update information of user
     */
    void updateInfo() {
        int number = this.accManager.getCurrentUser().numOfCollections();
        collectionNumLabel.setText(Integer.toString(number));
    }

    /**
     * Action functions
     * @param e: ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // logout
        if (e.getSource() == logoutButton) {
            accManager.logout();
            parentFrame.backToLogin();   // transition to login page
        }

        // reset password
        if (e.getSource() == resetPassButton) {
            // popup reset password dialog
            ForgetPassDialog newDialog = new ForgetPassDialog(accManager);
            newDialog.setModal(true);
            newDialog.setVisible(true);
        }

        // reset data folder path
        if (e.getSource() == resetPathButton) {
            // popup file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Folder");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);   // select folders only
            int option = fileChooser.showOpenDialog(parentFrame);

            // process folder selected
            if (option == JFileChooser.APPROVE_OPTION) {
                // get path selected
                String folderPath = fileChooser.getSelectedFile().getAbsolutePath();

                // Confirm dialog
                int confirm = JOptionPane.showConfirmDialog(parentFrame,
                        "Program must be restarted after resetting data folder. Do you want to continue?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                // If confirmed as yes
                if (confirm == 0) {
                    // set data folder path
                    JOptionPane.showMessageDialog(parentFrame,"Data folder path is reset. Program will be closed.");
                    accManager.getDataIO().setDataDirPath(folderPath);
                    // PROGRAM IS CLOSED
                }
            }
        }
    }
}