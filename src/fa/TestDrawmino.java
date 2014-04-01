package fa;

import javax.swing.JFrame;

public class TestDrawmino {

	public TestDrawmino() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Dominoes!");
		Display display = new Display();
		frame.add(display);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(750, 500);
		frame.setVisible(true);
	}
}
