import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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
    MyGraphics myGraphics = new MyGraphics();

    public MyFrame() {
        setTitle("MeteoriteX");
        setSize(800, 800);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(myGraphics);

        setVisible(true);
    }
}

class MyGraphics extends JPanel {
    
    private Image[] meteos = new Image[10];
    private int[] meteoX = new int[meteos.length];
    private int[] meteoY = new int[meteos.length];

    public MyGraphics() {
        setSize(800, 800);
        setLocation(0, 0);
        
        for (int i = 0; i < 2; i++) {
            meteos[i] = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "materials\\" + (new Random().nextInt(10) + 1) + ".png");
            meteoX[i] = new Random().nextInt(700);
            meteoY[i] = new Random().nextInt(700);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);

        for (int i = 0; i < 2; i++) {
            g.drawImage(meteos[i], meteoX[i], meteoY[i],  meteoX[i] + 60, meteoY[i] + 60, 0, 0, 300, 300, this);
        }
    }
}