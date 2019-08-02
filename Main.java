package futball;
import javax.swing.JFrame;

public class Main{
public static void main(String[] args) {

JFrame obj = new JFrame();
Gameplay gamePlay = new Gameplay(1400, 800);
obj.setBounds(10, 10, 1400, 800);
obj.setTitle("M\u00E1rton Petrekanics's Futball Game");
obj.setResizable(false);
obj.setVisible(true);
obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
obj.add(gamePlay);
}
}