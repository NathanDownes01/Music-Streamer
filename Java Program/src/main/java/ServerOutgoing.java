import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class ServerOutgoing {
    //tcp
    public final int port = ClientOutgoing.port;
    public ServerSocket serverSocket;
    public Socket socket;
    public OutputStream output;
    public InputStream input;
    private ServerIncoming receiver;
    //tcp test
    public final int audioPort =ClientOutgoing.audioPort;
    public ServerSocket audioServerSocket;
    public Socket audioSocket;
    public OutputStream audioOutput;
    //audio
    public static final int maxLength = ClientIncoming.maxLength;
    private ServerAudioTransmitter transmitter;
    private Thread transmitterThread;
    private int duration;
    private ResultSet durationResult;
    //database
    public static Connection connect;
    private ResultSet resultSet;
    private String results;



    public ServerOutgoing() throws IOException {
        //connect to database
        try {
            connectToDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        //set up server
        serverSocket = new ServerSocket(port, 1000);
        audioServerSocket = new ServerSocket(audioPort,1000);
        establishConnection();
        //set up receiver
        receiver = new ServerIncoming(this);
        transmitter = new ServerAudioTransmitter(this);
        transmitterThread = new Thread(transmitter);
        Thread thread = new Thread(receiver);
        thread.start();
        duration=0;


    }

    public void connectToDatabase() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/Music","root","YES");
        connect.setAutoCommit(true);
        connect.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    //establish connection with client
    public void establishConnection() throws IOException{
        //default comms channel
        System.out.println("waiting");
        socket = serverSocket.accept();
        output = socket.getOutputStream();
        output.flush();
        input = socket.getInputStream();
        System.out.println("Connected");
        //audio comms channel
        audioSocket=audioServerSocket.accept();
        audioOutput=audioSocket.getOutputStream();
        audioOutput.flush();
    }


    //process query then send results
    public void processQuery(String query) {
        System.out.println("Server: "+query);
        try {
            //removes the Q at the start
            query = query.substring(1);
            //processes query
            resultSet = connect.createStatement().executeQuery(query);
            //format for client to receive
            results="RESULTS";
            int counter =0;
            while(resultSet.next()){
                results+=(";;"+resultSet.getString("Title") + " by "+resultSet.getString("Artist")+">"+resultSet.getString("Songid"));
                counter++;
                if(counter>10){
                    //send to client
                    output.write(results.getBytes(),0,results.length());
                    output.flush();
                    System.out.println("results sent: " +results);
                    counter =0;
                    results="";

                }
            }
            if(counter>0||results.equals("RESULTS")) {
                //send to client
                output.write(results.getBytes(), 0, results.length());
                output.flush();
                System.out.println("results sent: " + results);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //loads song and sends over network
    public void sendAudio(String instruction) throws Exception {
        //pauses audio if running
        if(!transmitter.getPaused()){
            transmitter.pause(true);

        }
        //send bytes sent previously (bytes to be ignored client side)
        output.write(("PLAY"+transmitter.getTotalSent()).getBytes());
        //if skipping part of song
        if(instruction.substring(0,4).equals("SKIP")){
            //calc how much to skip
            long skip = (Integer.parseInt(instruction.substring(4).trim()) * transmitter.getSongSize()) / duration;
            System.out.println("Skip Size: "+skip);
            transmitter.reloadAudio(skip);

        }else {
            //remove 'play' header
            instruction = instruction.substring(4);
            transmitter.loadAudio(instruction);
            //get duration of song from database
            durationResult= connect.createStatement().executeQuery
                    ("SELECT Duration FROM Songs WHERE Songid = \'"+transmitter.getCurrentSong()+"\';");
            while(durationResult.next()){
                duration = durationResult.getInt("Duration");
            }
            //send client duration of song
            output.write(("DUR"+duration).getBytes());
            output.flush();
        }
        //restart audio thread
        transmitter.pause(false);
        transmitterThread.stop();
        transmitterThread = new Thread(transmitter);
        transmitterThread.start();

    }





}

