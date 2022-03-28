package view.auth;

import model.user.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgetPassPanel extends JPanel implements ActionListener {
    // account manager
    private final AccountManager accManager;
    private final ForgetPassDialog parentFrame;

    // elements
    private final JLabel descLabel = new JLabel("RESET PASSWORD");
    private final JLabel userLabel = new JLabel("Username");
    private final JTextField userTextField = new JTextField();
    private final JButton nextButton1 = new JButton("Next");
    private final JLabel secQuestionLabel = new JLabel("");
    private final JTextField secAnsField = new JTextField();
    private final JButton nextButton2 = new JButton("Next");
    private final JLabel passwordLabel = new JLabel("New password below:");
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton okButton = new JButton("OK");
    private final JCheckBox showPassword = new JCheckBox("Show Password");
    private final JButton cancelButton = new JButton("Cancel");

    /**
     * Constructor
     */
    ForgetPassPanel(AccountManager accManager, ForgetPassDialog parentFrame) {
        this.accManager = accManager;
        this.parentFrame = parentFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // set font
        descLabel.setFont(new Font("Verdana", Font.BOLD, 15));

        // set visibility, based on if user is logged in or not
        if (!accManager.isLoggedIn()) {  // not logged in yet => forget password
            secQuestionLabel.setVisible(false);
            secAnsField.setVisible(false);
            nextButton2.setVisible(false);
        }
        else {       // if user is already logged in => reset password in program
            userLabel.setVisible(false);
            userTextField.setVisible(false);
            nextButton1.setVisible(false);
            secQuestionLabel.setText(accManager.getCurrentUser().getSecurityQuestion());
        }
        // elements that are always invisible initially
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        showPassword.setVisible(false);
        okButton.setVisible(false);

        // add elements + listeners
        addElements();
        addActionEvent();
    }

    /**
     * Add individual elements to panel
     */
    private void addElements() {
        this.add(descLabel);
        this.add(Box.createVerticalStrut(5));
        this.add(userLabel);
        this.add(userTextField);
        this.add(nextButton1);
        this.add(Box.createVerticalStrut(5));
        this.add(secQuestionLabel);
        this.add(secAnsField);
        this.add(nextButton2);
        this.add(Box.createVerticalStrut(5));
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(showPassword);
        this.add(Box.createVerticalStrut(5));
        this.add(okButton);
        this.add(Box.createVerticalStrut(5));
        this.add(cancelButton);
        this.add(Box.createVerticalStrut(5));
    }

    /**
     * Add listeners to buttons
     */
    private void addActionEvent() {
        nextButton1.addActionListener(this);
        nextButton2.addActionListener(this);
        okButton.addActionListener(this);
        showPassword.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // first Next => show security question
        if (e.getSource() == nextButton1) {
            // verify username exists
            try {
                accManager.forceLogin(userTextField.getText());
            } catch (RuntimeException error) {
                JOptionPane.showMessageDialog(parentFrame,"User does not exist");
                return;
            }
            // disable username field + button
            userTextField.setEditable(false);
            nextButton1.setEnabled(false);
            // retrieve security question => set it visible
            secQuestionLabel.setText(accManager.getCurrentUser().getSecurityQuestion());
            secQuestionLabel.setVisible(true);
            secAnsField.setVisible(true);
            nextButton2.setVisible(true);
            // repack
            parentFrame.pack();
        }
        // second Next => prompt new password
        if (e.getSource() == nextButton2) {
            // verify security answer, if false return
            String answer = secAnsField.getText();
            boolean result = accManager.getCurrentUser().verifySecurityAnswer(answer);

            // return if result is wrong
            if (!result) {
                JOptionPane.showMessageDialog(parentFrame, "Wrong security answer. Try again.");
                return;
            }

            // disable previous field
            secAnsField.setEditable(false);
            nextButton2.setEnabled(false);

            // prompt new password
            passwordLabel.setVisible(true);
            passwordField.setVisible(true);
            showPassword.setVisible(true);
            okButton.setVisible(true);

            // repack
            parentFrame.pack();
        }
        // showPassword JCheckBox
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }
        // OK Button
        if (e.getSource() == okButton) {
            String newPassword = String.valueOf(passwordField.getPassword());

            // precond: currUser not null
            User currUser = accManager.getCurrentUser();
            currUser.setPassword(newPassword);       // set newPassword

            // save current User to file
            accManager.getDataIO().saveUser(currUser);

            // dispose parentFrame
            JOptionPane.showMessageDialog(parentFrame, "Reset password successfully");
            parentFrame.dispose();
        }
        // cancel Button
        if (e.getSource() == cancelButton) {
            // dispose parentFrame
            parentFrame.dispose();
        }
    }
}
