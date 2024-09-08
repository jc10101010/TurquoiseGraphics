import javax.swing.JFrame;

public class GFrame extends JFrame
{
    public static void main(String[] args) throws InterruptedException {
        //Create new frame and update it
        GFrame frame = new GFrame();
        while (true) {
            Thread.sleep(50);
            frame.update(); 
            //Thread.sleep(5);
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private final int SCREEN_WIDTH = 960;//1920;
    private final int SCREEN_HEIGHT = 540;//1080;
    private GPanel demoPanel; //This demo panel inherits from the JPanel class and is where the 3D graphics happen

    //This constructor sets up the GameFrame object and creates all of the UI.
    public GFrame()
    {
        demoPanel = new GPanel(SCREEN_WIDTH, SCREEN_HEIGHT);
        demoPanel.setBounds(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        demoPanel.setVisible(true);
        add(demoPanel);
        
        setTitle("Turquoise Graphics Engine Demo");
        setVisible(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void update() {
        repaint();
    }

}

