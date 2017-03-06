package breakout;

import javax.swing.JFrame;

public class BreakoutMain {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		BreakoutInterface panel = new BreakoutInterface();
		frame.setBounds(5,5,700,600);
		frame.setTitle("Breakout");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);		
	}
}
