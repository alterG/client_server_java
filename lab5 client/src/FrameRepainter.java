import javax.swing.*;

/**
 * Created by alterG on 31.01.2017.
 */
public class FrameRepainter implements Runnable {
    private JFrame frame;

    public FrameRepainter(JFrame frame) {
    this.frame=frame;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                frame.repaint();
            } catch (InterruptedException e) {}
        }
    }
}
