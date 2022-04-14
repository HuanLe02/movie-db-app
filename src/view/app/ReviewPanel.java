package view.app;

import model.movie.Review;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReviewPanel extends JPanel {

    private final MovieInfoDialog parentDialog;
    private final AppFrame parentFrame;

    private final JButton addReviewButton = new JButton("Add a Review");

    // labels
    private final JLabel avgScoreLabel = new JLabel();
    private final JPanel commentsPanel = new JPanel();

    /**
     * Constructor
     * @param parentDialog: MovieInfoDialog that owns this panel
     * @param parentFrame: parent frame (AppFrame) that owns parentDialog
     */
    ReviewPanel(MovieInfoDialog parentDialog, AppFrame parentFrame){
        this.parentDialog = parentDialog;
        this.parentFrame = parentFrame;

        // set layout
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.PAGE_AXIS));

        // add elements
        this.add(Box.createVerticalStrut(10));  // padding
        this.add(addReviewButton);
        this.add(Box.createVerticalStrut(20));  // padding
        this.add(avgScoreLabel);
        this.add(commentsPanel);

        // load all reviews
        loadReviews();

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

                // load reviews again
                loadReviews();
            }
        });
    }

    /**
     * Load reviews associated with movie
     */
    private void loadReviews() {
        // get list of reviews associated w/ movie
        List<Review> reviewsList = parentDialog.movie.getReviews();

        // reset comment panel
        commentsPanel.removeAll();

        // no reviews
        if (reviewsList.size() == 0) {
            avgScoreLabel.setText("Average user-rated score: N/A");
            return;
        }

        // calculate to display avg score and review comments
        double avgScore = 0.0;
        for (Review rev : reviewsList) {   // calculate average score
            avgScore += rev.getUserScore();

            // add labels to comment panels
            commentsPanel.add(Box.createVerticalStrut(10));  // padding
            String text = "User " + rev.getUserName() + " commented: \"" + rev.getUserComment() + "\"";
            JLabel newLabel = new JLabel(text);
            commentsPanel.add(newLabel);
        }

        avgScore = avgScore / reviewsList.size();   // get average
        avgScoreLabel.setText("Average user-rated score: " + String.format("%.2f",avgScore));  // display average
    }

}
