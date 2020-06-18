import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ClientOutgoing {
    private byte buffer[];
    private ClientIncoming receiver;
    //tcp network
    public Socket socket;
    public OutputStream output;
    public InputStream input;
    public static final int port = 8111;
    public InetAddress ip;
    private SearchResults searchResults;
    //tcp test
    public static final int audioPort =8112;
    public Socket audioSocket;
    public InputStream audioInput;
    private ClientAudioIncoming music;

    //this will change to public after port forward
    private final String serverIp = "86.10.52.77";


    //establish connection and start receiver
    public ClientOutgoing() throws IOException {
        //establish server ip
        ip = InetAddress.getByName(serverIp);
        establishConnection();
        try {
            receiver = new ClientIncoming(this);
            Thread receivingThread = new Thread(receiver);
            receivingThread.start();
            music = new ClientAudioIncoming(this);
            Thread musicThread = new Thread(music);
            musicThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        //testing file play
        /*
        buffer = "Play".getBytes();
        output.write(buffer,0,buffer.length);
        output.flush();

         */
    }

    public void establishConnection() throws IOException{
        //default comms channel
        System.out.println("Client waiting");
        socket = new Socket(ip,port);
        output = socket.getOutputStream();
        output.flush();
        input=socket.getInputStream();
        System.out.println("Connected");
        //audio comms channel
        audioSocket= new Socket(ip,audioPort);
        audioInput = audioSocket.getInputStream();
    }

    //displays query results to GUI
    public void displayResults(String instruction,boolean wipe){
        //checks if any results matched search criteria
        //if not first packet
        if(instruction.substring(0,2).equals(";;")){
            instruction = instruction.substring(2);
            searchResults.setResults(instruction);
        }
        //if first packet
        else{ if(instruction.length()>=8) {
            //remove "RESULTS-" from front of string
            instruction = instruction.substring(9);
            //using this setter, allows for swing thread safe update
            searchResults.setResults(instruction);
        }
            //if no results
             else{
            //can be split into error and type
            searchResults.setResults("ERROR;;404");
        }}
        //update display
        searchResults.displayResults(wipe);
    }

    //pauses music
    public void pause(){
        music.pause(true);
    }

    public void unpause(String total){
        //music.pause(false);
        music.setTotal(total);
    }

    public void toggleUserPause(boolean tof){
        music.toggleUserPause(tof);
    }
    public boolean getUserPause(){
        return music.getUserPause();
    }


    //used to avoid static context issues
    public void setSearchResults(SearchResults s){
        searchResults=s;
    }

    //sends query to server
    public void sendQuery(String query){
        try {
            output.write(query.getBytes(), 0, query.length());
            output.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendPlayRequest(String instruction){
        instruction = "PLAY" + instruction;
        System.out.println(instruction);
        try {
            output.write(instruction.getBytes(), 0, instruction.length());
            output.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

    }



}
