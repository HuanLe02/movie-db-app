package view.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReviewPanel extends JPanel {

    private final MovieInfoDialog parentDialog;
    private final AppFrame parentFrame;

    private final JButton addReviewButton = new JButton("Add a Review");

    ReviewPanel(MovieInfoDialog parentDialog, AppFrame parentFrame){
        this.parentDialog = parentDialog;
        this.parentFrame = parentFrame;

        this.add(addReviewButton);
        // add listener
        addReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog newDialog = new AddReviewDialog(parentDialog.movie, parentFrame.currUser().getUsername());
                newDialog.setModal(true);
                newDialog.setMinimumSize(new Dimension(400,150));
                newDialog.setLocationRelativeTo(null);
                newDialog.setVisible(true);
                newDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });

    }

}
