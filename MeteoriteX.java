
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

// class main ใช้รันโดยที่ไม่ได้ input ค่า
public class MeteoriteX {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame(2);
    }
}

// class frame ใช้แสดงผล frame ตามค่าที่ตั้งไว้
class MyFrame extends JFrame {
    // constructor ที่ต้องมีการ input number ก่อน ถึงจะเรียกใช้งานได้
    public MyFrame(int number) {
        // เรียกใช้ MyPaint โดยใส่ค่า number ด้วย
        MyPaint paint = new MyPaint(number);
        paint.number = number;          // มีปัญหาที่ใส่ค่า number ลงใน parameter แล้วค่าไม่ไป เลยเรียกใช้โดยตรง

        setTitle("Meteorite X");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(paint);

        setVisible(true);
    }
}

// class paint ใช้เพื่อเรียกคำสัััั่งวาดรูป
class MyPaint extends JPanel {
    // ประกาศตัวแปรเอาไว้ก่อน
    int number;         // ตัวแปรที่จะเอาไว้กำหนดจำนวนอุกกาบาต
    int size = 50;          // ขนาดของอุกกาบาต
    Image[] meteors;            // รูปของอุกกาบาต
    Image[] bomb;           // รูปของระเบิด
    Random rand = new Random();         // object random จะได้ไม่ต้อง new ทุกครั้ง
    int[] x;            // ตำแหน่ง x ของอุกกาบาต
    int[] y;            // ตำแหน่ง y ของอุกกาบาต
    int[] dx;           // การเคลื่อนที่ของอุกกาบาตในแนว x มี 1 ถึง 3 และ -1 ถึง -3
    int[] dy;           // การเคลื่อนที่ของอุกกาบาตในแนว y มี 1 ถึง 3 และ -1 ถึง -3
    int[] direction;            // ทิศทางการเคลื่อนที่ของอุกกาบาต มี 0 แนวนอน, 1 แนวตั้ง, 2 แนวทะแยง
    boolean[] meteoAlive;           // ใช้ตรวจสอบว่าอุกกาบาตยังอยู่
    boolean[] bombAlive;            // ใช้ตรวจสอบว่าระเบิดถูกใช้งาน
    MyThread[] threads;         // สร้าง thread จาก class MyThread เพื่อทำให้อุกกาบาตดูเหมื่อนเคลื่อนที่
    Timer[] timers;         // สร้าง timer เพื่อกำหนดระยะเวลาที่ระเบิดจะหายไป
    
    // constructor ที่ต้องมีการ input number ก่อน ถึงจะเรียกใช้งานได้
    public MyPaint(int number) {
        // กำหนดตัวแปรอาเรย์หลังจากมี number
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

        // สร้าง panel เพื่อให้สามารถวาดรูปบน panel
        setSize(getWidth(), getHeight());
        setLocation(0, 0);

        // ใส่ข้อมูลลงใน image และตัวแปรต่างๆ เพื่อเตรียมวาดรูป
        for (int i = 0; i < number; i++) {
            // สร้างอุกกาบาต แล้วก็สุ่มรูปอุกกาบาต
            meteors[i] = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir") + File.separator + "materials\\" + (rand.nextInt(10) + 1) + ".png"
            );

            // สร้างระเบิด เพื่อรอใช้งาน
            bomb[i] = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir") + File.separator + "materials\\bomb.gif"
            );

            // กำหนดค่าต่างๆ เพื่อเตรียมใช้งาน
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

        // ถ้าคลิกที่อุกกาบาต 2 ครั้ง จะทำให้อุกกาบาตหายไป
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    for (int i = 0; i < number; i++) {
                        // ตรวจว่าอุกกาบาตนั้นมีอยู่หรือไม่ และเรียกใช้ method ที่สามารถตรวจสอบขนาดสี่เหลี่ยมของอุกกาบาต
                        if (meteors[i] != null && getRectMeteor(x[i], y[i]).contains(e.getPoint())) {
                            dx[i] = 0;
                            dy[i] = 0;
                            meteoAlive[i] = false;
                            bombAlive[i] = true;

                            explosionMeteos();          // method บรรทัดที่ 127 : เพื่อวาดรูประเบิด
                        }
                    }
                }
            }
        });
    }

    // method ทีีใช้ timer ที่จะทำให้รูป gif ทำงานและหายไป
    public void explosionMeteos() {
        for (int i = 0; i < number; i++) {
            if (bombAlive[i]) {
                timers[i] = new Timer();
                // เรียกใช้ TimerTask แบบ inner class
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

    // method paint ใช้วาดรูป
    @Override
    protected void paintComponent(Graphics g) {
        // วาดรูปพื้นหลังสีดำ
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        // วาดรูปอุกกาบาต และระเบิด
        for (int i = 0; i < number; i++) {
            // วาดรูปอุกกาบาต เมื่อระเบิดยังเป็น false
            if (meteoAlive[i] && !bombAlive[i]) {
                g.drawImage(meteors[i], x[i], y[i], x[i] + size, y[i] + size, 0, 0, 300, 300, this);
            }

            // วาดรูประเบิด เมื่อระเบิดเป็น true
            if (!meteoAlive[i] && bombAlive[i]) {
                g.drawImage(bomb[i], x[i], y[i], x[i] + size, y[i] + size, 0, 0, 80, 80, this);
            }
        }

        movingMeteor();         // method บรรทัดที่ 171 : เพื่อให้อุกกาบาตเคลื่อนที่ตามแนว
    }

    // method ที่จะควบคุมลักษณะการเคลื่อนที่ของอุกกาบาต
    public void movingMeteor() {
        for (int i = 0; i < number; i++) {
            // เคลื่อนที่แนวนอน
            if (direction[i] == 0) {
                x[i] += dx[i];
            }
            // เคลื่อนที่แนวตั้ง
            else if (direction[i] == 1) {
                y[i] += dy[i];
            }
            // เคลื่อนที่แนวเฉียง
            else if (direction[i] == 2) {
                x[i] += dx[i];
                y[i] += dy[i];
            }

            checkWallHit();         // method บรรทัดที่ 193 : เพื่อตรวจสอบเมื่ออุกกาบาตชนเข้ากับขอบของ frame
            checkMeteosHit();           // method บรรทัด 212 : เพื่อตรวจสอบเมื่ออุกกาบาต 2 ลูกมาชนกัน
        }
    }

    // method ตรวจสอบการชนกับขอบของ frame
    public void checkWallHit() {
        for (int i = 0; i < number; i++) {
            // เมื่อขอบชนแถวๆ ผั่งซ้าย
            if (x[i] <= 0 || y[i] <= 0) {
                dx[i] = rand.nextInt(3) + 1;
                dy[i] = dx[i];
                direction[i] = rand.nextInt(3);
            }

            // เมื่อขอบชนแถวๆ ผั่งขวา
            if (x[i] >= getWidth() - size || y[i] >= getHeight() - size) {
                dx[i] = rand.nextInt(3) - 3;
                dy[i] = dx[i];
                direction[i] = rand.nextInt(3);
            }
        }
    }

    // method ตรวจสอบเมื่ออุกกาบาต 2 ลูกมาชนกัน
    public void checkMeteosHit() {
        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                // เรียกใช้ method getRectMeteor ทั้งสองลูก เทื่อตรวจสอบว่ามีการซ้อนทับกันของตำแหน่งหรือไม่
                if (getRectMeteor(x[i], y[i]).intersects(getRectMeteor(x[j], y[j]))) {
                    // จะทำงานก็ต่อเมื่ออุกกาบาตทั้ง 2 ลูกยังไม่ระเบิด
                    if (meteoAlive[i] && meteoAlive[j]) {
                        // ถ้าการเคลื่อนที่ของอุกกาบาตทั้งสองเป็นลบ ทำการสุ่มการเคลื่อนที่
                        if (dx[i] < 0 || dy[i] < 0 || dx[j] > 0 || dy[j] > 0) {
                            dx[i] = rand.nextInt(3) - 3;
                            dy[i] = dx[i];
                            dx[j] = rand.nextInt(3) + 1;
                            dy[j] = dx[j];
                        }

                        // ถ้าการเคลื่อนที่ของอุกกาบาตทั้งสองเป็นบวก ทำการสุ่มการเคลื่อนที่
                        if (dx[i] > 0 || dy[i] > 0 || dx[j] < 0 || dy[j] < 0) {
                            dx[i] = rand.nextInt(3) - 3;
                            dy[i] = dx[i];
                            dx[j] = rand.nextInt(3) + 1;
                            dy[j] = dx[j];
                        }
                        
                        // สุ่มลักษณะการเคลื่อนที่ใหม่
                        direction[i] = rand.nextInt(3);
                        direction[j] = rand.nextInt(3);
                    }
                }
            }
        }
    }

    // method ที่กำหนดลักษณะสี่เหลี่ยมของอุกกาบาต
    public Rectangle getRectMeteor(int x, int y) {
        return new Rectangle(x, y, size, size);
    }
}

// class thread เอาไว้ทำให้อุกกาบาตดูเหมือนเคลื่อนที่
class MyThread extends Thread {
    // เรียกใช้ class MyPaint เพื่อที่จะวาดลง panel เรื่อยๆ
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