package displays;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.Drawmino;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {

	private ArrayList<Drawmino> drawminoes = new ArrayList<Drawmino>();
	private BufferedImage background = null;

	public PlayerPanel() {
		setPreferredSize(new Dimension(700, 100));
		URL input = getClass().getResource("table.png");
		try {
			background = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDrawmino(Drawmino d) {
		// eliminate duplicates
		for (Drawmino dom : drawminoes) {
			if ((dom.getLeft() == d.getLeft() && dom.getRight() == d.getRight())
					|| (dom.getLeft() == d.getRight() && dom.getRight() == d
							.getLeft())) {
				return;
			}
		}
		drawminoes.add(d);
	}

	public void clearDrawminoes(){
		drawminoes.clear();
	}
	
	public void removeDrawmino(int index) {
		drawminoes.remove(index);
	}

	private BufferedImage rotateImage(BufferedImage before) {
		BufferedImage after = new BufferedImage(before.getWidth(),
				before.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform tx = AffineTransform.getRotateInstance(Math.PI / 2,
				278 / 10, 147 / 5);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_BILINEAR);
		return op.filter(before, after);
	}

	private void drawDrawminoes(Graphics g) {
		int x = 100;
		int y = 20;
		for (Drawmino drawmino : drawminoes) {
			g.drawImage(rotateImage(drawmino.getImage()), x, y, null);
			x += 35;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		drawDrawminoes(g);
	}
}
