import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//holds pause play button and duration bar
public class ControlsBar extends JPanel {
    private JButton pause;

    public ControlsBar(){
        setPreferredSize(new Dimension(580,100));
        setOpaque(true);
        pause = new JButton();
        pause.setOpaque(false);
        pause.setFocusPainted(false);
        pause.setBorderPainted(false);
        pause.setContentAreaFilled(false);
        pause.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        pause.setIcon(new ImageIcon("res/pause.png"));
        pause.setPreferredSize(new Dimension(100,100));
        pause.addActionListener(pausePlay);
        add(pause);

    }

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
