import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MeteoriteX {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
    }
}

class MyFrame extends JFrame {

    MyGraphics myGraphics = new MyGraphics();

    public MyFrame() {
        setTitle("MeteoriteX");
        setSize(800, 800);
        setLayout(null);
        setBackground(Color.black);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(myGraphics);

        setVisible(true);
    }
}

class MyGraphics extends JPanel {
    
    Image meteo = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "materials\\1.png");
    int x = 0;
    int y = 0;

    public MyGraphics() {
        setSize(800, 800);
        setLocation(0, 0);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(this), 0, 10);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);
        
        g.drawImage(meteo, x, y, x + 60, y + 60, 0, 0, 300, 300, this);
    }
}

class MyTimerTask extends TimerTask {

    MyGraphics graphics;

    public MyTimerTask(MyGraphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void run() {
        graphics.x++;
        graphics.repaint();
    }
}