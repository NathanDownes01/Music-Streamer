import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientAudioIncoming implements Runnable {

    //networking
    private Socket audioSocket;
    private InputStream audioInput;
    //audio
    private SourceDataLine line;
    private DataLine.Info info;
    private AudioFormat format;
    private int buffSize = 176400;
    private int numBytesRead, wipe,totalRead;
    private byte buffer[];
    private volatile int total;
    public static final int maxLength =ClientIncoming.maxLength;
    private volatile boolean paused;
    private volatile boolean userPause;

    public ClientAudioIncoming(ClientOutgoing client){
        this.audioSocket=client.audioSocket;
        this.audioInput = client.audioInput;
        buffer = new byte[maxLength];
        //audio setup
        //format
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100,false);
        //lines
        info = new DataLine.Info(SourceDataLine.class,format);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format,buffSize);
            line.write(new byte[4],0,4);
            line.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        paused = false;
        userPause=false;


    }

    public void pause(boolean tof){
        paused = tof;
    }

    public void setTotal(String total){
        this.total = Integer.parseInt(total);
        if(this.total==0){
            this.total=10;
        }
    }

    public void toggleUserPause(boolean tof){
        userPause=tof;
    }

    public boolean getUserPause(){
        return userPause;
    }


    @Override
    public void run() {
        while(!audioSocket.isClosed()) {
            try {
                if(!paused&&!userPause) {
                    numBytesRead = audioInput.read(buffer, 0, buffer.length);
                    if (numBytesRead != -1) {
                        totalRead+=numBytesRead;
                        line.write(buffer, 0, buffer.length);
                    }
                }else if(paused){
                    //NOTE: eventually this will be changed by a different variable when changing songs,not just pausing
                    //wipe remaining audio from line
                    System.out.println("Paused");
                    line.stop();
                    line.drain();
                    line.flush();
                    line.start();
                    //wait for total data streamed to be sent
                    while(total==0){
                        System.out.println("Waiting");
                    }
                    //wipe buffered stream
                    while(totalRead<total) {
                        System.out.println("Total received: " +total +"\nTotal read: "+totalRead);
                        totalRead += audioInput.read(buffer, 0, buffer.length);
                    }
                    paused = false;
                    userPause=false;
                    totalRead=0;
                    total=0;
                }
                //user pause play feature
                if(userPause&&line.isActive()){
                    line.stop();
                    System.out.println("1");
                }
                if(!userPause&&!line.isActive()){
                    line.start();
                    System.out.println("2");
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
