
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MeteoriteX {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
    }
}

class MyFrame extends JFrame {
    public MyFrame() {
        MyPaint paint = new MyPaint();

        setTitle("Meteorite X");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(paint);

        setVisible(true);
    }
}

class MyPaint extends JPanel {
    int number = 5;
    int size = 50;
    Image[] meteors = new Image[number];
    Image bomb;
    Random rand = new Random();
    int[] x = new int[number];
    int[] y = new int[number];
    int[] dx = new int[number];
    int[] dy = new int[number];
    int[] direction = new int[number];
    boolean[] meteoAlive = new boolean[number];
    MyThread[] threads = new MyThread[number];
    
    public MyPaint() {
        setSize(getWidth(), getHeight());
        setLocation(0, 0);

        for (int i = 0; i < number; i++) {
            meteors[i] = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir") + File.separator + "materials\\" + (rand.nextInt(10) + 1) + ".png"
            );

            bomb = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "materials\\bomb.gif");

            x[i] = rand.nextInt(400);
            y[i] = rand.nextInt(400);
            dx[i] = rand.nextInt(5) + 1;
            dy[i] = dx[i];
            direction[i] = rand.nextInt(2);
            meteoAlive[i] = true;

            threads[i] = new MyThread(this);
            threads[i].start();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    for (int i = 0; i < number; i++) {
                        removeMeteorAtPoint(e.getPoint());
                    }
                }
            }
        });
    }

    private void removeMeteorAtPoint(Point point) {
        for (int i = 0; i < number; i++) {
            if (meteors[i] != null && getRectMeteor(x[i], y[i]).contains(point)) {
                meteors[i] = null;
                meteoAlive[i] = false;
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < number; i++) {
            if (meteoAlive[i]) {
                g.drawImage(meteors[i], x[i], y[i], x[i] + size, y[i] + size, 0, 0, 300, 300, this);
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
                dx[i] = rand.nextInt(5) + 1;
                dy[i] = dx[i];
                direction[i] = rand.nextInt(3);
            }

            if (x[i] >= getWidth() - size || y[i] >= getHeight() - size) {
                dx[i] = rand.nextInt(5) - 5;
                dy[i] = dx[i];
                direction[i] = rand.nextInt(3);
            }
        }
    }

    public void checkMeteosHit() {
        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                if (getRectMeteor(x[i], y[i]).intersects(getRectMeteor(x[j], y[j]))) {
                    if ((dx[i] < 0 && dy[i] < 0) || (dx[j] < 0 && dy[j] < 0)) {
                        dx[i] = rand.nextInt(5) + 1;
                        dy[i] = dx[i];
                        dx[j] = rand.nextInt(5) - 5;
                        dy[j] = dx[j];
                    }

                    if ((dx[i] > 0 && dy[i] > 0) || (dx[j] > 0 && dy[j] > 0)) {
                        dx[i] = rand.nextInt(5) - 5;
                        dy[i] = dx[i];
                        dx[j] = rand.nextInt(5) + 1;
                        dy[j] = dx[j];
                    }
                    
                    direction[i] = rand.nextInt(3);
                    direction[j] = rand.nextInt(3);
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