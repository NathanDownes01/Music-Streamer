import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

//holds pause play button and duration bar
public class ControlsBar extends JPanel {
    private JButton pause;
    private static JSlider durationSlider;
    private static Hashtable labelTable;
    private JSlider volumeSlider;
    private Timer animation;
    private GridBagConstraints c;


    public ControlsBar(){
        setPreferredSize(new Dimension(580,120));
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        setOpaque(true);
        //pause button
        pause = new JButton();
        pause.setOpaque(false);
        pause.setFocusPainted(false);
        pause.setBorderPainted(false);
        pause.setContentAreaFilled(false);
        pause.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        pause.setIcon(new ImageIcon("res/pause.png"));
        pause.setPreferredSize(new Dimension(80,80));
        pause.addActionListener(pausePlay);
        //button gui
        c.gridx=27;
        c.gridy=0;
        c.gridheight=3;
        c.gridwidth=4;
        c.weightx=1;
        add(pause,c);
        //song slider labels
        labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0:00"));
        //song slider
        durationSlider = new JSlider(JSlider.HORIZONTAL, 0,0,0);
        durationSlider.setLabelTable(labelTable);
        durationSlider.setPaintTicks(true);
        durationSlider.setPaintLabels(true);
        durationSlider.addChangeListener(skipToPart);
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=57;
        c.gridheight=1;
        c.weightx=0;
        c.fill=GridBagConstraints.HORIZONTAL;
        add(durationSlider,c);
        //volume slider
        volumeSlider = new JSlider(JSlider.VERTICAL, (int)ClientGui.client.getMinVol(),(int)ClientGui.client.getMaxVol(),
                (int)ClientGui.client.getCurrentVol());
        durationSlider.setPaintTicks(false);
        volumeSlider.addChangeListener(changeVolume);
        c.gridx=57;
        c.gridy=0;
        c.gridheight=4;
        c.gridwidth=1;
        c.weightx=0;
        c.weighty=1;
        c.fill = GridBagConstraints.VERTICAL;
        add(volumeSlider,c);
        //animation for duration bar
        animation = new Timer(1000,animator);
        animation.start();


    }


    //called when new song starts playing
    public static void setMaxDuration(int max){
        durationSlider.setPaintLabels(false);
        durationSlider.setValueIsAdjusting(true);
        durationSlider.setMaximum(max);
        durationSlider.setValue(0);
        //update labels
        labelTable.clear();
        labelTable.put(0, new JLabel("0:00"));
        labelTable.put(durationSlider.getMaximum(),
                new JLabel(durationSlider.getMaximum()/60+":"+durationSlider.getMaximum()%60));
        durationSlider.setLabelTable(labelTable);
        durationSlider.setPaintLabels(true);
    }

    //allows user to skip/rewind part of song
    private ChangeListener skipToPart = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider skip = (JSlider)e.getSource();
            if(!skip.getValueIsAdjusting()) {
                ClientGui.client.pause();
                ClientGui.client.skipToPart(skip.getValue());
            }
        }
    };


    //animation code
    private ActionListener animator = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!ClientGui.client.getUserPause()&&durationSlider.getValue()<durationSlider.getMaximum()){
                //stops change listener from triggering
                durationSlider.setValueIsAdjusting(true);
                //increments
                durationSlider.setValue(durationSlider.getValue()+1);
                //update labels
                labelTable.clear();
                //how far in label
                if(durationSlider.getValue()%60>9) {
                    labelTable.put(0,
                            new JLabel(durationSlider.getValue() / 60 + ":" + durationSlider.getValue() % 60));
                }else{
                    labelTable.put(0,
                            new JLabel(durationSlider.getValue() / 60 + ":0" + durationSlider.getValue() % 60));
                }
                //end label
                if(durationSlider.getMaximum()%60>9) {
                    labelTable.put(durationSlider.getMaximum(),
                            new JLabel(durationSlider.getMaximum() / 60 + ":" + durationSlider.getMaximum() % 60));
                }else{
                    labelTable.put(durationSlider.getMaximum(),
                            new JLabel(durationSlider.getMaximum() / 60 + ":0" + durationSlider.getMaximum() % 60));
                }

                durationSlider.setLabelTable(labelTable);
                durationSlider.setPaintLabels(true);

            }



        }
    };


    //adds volume functionality to volume slider
    private ChangeListener changeVolume = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider volume = (JSlider)e.getSource();
            ClientGui.client.setVoume(volume.getValue());
        }
    };

    //toggle pause play
    private ActionListener pausePlay = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(ClientGui.client.getUserPause()) {
                ClientGui.client.toggleUserPause(false);
            }else{
                ClientGui.client.toggleUserPause(true);
            }
        }
    };

}
