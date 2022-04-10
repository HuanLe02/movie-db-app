package view.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReviewPanel extends JPanel implements ActionListener{

    private final MovieInfoDialog parentDialog;

    ReviewPanel(MovieInfoDialog parentDialog){
        this.parentDialog = parentDialog;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
