import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchBar extends JPanel {
    private JTextField songSearch,artistSearch;
    private JLabel song,artist;

    public SearchBar(){
        setLayout(new GridLayout(2,2));
        song = new JLabel("Song:");
        artist =  new JLabel("Artist:");
        songSearch= new JTextField();
        artistSearch = new JTextField();
        //adding on search on type feature
        songSearch.addKeyListener(search);
        artistSearch.addKeyListener(search);



        add(song);
        add(artist);
        add(songSearch);
        add(artistSearch);
    }


    private KeyAdapter search = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyTyped(e);
            SearchResults.updateResults(songSearch.getText(), artistSearch.getText());

        }
    };



}
