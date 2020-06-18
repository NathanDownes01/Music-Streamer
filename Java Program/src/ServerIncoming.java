import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerIncoming implements Runnable {
    private ServerOutgoing server;
    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream input;
    private int numBytesRead;
    private final int maxLength =ServerOutgoing.maxLength;
    private byte buffer[];
    private String instruction;





    public ServerIncoming(ServerOutgoing server){
        this.server = server;
        this.serverSocket = server.serverSocket;
        this.socket = server.socket;
        this.input = server.input;
    }




    //listens for packets
    @Override
    public void run() {
        buffer = new byte[maxLength];
        while(!socket.isClosed()){
            try{
                //receives data
                numBytesRead=input.read(buffer,0,buffer.length);
                //if data was received
                if(numBytesRead!=-1) {
                    instruction = new String(buffer,0,buffer.length);
                    if (instruction.substring(0, 1).equals("Q")) {
                        server.processQuery(instruction);
                    } else {
                        try {
                            server.sendAudio(instruction);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            buffer = new byte[maxLength];


        }



    }
}
