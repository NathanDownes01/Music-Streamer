import javax.sound.sampled.*;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class ClientIncoming implements Runnable {
    private ClientOutgoing client;
    //tcp network
    private InputStream input;
    private Socket socket;
    public static final int maxLength =1024;
    private byte buffer[];
    private int numBytesRead;
    //query
    private String instruction;





    //use the same socket for in and out
    public ClientIncoming(ClientOutgoing client) throws Exception{
        this.client = client;
        this.socket = client.socket;
        this.input = client.input;
    }


    @Override
    public void run() {
        buffer = new byte[maxLength];
        //receiving loop
        while(!socket.isClosed()){
            try{
                numBytesRead = input.read(buffer,0,buffer.length);
                //if data was received
                if(numBytesRead!=-1) {
                    //if query results process query results
                    instruction = new String(buffer, 0, numBytesRead);
                    System.out.println(instruction);
                    //used for unpause when changing songs
                    if(instruction.substring(0,4).equals("PLAY")){
                        client.unpause(instruction.substring(4));
                    }else
                        //for updating list of songs
                        if (instruction.substring(0, 7).equals("RESULTS")) {
                        client.displayResults(instruction,true);
                        }else{
                            client.displayResults(instruction,false);
                        }
                }

            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }




}
