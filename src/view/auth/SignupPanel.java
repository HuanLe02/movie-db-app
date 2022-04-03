package view.auth;

// self packages
import model.user.AccountManager;
import view.LinkButton;

// java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignupPanel extends JPanel implements ActionListener {
    // Account Manager
    private final AccountManager accManager;

    // max TextField Dimension
//    private final Dimension maxTxtFieldDim = new Dimension(300, 30);

    // List of security questions
    private final String[] questionList = {"In what city were you born?", "What is your mother's maiden name?",
                                           "What high school did you attend?", "What is the make of your first car?",
                                           "What is the name of your first school?",
                                           "What is your favorite food?"};

    // components (buttons, fields, labels)
    private final JLabel descLabel = new JLabel("SIGN UP");
    private final JLabel userLabel = new JLabel("Username");
    private final JLabel passwordLabel = new JLabel("Password");
    private final JLabel secQuesLabel = new JLabel("Security Question");
    private final JLabel secAnsLabel = new JLabel("Security Answer");
    private final JTextField userTextField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JComboBox secQuesMenu = new JComboBox(questionList);
    private final JTextField secAnsField = new JTextField();
    private final JButton signupButton = new JButton("Create Account");
    private final JButton resetButton = new JButton("Reset");
    private final JCheckBox showPassword = new JCheckBox("Show Password");
    private final JCheckBox adminStatus = new JCheckBox("Admin User?");

    // variable, if user is admin
    private boolean isAdmin = false;

    /**
     * Constructor
     */
    SignupPanel(WelcomeFrame parentFrame) {
        this.accManager = parentFrame.currManager();
        this.setLayout(new GridLayout(12,1));

        // set sizes
        descLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add elements and action events
        addElements();
        addActionEvent();
    }

    /**
     * Add individual elements to panel
     */
    private void addElements() {
        this.add(descLabel);
        this.add(userLabel);
        this.add(userTextField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(showPassword);
        this.add(secQuesLabel);
        this.add(secQuesMenu);
        this.add(secAnsLabel);
        this.add(secAnsField);
        this.add(adminStatus);

        // buttonPane
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        buttonPane.add(signupButton);
        buttonPane.add(Box.createHorizontalStrut(10));
        buttonPane.add(resetButton);
        this.add(buttonPane);
    }

    /**
     * Add listeners to buttons
     */
    private void addActionEvent() {
        signupButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
        adminStatus.addActionListener(this);
    }

    /**
     * clear all text fields
     */
    private void clearFields() {
        userTextField.setText("");
        passwordField.setText("");
        secAnsField.setText("");
    }

    /**
     * Action Listeners
     * @param e: ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of SignUp button
        if (e.getSource() == signupButton) {
            // get inputs from text field
            String usrText = userTextField.getText();
            String pwdText = String.valueOf(passwordField.getPassword());
            String secQues = (String) secQuesMenu.getSelectedItem();
            String secAns = secAnsField.getText();

            // check that all fields are filled
            if (usrText.length() * pwdText.length() * secAns.length() == 0) {
                JOptionPane.showMessageDialog(this, "All fields must be filled");
                return;
            }

            // create user
            try {
                accManager.createAccount(usrText, pwdText, secQues, secAns, isAdmin);
                JOptionPane.showMessageDialog(this, "User created. You can login now.");
                clearFields();
            }
            catch (RuntimeException error) {
                JOptionPane.showMessageDialog(this, error.getMessage());
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
        // adminStatus JCheckBox
        if (e.getSource() == adminStatus) {
            if (adminStatus.isSelected()) {
                isAdmin = true;
            } else {
                isAdmin = false;
            }
        }
    }
}