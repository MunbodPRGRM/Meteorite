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
        setSize(812, 828);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(myGraphics);
        
        setVisible(true);
    }
}

class MyGraphics extends JPanel {
    Image[] meteorites = new Image[1];
    int[] x = new int[meteorites.length];
    int[] y = new int[meteorites.length];
    int[] xMovement = new int[meteorites.length];
    int[] yMovement = new int[meteorites.length];
    int[] speed = new int[meteorites.length];
    int[] direction = new int[meteorites.length];
    int[] count = new int[meteorites.length];
    Timer[] timers = new Timer[meteorites.length];

    public MyGraphics() {
        setSize(800, 800);
        setLocation(0, 0);

        for (int i = 0; i < meteorites.length; i++) {
            meteorites[i] = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + ("materials\\" + (new Random().nextInt(10) + 1) + ".png"));
            
            x[i] = new Random().nextInt(700);
            y[i] = new Random().nextInt(700);
            xMovement[i] = 1;
            yMovement[i] = 1;
            speed[i] = new Random().nextInt(10) + 1;
            direction[i] = new Random().nextInt(3) + 1;
        }

        for (int i = 0; i < meteorites.length; i++) {
            timers[i] = new Timer();
            timers[i].scheduleAtFixedRate(new MyTimerTask(this, speed[i]), 0, 1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);

        for (int i = 0; i < meteorites.length; i++) {
            if (direction[i] == 1) {
                x[i] = x[i] + xMovement[i];

                if (x[i] <= 0 || x[i] >= getWidth() - 60) {
                    xMovement[i] = xMovement[i] * -1;
                    direction[i] = new Random().nextInt(3) + 1;
                    count[i] = 1;
                }
            }
            
            if (direction[i] == 2) {
                y[i] = y[i] + yMovement[i];

                if (y[i] <= 0 || y[i] >= getHeight() - 60) {
                    yMovement[i] = yMovement[i] * -1;
                    direction[i] = new Random().nextInt(3) + 1;
                    count[i] = 1;
                }
            }
            
            if (direction[i] == 3) {
                x[i] = x[i] + xMovement[i];
                y[i] = y[i] + yMovement[i];

                if (x[i] <= 0 || x[i] >= getWidth() - 60 || y[i] <= 0 || y[i] >= getHeight() - 60) {
                    xMovement[i] = xMovement[i] * -1;
                    yMovement[i] = yMovement[i] * -1;
                    direction[i] = new Random().nextInt(3) + 1;
                    count[i] = 1;
                }
            }

            g.drawImage(meteorites[i], x[i], y[i], x[i] + 60, y[i] + 60, 0, 0, 300, 300, this);
        }
    }
}

class MyTimerTask extends TimerTask {
    MyGraphics graphics;
    int speed = 0;

    public MyTimerTask(MyGraphics graphics, int speed) {
        this.graphics = graphics;
        this.speed = speed;
    }

    @Override
    public void run() {
        for (int i = 0; i < graphics.meteorites.length; i++) {
            if (graphics.count[i] == 1) {
                speed = new Random().nextInt(10) + 1;
                graphics.count[i] = 0;
                System.out.println(speed);
            }
        }

        graphics.repaint();

        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {}
    }
}
