package view.auth;

import model.user.AccountManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ForgetPassDialog extends JDialog {

    /**
     * Constructor
     * @param accManager: AccountManager object to be passed to panel
     * */
   public ForgetPassDialog(AccountManager accManager) {
       // home container
       JPanel homeContainer = new JPanel();
       homeContainer.setLayout(new BoxLayout(homeContainer, BoxLayout.LINE_AXIS));

       // add ResetPassPanel
       homeContainer.add(Box.createHorizontalStrut(10));
       homeContainer.add(new ForgetPassPanel(accManager, this));
       homeContainer.add(Box.createHorizontalStrut(10));

       // set frame attributes
       this.add(homeContainer);
       this.setResizable(false);
       this.setTitle("Reset Password");
       this.pack();
       this.setLocationRelativeTo(null);
       this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   }

}
