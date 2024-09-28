
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class InputNumber {
    public static void main(String[] args) {
        InputFrame frame = new InputFrame();
    }
}

class InputFrame extends JFrame implements ActionListener {
    JTextField textField = new JTextField();
    JButton button = new JButton("OK");

    public InputFrame() {
        setSize(200, 100);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
        button.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            int number = Integer.parseInt(textField.getText());

            MyFrame myFrame = new MyFrame(number);
            dispose();
        }
    }
}