import javax.swing.JFrame;

public class GFrame extends JFrame {
    /**
     * Main method to start the application.
     * Creates a new GFrame instance and continuously updates the frame.
     * 
     * @param args Command line arguments (not used).
     * @throws InterruptedException if the thread sleep is interrupted.
     */
    public static void main(String[] args) throws InterruptedException {
        GFrame frame = new GFrame(); //Create a new instance of the frame

        //Infinite loop to continuously update the frame
        while (true) {
            Thread.sleep(50);  //Control frame rate to approximately 20 frames per second (50 ms delay)
            frame.update();    //Call the update method to refresh the frame
        }
    }

    //Screen width and height for the window
    private final int SCREEN_WIDTH = 960;
    private final int SCREEN_HEIGHT = 540;
    
    //Custom JPanel where all game graphics will be rendered
    private GPanel demoPanel;

    /**
     * Constructor to set up the frame and its components.
     * Initializes the game panel, sets up the frame's size, title, and visibility.
     */
    public GFrame() {
        //Create a new GPanel instance for rendering, passing screen dimensions
        demoPanel = new GPanel(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        //Set the size and position of the panel to fit the frame
        demoPanel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        //Add the panel to the frame
        add(demoPanel);

        //Set up frame properties
        setTitle("Turquoise Graphics Engine Demo");  //Title of the window
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);        //Set the size of the window
        setVisible(true);                            //Make the window visible
        setDefaultCloseOperation(EXIT_ON_CLOSE);     //Ensure the program exits when the window is closed
    }

    /**
     * Method to refresh the frame's content by repainting it.
     * This method is called in each iteration of the game loop.
     */
    public void update() {
        //Repaint the frame, which internally calls the paintComponent() method of the panel
        repaint();
    }
}
