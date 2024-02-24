import multiplayer.game.Frame;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //Create new frame and update it
        Frame frame = new Frame();
        while (true) {
            frame.update();
            Thread.sleep(5);
        }
    }
}
