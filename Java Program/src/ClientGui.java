import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientGui extends JFrame {
    public final static int HEIGHT =800;
    public final static int WIDTH = 600;
    private SearchBar searchBar;
    private SearchResults searchResults;
    private ControlsBar controlsBar;
    private GridBagConstraints c;
    public static ClientOutgoing client;


    public ClientGui(){
        //networking
        try {
            client = new ClientOutgoing();
        }catch (IOException e){
            e.printStackTrace();
        }
        //gui
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c =  new GridBagConstraints();
        //add search bar
        searchBar = new SearchBar();
        c.gridx = 0;
        c.gridy=0;
        c.gridwidth=3;
        c.gridheight=1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(searchBar,c);
        //add results
        searchResults = new SearchResults();
        client.setSearchResults(searchResults);
        //adds scroll bars
        JScrollPane s = new JScrollPane(searchResults,20,31);
        s.setPreferredSize(new Dimension(580,600));
        c.gridx = 0;
        c.gridy=1;
        c.gridwidth=3;
        c.gridheight=6;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(s,c);
        //add controls bar
        controlsBar = new ControlsBar();
        c.gridx=0;
        c.gridy=7;
        c.gridwidth=3;
        c.gridheight=1;
        c.fill=GridBagConstraints.HORIZONTAL;
        add(controlsBar,c);
        pack();
        setVisible(true);
    }


    //this will be run if user is the client
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientGui x = new ClientGui();
            }
        });
        /*
        try{
            ServerOutgoing s = new ServerOutgoing();
        }catch (IOException e){
            e.printStackTrace();
        }


         */


    }




}
