import javax.swing.JFrame;

public class RunTest {
    public static void main(String[] args) {
        JFrame frm = new JFrame();
        frm.setTitle("test");
        Test g = new Test();
        frm.setContentPane(g);
        frm.setSize(300, 400);
        frm.setResizable(false);
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
}
