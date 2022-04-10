package view.app;

import model.movie.Movie;
import model.movie.Reviews;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddReviewDialog extends JDialog {
    // home container
    private final JPanel homeContainer = new JPanel();

    // fields
    private final Movie mov;
    private final String userName;

    // labels and buttons
    private final Double[] scores = {0.,1.,2.,3.,4.,5.,6.,7.,8.,9.,10.};
    private final JLabel scoreLabel = new JLabel("Select a score");
    private final JComboBox selectScore = new JComboBox(scores);
    private final JLabel commentLabel = new JLabel("Add your comment");
    private final JTextField commentTextField = new JTextField();
    private final JButton submitButton = new JButton("Submit");

    AddReviewDialog(Movie mov, String userName) {
        // initialize internal fields
        this.mov = mov;
        this.userName = userName;

        // layout
        homeContainer.setLayout(new BoxLayout(homeContainer, BoxLayout.PAGE_AXIS));

        // add elements
        homeContainer.add(scoreLabel);
        homeContainer.add(selectScore);
        homeContainer.add(commentLabel);
        homeContainer.add(commentTextField);
        homeContainer.add(submitButton);

        // add event listener for button
        addListener();

        // add homeContainer
        this.add(homeContainer);
        this.setTitle("Add a review");
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * dispose dialog
     */
    void disposeDialog() {
        this.dispose();
    }

    /**
     * add listener
     */
    private void addListener() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentTextField.getText();
                // if comment is blank, warn
                if (comment.isBlank()) {
                    JOptionPane.showMessageDialog(homeContainer, "Cannot enter blank comment");
                    return;
                }
                Reviews review = new Reviews((String) mov.get("imdbID"), userName,
                        (Double) selectScore.getSelectedItem(), comment);
                // call addReview on movie
                mov.addReview(review);
                // close dialog
                disposeDialog();
            }
        });
    }
}
