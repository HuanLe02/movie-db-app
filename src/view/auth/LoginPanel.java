package view.auth;

// self packages
import model.user.AccountManager;
import view.LinkButton;

// java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel implements ActionListener {
    // Account Manager
    private final AccountManager accManager;

    // Parent frame
    private final WelcomeFrame parentFrame;

    // max TextField Dimension
    private final Dimension maxTxtFieldDim = new Dimension(300, 30);

    // components (buttons, fields, labels)
    private final JLabel descLabel = new JLabel("SIGN IN");
    private final JLabel userLabel = new JLabel("Username");
    private final JLabel passwordLabel = new JLabel("Password");
    private final JTextField userTextField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Login");
    private final JButton resetButton = new JButton("Reset");
    private final JCheckBox showPassword = new JCheckBox("Show Password");
    private final JButton forgotPassword = new LinkButton("Forget Password?");

    /**
     * Constructor
     */
    LoginPanel(AccountManager accManager, WelcomeFrame parentFrame) {
        this.accManager = accManager;
        this.parentFrame = parentFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // set sizes
        descLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        userTextField.setMaximumSize(maxTxtFieldDim);
        passwordField.setMaximumSize(maxTxtFieldDim);

        addElements();
        addActionEvent();
    }

    /**
     * Add individual elements to panel
     */
    private void addElements() {
        this.add(descLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(userLabel);
        this.add(userTextField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(showPassword);
        this.add(Box.createVerticalStrut(5));
        this.add(loginButton);
        this.add(Box.createVerticalStrut(5));
        this.add(resetButton);
        this.add(Box.createVerticalStrut(5));
        this.add(forgotPassword);
        this.add(Box.createVerticalStrut(5));
    }

    /**
     * Add listeners to buttons
     */
    private void addActionEvent() {
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
        forgotPassword.addActionListener(this);
    }

    /**
     * clear all text fields
     */
    private void clearFields() {
        userTextField.setText("");
        passwordField.setText("");
    }

    /**
     * Action Listeners
     * @param e: ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of LOGIN button
        if (e.getSource() == loginButton) {
            // get inputs from text field
            String usrText = userTextField.getText();
            String pwdText = String.valueOf(passwordField.getPassword());

            // try logging in, show dialog if fail
            try {
                accManager.login(usrText, pwdText);
                assert accManager.isLoggedIn();
                // dialog if success
                JOptionPane.showMessageDialog(parentFrame, "Login Successful");
                // transition to app
                parentFrame.transitionToApp();
            } catch (RuntimeException loginError) {
                JOptionPane.showMessageDialog(parentFrame, loginError.getMessage());
                assert !accManager.isLoggedIn();
            }
        }
        // RESET button
        if (e.getSource() == resetButton) {
            clearFields();
        }
        // showPassword JCheckBox
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }
        // forgotPassword
        if (e.getSource() == forgotPassword) {
            clearFields();

            // new dialog
            ForgetPassDialog dialog = new ForgetPassDialog(accManager, false);
            dialog.setModal(true);
            dialog.setVisible(true);

            // logout temporary user when dialog is closed
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    accManager.logout();
                }
            });
        }
    }
}
