package view.app;

// java packages
import model.movie.Movie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MovieInfoPanel extends JPanel implements ActionListener {

    private final MovieInfoDialog parentDialog;

    private final JButton backButton = new JButton("<< Go Back");
    private final JButton addReviewButton = new JButton("Add a Review");

    MovieInfoPanel(MovieInfoDialog parentDialog, Movie mov, AppFrame parentFrame) {
        this.parentDialog = parentDialog;
        this.setLayout(new GridLayout(4, 2));

        // add listener
        addReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog newDialog = new AddReviewDialog(mov, parentFrame.currUser().getUsername());
                newDialog.setModal(true);
                newDialog.setVisible(true);
                newDialog.setLocationRelativeTo(null);
                newDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });

        addElements();
    }

    private void addElements() {
        this.add(backButton);
        this.add(addReviewButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
