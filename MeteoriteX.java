
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MeteoriteX {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame(2);
    }
}

class MyFrame extends JFrame {
    public MyFrame(int number) {
        MyPaint paint = new MyPaint(number);
        paint.number = number;

        setTitle("Meteorite X");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(paint);

        setVisible(true);
    }
}

class MyPaint extends JPanel {
    int number;
    int size = 50;
    Image[] meteors;
    Image[] bomb;
    Random rand = new Random();
    int[] x;
    int[] y;
    int[] dx;
    int[] dy;
    int[] direction;
    boolean[] meteoAlive;
    boolean[] bombAlive;
    MyThread[] threads;
    Timer[] timers;
    
    public MyPaint(int number) {
        meteors = new Image[number];
        bomb = new Image[number];
        x = new int[number];
        y = new int[number];
        dx = new int[number];
        dy = new int[number];
        direction = new int[number];
        meteoAlive = new boolean[number];
        bombAlive = new boolean[number];
        threads = new MyThread[number];
        timers = new Timer[number];

        setSize(getWidth(), getHeight());
        setLocation(0, 0);

        for (int i = 0; i < number; i++) {
            meteors[i] = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir") + File.separator + "materials\\" + (rand.nextInt(10) + 1) + ".png"
            );

            bomb[i] = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir") + File.separator + "materials\\bomb.gif"
            );

            x[i] = rand.nextInt(700);
            y[i] = rand.nextInt(700);
            dx[i] = rand.nextInt(3) + 1;
            dy[i] = dx[i];
            direction[i] = rand.nextInt(3);
            meteoAlive[i] = true;
            bombAlive[i] = false;

            threads[i] = new MyThread(this);
            threads[i].start();
        }

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    for (int i = 0; i < number; i++) {
                        if (meteors[i] != null && getRectMeteor(x[i], y[i]).contains(e.getPoint())) {
                            dx[i] = 0;
                            dy[i] = 0;
                            meteoAlive[i] = false;
                            bombAlive[i] = true;

                            explosionMeteos();
                        }
                    }
                }
            }
        });
    }

    public void explosionMeteos() {
        for (int i = 0; i < number; i++) {
            if (bombAlive[i]) {
                timers[i] = new Timer();
                timers[i].schedule(new TimerTask() {

                    @Override
                    public void run() {
                        for (int i = 0; i < number; i++) {
                            bombAlive[i] = false;
                            repaint();
                        }
                    }
                    
                }, 500);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < number; i++) {
            if (meteoAlive[i] && !bombAlive[i]) {
                g.drawImage(meteors[i], x[i], y[i], x[i] + size, y[i] + size, 0, 0, 300, 300, this);
            }

            if (!meteoAlive[i] && bombAlive[i]) {
                g.drawImage(bomb[i], x[i], y[i], x[i] + size, y[i] + size, 0, 0, 80, 80, this);
            }
        }

        movingMeteor();
    }

    public void movingMeteor() {
        for (int i = 0; i < number; i++) {
            if (direction[i] == 0) {
                x[i] += dx[i];
            }
            else if (direction[i] == 1) {
                y[i] += dy[i];
            }
            else if (direction[i] == 2) {
                x[i] += dx[i];
                y[i] += dy[i];
            }

            checkWallHit();
            checkMeteosHit();
        }
    }

    public void checkWallHit() {
        for (int i = 0; i < number; i++) {
            if (x[i] <= 0 || y[i] <= 0) {
                dx[i] = rand.nextInt(3) + 1;
                dy[i] = dx[i];
                direction[i] = rand.nextInt(3);
            }

            if (x[i] >= getWidth() - size || y[i] >= getHeight() - size) {
                dx[i] = rand.nextInt(3) - 3;
                dy[i] = dx[i];
                direction[i] = rand.nextInt(3);
            }
        }
    }

    public void checkMeteosHit() {
        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                if (getRectMeteor(x[i], y[i]).intersects(getRectMeteor(x[j], y[j]))) {
                    if (meteoAlive[i] && meteoAlive[j]) {
                        if ((dx[i] < 0 && dy[i] < 0) || (dx[j] < 0 && dy[j] < 0)) {
                            dx[i] = rand.nextInt(3) - 3;
                            dy[i] = dx[i];
                            dx[j] = rand.nextInt(3) + 1;
                            dy[j] = dx[j];
                        }

                        if ((dx[i] > 0 && dy[i] > 0) || (dx[j] > 0 && dy[j] > 0)) {
                            dx[i] = rand.nextInt(3) - 3;
                            dy[i] = dx[i];
                            dx[j] = rand.nextInt(3) + 1;
                            dy[j] = dx[j];
                        }
                        
                        direction[i] = rand.nextInt(3);
                        direction[j] = rand.nextInt(3);
                    }
                }
            }
        }
    }

    public Rectangle getRectMeteor(int x, int y) {
        return new Rectangle(x, y, size, size);
    }
}

class MyThread extends Thread {
    MyPaint paint;

    public MyThread(MyPaint paint) {
        this.paint = paint;
    }

    @Override
    public void run() {
        while (true) {
            paint.repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}