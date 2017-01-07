import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args){
        JFrame jf = new JFrame();
        jf.setSize(800,800);
        jf.setTitle("Mandel");
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(new Window(), BorderLayout.CENTER);
        jf.setVisible(true);
        jf.repaint();
    }
}
