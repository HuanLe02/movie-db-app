package view.app;

import model.list.MovieLibrary;
import model.movie.Movie;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LibraryPanel extends JPanel {

    // core MovieLibrary object
    protected final MovieLibrary library;
    // core list (to be displayed)
    protected List<Movie> coreList;

    // parent frame
    protected AppFrame parentFrame;

    // elements
    protected final JLabel descLabel = new JLabel("MOVIES IN LIBRARY");
    String[] columns = {"imdbID", "Title", "Year", "Genre", "Actors", "imdbRating", "Metascore", "Rated"};   // column titles
    protected final DefaultTableModel tableModel;
    protected final JTable tableView;

    // filter text
    protected String titleFilterText = "";
    protected String genreFilterText = "";
    protected String actorFilterText = "";

    // option pane elements
    protected final JPanel optionPane = new JPanel();
    protected final JLabel nameLabel = new JLabel();
    protected final JButton renameButton = new JButton("Rename Collection");
    protected final JLabel titleKeywordLabel = new JLabel("Title Keywords:");
    protected final JTextField titleKeywordField = new JTextField();
    protected final JLabel genreLabel = new JLabel("Including Genre(s), seperated by commas");
    protected final JTextField genreField = new JTextField();
    protected final JLabel actorLabel = new JLabel("Including Performer(s), seperated by commas");
    protected final JTextField actorField = new JTextField();
    protected final JButton resetSearchButton = new JButton("Reset Search");
    protected final JButton clearSelectionButton = new JButton("Clear Selection");
    protected final JButton viewMovieButton = new JButton("View Movie Info");
    protected final JButton addMovieButton = new JButton("Add movie to collection...");
    protected final JButton removeMovieButton = new JButton("Remove movie from collection...");

    /**
     * Constructor
     * @param parentFrame frame that panel belongs to
     */
    protected LibraryPanel(AppFrame parentFrame) {
        // initialize fields
        this.parentFrame = parentFrame;
        this.library = parentFrame.currLibrary();
        this.coreList = library.toList();

        // set alignment
        descLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // create table
        tableModel = new DefaultTableModel(columns, 0);
        this.tableView = new JTable(tableModel) {
            // disable cell editing
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };

        // set data for table
        setTableData();

        // settings for tableView
        tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);   // only allow single selection
        tableView.setRowSelectionAllowed(true);
        tableView.getTableHeader().setReorderingAllowed(false);
        tableView.setAutoCreateRowSorter(true);
        tableView.setUpdateSelectionOnSort(false);

        // column width
        tableView.getColumn(columns[0]).setMinWidth(75);
        tableView.getColumn(columns[0]).setMaxWidth(80);
        tableView.getColumn(columns[1]).setMinWidth(350);
        tableView.getColumn(columns[1]).setMaxWidth(355);
        tableView.getColumn(columns[2]).setMinWidth(50);
        tableView.getColumn(columns[2]).setMaxWidth(55);
        tableView.getColumn(columns[3]).setMinWidth(200);
        tableView.getColumn(columns[3]).setMaxWidth(205);
        tableView.getColumn(columns[4]).setMinWidth(375);
        tableView.getColumn(columns[4]).setMaxWidth(380);
        tableView.getColumn(columns[5]).setMinWidth(75);
        tableView.getColumn(columns[5]).setMaxWidth(76);
        tableView.getColumn(columns[6]).setMinWidth(70);
        tableView.getColumn(columns[6]).setMaxWidth(75);
        tableView.getColumn(columns[7]).setMinWidth(50);
        tableView.getColumn(columns[7]).setMaxWidth(55);

        // wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(tableView);

        // right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(descLabel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        rightPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        rightPanel.add(Box.createHorizontalStrut(10), BorderLayout.SOUTH);

        // setup optionPane
        optionPane.setLayout(new BoxLayout(optionPane, BoxLayout.PAGE_AXIS));
        optionPane.add(Box.createVerticalStrut(20));
        optionPane.add(nameLabel);
        nameLabel.setText("AAA");
        optionPane.add(renameButton);
        optionPane.add(Box.createVerticalStrut(50));
        optionPane.add(titleKeywordLabel);
        optionPane.add(titleKeywordField);
        optionPane.add(Box.createVerticalStrut(20));
        optionPane.add(genreLabel);
        optionPane.add(genreField);
        optionPane.add(Box.createVerticalStrut(20));
        optionPane.add(actorLabel);
        optionPane.add(actorField);
        optionPane.add(Box.createVerticalStrut(100));
        optionPane.add(resetSearchButton);
        optionPane.add(Box.createVerticalStrut(20));
        optionPane.add(clearSelectionButton);
        optionPane.add(Box.createVerticalStrut(20));
        optionPane.add(viewMovieButton);
        optionPane.add(Box.createVerticalStrut(20));
        optionPane.add(addMovieButton);
        optionPane.add(Box.createVerticalStrut(20));
        optionPane.add(removeMovieButton);

        // set visibility
        nameLabel.setVisible(false);
        renameButton.setVisible(false);
        renameButton.setEnabled(false);
        removeMovieButton.setVisible(false);
        removeMovieButton.setEnabled(false);

        // add table listener
        addTableListener();

        // Document Listeners for text fields
        addFieldDocumentListeners();

        // add button listeners
        addButtonListeners();

        // left panel = option pane
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout());
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(optionPane);
        leftPanel.add(Box.createHorizontalStrut(10));

        // set general layout
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(rightPanel);
        this.add(leftPanel);
    }

    /**
     * add listener to tableView
     */
    protected void addTableListener() {
        tableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {   // check double click
                    // popup movie info dialog, if a row is selected
                    popupMovieInfoDialog();
                }
            }
        });
    }

    /**
     * add listeners to text fields
     */
    protected void addFieldDocumentListeners() {
        titleKeywordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // update filter text
                titleFilterText = titleKeywordField.getText();
                setTableData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // update filter text
                titleFilterText = titleKeywordField.getText();
                setTableData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // update filter text
                titleFilterText = titleKeywordField.getText();
                setTableData();
            }
        });
        genreField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // update filter text
                genreFilterText = genreField.getText();
                setTableData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // update filter text
                genreFilterText = genreField.getText();
                setTableData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // update filter text
                genreFilterText = genreField.getText();
                setTableData();
            }
        });
        actorField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // update filter text
                actorFilterText = actorField.getText();
                setTableData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // update filter text
                actorFilterText = actorField.getText();
                setTableData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // update filter text
                actorFilterText = actorField.getText();
                setTableData();
            }
        });
    }

    /**
     * clear text fields
     */
    protected void clearTextFields() {
        titleKeywordField.setText("");
        genreField.setText("");
        actorField.setText("");
    }

    /**
     * select ID from table view
     */
    protected String selectedID() {
        // selected row index
        int selectedRowIndex = tableView.getSelectedRow();

        // if no row selected
        if (selectedRowIndex < 0) {
            return null;
        }

        // return selected imdbID (at column 0 of selectedRowIndex)
        return (String) tableView.getValueAt(selectedRowIndex, 0);
    }

    /**
     * add listeners to button
     */
    protected void addButtonListeners() {
        // ADD MOVIE TO COLLECTION BUTTON
        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedID = selectedID();

                // add to collection
                if (selectedID != null) addMovieToCollection(selectedID());
                else {
                    JOptionPane.showMessageDialog(parentFrame, "Please select a movie from list to add");
                }
            }
        });

        // RESET SEARCH
        resetSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear selection in table
                tableView.getSelectionModel().clearSelection();

                // clear text fields => also clear filter text and reset table
                clearTextFields();
            }
        });

        // CLEAR CURRENT SELECTION
        clearSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear selection in table
                tableView.getSelectionModel().clearSelection();
            }
        });

        // VIEW MOVIE INFO
        viewMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // popup dialog
                popupMovieInfoDialog();
            }
        });
    }

    /**
     * popup movie info dialog, of selected movie
     */
    protected void popupMovieInfoDialog() {
        String selectedID = selectedID();

        // if user has not selected any => popup message & return early
        if (selectedID == null) {
            JOptionPane.showMessageDialog(parentFrame, "Please select a movie from list to add");
        }
        else {  // if not null
            // Movie of selected ID
            Movie mov = this.library.getMovie(selectedID);
            // new dialog
            MovieInfoDialog newDialog = new MovieInfoDialog(mov);
            newDialog.setTitle("Movie Info");
            newDialog.setMinimumSize(new Dimension(200,200));
            newDialog.setLocationRelativeTo(null);
            newDialog.pack();
            // popup this new dialog
            newDialog.setModal(true);
            newDialog.setVisible(true);
            newDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    /**
     * add a movie with imdbID to collection
     * @param imdbID: String of imdbID of Movie
     */
    private void addMovieToCollection(String imdbID) {
        // pop up to select collection
        String selectedCollectionName = parentFrame.selectCollection();

        // if no input => return
        if (selectedCollectionName == null) return;

        // add movie to collection in user
        try{
            parentFrame.currUser().getCollection(selectedCollectionName).addMovie(imdbID);
            JOptionPane.showMessageDialog(parentFrame, "Movie added to collection");
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(parentFrame, e.getMessage());
        }
    }

    /**
     * set data tableView with filtered coreList
     * NOTE: title filter is AND, genre/actor filters are OR
     */
    protected void setTableData() {
        // clear rows
        tableModel.setRowCount(0);

        // filter coreList based on title
        String[] title_tokens = titleFilterText.toLowerCase().split(" ");
        List<Movie> tempList = coreList.stream().filter(movie ->
        {
            String title = ((String) movie.get("Title")).toLowerCase();
            // check that title contains ALL the tokens
            for (String token : title_tokens) {
                if (!title.contains(token)) return false;
            }
            return true;
        }).toList();

        // pipeline to genre filter
        // tokenize filter text (,)
        String[] genre_tokens = genreFilterText.toLowerCase().split(" *, *");
        tempList = tempList.stream().filter(movie ->
        {
            String genres = ((String) movie.get("Genre")).toLowerCase();
            // check that movie genre contains AT LEAST ONE input token
            for (String token : genre_tokens) {
                if (genres.contains(token)) return true;
            }
            return false;
        }).toList();

        // pipeline to actor filter
        // tokenize filter text (,)
        String[] actor_tokens = actorFilterText.toLowerCase().split(" *, *");
        tempList = tempList.stream().filter(movie ->
        {
            String genres = ((String) movie.get("Actors")).toLowerCase();
            // check that movie genre contains AT LEAST ONE input token
            for (String token : actor_tokens) {
                if (genres.contains(token)) return true;
            }
            return false;
        }).toList();

        // add rows to the end
        for (Movie mov : tempList) {
            tableModel.insertRow(tableModel.getRowCount(), new Object[] {
                    mov.get(columns[0]),
                    mov.get(columns[1]),
                    Integer.valueOf((String) mov.get(columns[2])),
                    mov.get(columns[3]),
                    mov.get(columns[4]),
                    Double.valueOf((String) mov.get(columns[5])),
                    mov.get(columns[6]),
                    mov.get(columns[7])
            });
        }
    }
}