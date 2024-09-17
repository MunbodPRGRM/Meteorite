import javax.swing.JFrame;
import javax.swing.JPanel;

public class MeteoriteX {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
    }
}

class MyFrame extends JFrame {
    public MyFrame() {
        setTitle("MeteoriteX");
        setSize(800, 800);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setComponent();

        setVisible(true);
    }

    JPanel panel = new JPanel();

    private void setComponent() {
        add(panel);
    }
}