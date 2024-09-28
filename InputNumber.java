
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

// class main ใช้รันเพื่อแสดง frame ที่สามารถ input จำนวนอุกกาบาต
public class InputNumber {
    public static void main(String[] args) {
        InputFrame frame = new InputFrame();
    }
}

// class frame ที่ตั้งค่าขนาดต่างๆ ของ frame
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
        button.setFocusable(false);
        button.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ถ้ากดที่ปุ่ม button จะเป็นการเรียก frame ที่มีอุกกาบาตเคลื่อนที่ไปมา
        if (e.getSource() == button) {
            int number = Integer.parseInt(textField.getText());
            MyFrame myFrame = new MyFrame(number);
            
            dispose();
        }
    }
}