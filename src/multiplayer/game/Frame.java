package multiplayer.game;


import javax.swing.JFrame;


public class Frame extends JFrame
{
    private final int SCREEN_WIDTH = 700;
    private final int SCREEN_HEIGHT = 700;
    private Panel doomPanel; //This doomPanel inherits from the JPanel class and is where the 3D graphics happen

    //This constructor sets up the GameFrame object and creates all of the UI.
    public Frame()
    {
        doomPanel = new Panel();
        doomPanel.setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        doomPanel.setVisible(true);
        add(doomPanel);
        setTitle("Doom");
        setVisible(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void update() {
        repaint();
    }

}

