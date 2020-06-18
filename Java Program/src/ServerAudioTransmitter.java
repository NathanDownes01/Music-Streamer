import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;

public class ServerAudioTransmitter implements Runnable{
    //tcp networking
    private Socket socket;
    private OutputStream output;
    private int maxLength = ServerOutgoing.maxLength;

    //audio
    private byte[] buffer;
    private int numRead;
    private AudioInputStream ais;
    private File song;
    private String id;
    private int length;
    private boolean paused;
    private volatile int totalSent;



    public ServerAudioTransmitter(ServerOutgoing server){
        this.socket = server.audioSocket;
        output = server.audioOutput;
        paused = false;
    }

    public void loadAudio(String instruction){
        //received integers aren't interpreted as ints, this fixes that
        length = Character.getNumericValue(instruction.charAt(0));
        id="";
        for(int i=0;i<length;i++) {
            id += (""+ Character.getNumericValue(instruction.charAt(i+1)));
        }
        String path = id+".wav";
        song = new File( "/Documents",path);
        try {
            System.out.println(song.getCanonicalPath());
        }catch (IOException e){
            e.printStackTrace();
        }
        totalSent=0;
    }

    public void pause(boolean tof){
        paused=tof;
        if(!paused){
            buffer = new byte[maxLength];
        }
    }

    public boolean getPaused(){
        return paused;
    }

    public int getTotalSent(){
        return totalSent;
    }

    @Override
    public synchronized void run() {
        try {
            //gets audio
            ais = AudioSystem.getAudioInputStream(song);
            buffer = new byte[maxLength];
            //reads audio from ais
            while(!paused){
                //send audio to client
                if((numRead = ais.read(buffer,0,buffer.length))>-1) {
                    totalSent+=numRead;
                    output.write(buffer, 0, numRead);
                    output.flush();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
