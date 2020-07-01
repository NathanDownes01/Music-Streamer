import javax.imageio.IIOException;
import java.io.IOException;

public class ServerCore {
    public ServerOutgoing server;

    public ServerCore(){
        try {
            server = new ServerOutgoing();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //this will be run if user is the server
    public static void main(String[] args) {
        ServerCore s = new ServerCore();




    }




}
