package fa;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	private BufferedImage background = null;
	private List<Drawmino> leftDrawminoes = new ArrayList<Drawmino>();
	private List<Drawmino> rightDrawminoes = new ArrayList<Drawmino>();
	
	public BoardPanel() {
		setPreferredSize(new Dimension(700, 500));
		URL input = getClass().getResource("table.png");
		try {
			background = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clearDrawminoes(){
		leftDrawminoes.clear();
		rightDrawminoes.clear();
	}

	public List<Drawmino> getLeftDrawminoes() {
		return leftDrawminoes;
	}

	public List<Drawmino> getRightDrawminoes() {
		return rightDrawminoes;
	}

	public void addLeftDrawmino(Drawmino d) {
		leftDrawminoes.add(d);
	}

	public void addRightDrawmino(Drawmino d) {
		rightDrawminoes.add(d);
	}

	public Drawmino getLeftDrawmino(){
		return leftDrawminoes.get(leftDrawminoes.size() - 1);
	}
	
	public Drawmino getRightDrawmino(){
		return rightDrawminoes.get(rightDrawminoes.size() -1);
	}
	
	private void drawDrawminoes(Graphics g) {
		int x = 256;
		int y = 220;
		int yflip = 191;
		for (Drawmino drawmino : leftDrawminoes) {
			if (drawmino.getFlip()) {
				g.drawImage(drawmino.getImage(), x, yflip, null);
			} else {
				g.drawImage(drawmino.getImage(), x, y, null);
			}
			x -= 58;
		}
		x = 313;
		for (Drawmino drawmino : rightDrawminoes) {
			if (drawmino.getFlip()) {
				x += 1;
				g.drawImage(drawmino.getImage(), x, yflip, null);
			} else {
				x -= 1;
				g.drawImage(drawmino.getImage(), x, y, null);
			}
			x += 58;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		drawDrawminoes(g);
	}
}
