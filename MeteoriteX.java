import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.Random;
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
        setSize(900, 900);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(myGraphics);
        
        setVisible(true);
    }
}

class MyGraphics extends JPanel {
    
    Image meteo = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "materials\\1.png");
    int x = 1;
    int y = 1;

    public MyGraphics() {
        setSize(800, 800);
        setLocation(0, 0);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(this), 0, 1);
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
    int movementX = 1;
    int movementY = 1;
    int direction = new Random().nextInt(3) + 1;
    int speed = new Random().nextInt(5) + 1;

    public MyTimerTask(MyGraphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void run() {
        if (direction == 1) {
            graphics.x = graphics.x + movementX;

            if (graphics.x > 740 || graphics.x < 0) {
                direction = new Random().nextInt(3) + 1;
                speed = new Random().nextInt(5) + 1;
                movementX = movementX * -1;
            }
        }
        else if (direction == 2) {
            graphics.y = graphics.y + movementY;

            if (graphics.y > 740 || graphics.y < 0) {
                direction = new Random().nextInt(3) + 1;
                speed = new Random().nextInt(5) + 1;
                movementY = movementY * -1;
            }
        }
        else if (direction == 3) {
            graphics.x = graphics.x + movementX;
            graphics.y = graphics.y + movementY;

            if (graphics.x > 740 || graphics.x < 0 || graphics.y > 740 || graphics.y < 0) {
                direction = new Random().nextInt(3) + 1;
                speed = new Random().nextInt(5) + 1;
                movementX = movementX * -1;
                movementY = movementY * -1;
            }
        }

        graphics.repaint();

        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {}
    }
}