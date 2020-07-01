import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SearchResults extends JPanel{
    private static JButton[] results, additionalResults;
    private static String query;
    private static String[] queryResults;
    private JLabel errorText;

    public SearchResults(){
        errorText = new JLabel("No Results Found");
        errorText.setSize(580,30);
    }


    //gets results from sql database
    public static void updateResults(String song , String artist){
        //the Q marks to the server that is this a query
        query =  "QSELECT Songid, Title, Artist FROM Songs WHERE Title LIKE \'"+song+"%\' AND Artist LIKE \'"+ artist +"%\';";
        System.out.println(query);
        //send query to server
        ClientGui.client.sendQuery(query);
    }

    //set and format results
    public void setResults(String sql){
        //split into pairs of song and artist
        queryResults=sql.split(";;");
    }

    //displays results
    public void displayResults(boolean wipe){
        //if first packet
        if(wipe) {
            //wipe current display of results
            this.removeAll();
            this.revalidate();
            results = new JButton[queryResults.length];
            //resize screen
            this.setPreferredSize(new Dimension(580, 30 * results.length));
            if (results.length < 20) {
                this.setLayout(new GridLayout(20, 1));
            } else {
                this.setLayout(new GridLayout(results.length, 1));
            }
            //add each button
            if (!queryResults[0].equals("ERROR")) {
                for (int i = 0; i < results.length; i++) {
                    //display text
                    System.out.println(queryResults[i]);
                    results[i] = new JButton(queryResults[i].split(">")[0]);
                    //sets name of button to song id
                    results[i].setName(queryResults[i].split(">")[1]);
                    System.out.println("ID: " + queryResults[i].split(">")[1]);
                    results[i].setSize(580, 30);
                    results[i].addActionListener(onclick);
                    this.add(results[i]);
                }
            } else {
                this.add(errorText);
            }
            this.revalidate();
            repaint();
        }else{
            //wipe screen
            this.removeAll();
            this.revalidate();
            additionalResults = new JButton[results.length+queryResults.length];
            //resize screen
            this.setPreferredSize(new Dimension(580, 30 * results.length));
            if (results.length < 20) {
                this.setLayout(new GridLayout(20, 1));
            } else {
                this.setLayout(new GridLayout(results.length, 1));
            }
            if (!queryResults[0].equals("ERROR")) {
                 //add previous results back to screen
                for(int i=0;i<results.length;i++){
                    //display text
                    additionalResults[i] = new JButton(results[i].getText());
                    //name button
                    additionalResults[i].setName(results[i].getName());
                    //size
                    additionalResults[i].setSize(580,30);
                    //functionality
                    additionalResults[i].addActionListener(onclick);
                    this.add(additionalResults[i]);
                }
                //add new results buttons
                for (int i = 0; i < queryResults.length; i++) {
                    //display text
                    System.out.println(queryResults[i]);
                    additionalResults[i+results.length] = new JButton(queryResults[i].split(">")[0]);
                    //sets name of button to song id
                    additionalResults[i+results.length].setName(queryResults[i].split(">")[1]);
                    System.out.println("ID: " + queryResults[i].split(">")[1]);
                    additionalResults[i+results.length].setSize(580, 30);
                    additionalResults[i+results.length].addActionListener(onclick);
                    this.add(additionalResults[i+results.length]);
                }
            } else {
                this.add(errorText);
            }
            this.revalidate();
            repaint();
            results=additionalResults.clone();



        }

    }


    public ActionListener onclick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            ClientGui.client.pause();
            //adds length of id as prefix to fix bug on server
            ClientGui.client.sendPlayRequest(clicked.getName().toCharArray().length+""+clicked.getName());

        }
    };



}
