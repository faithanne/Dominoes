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
public class BoneYardPanel extends JPanel {

	private BufferedImage back;
	private BufferedImage background;
	private ArrayList<Drawmino> drawminoes = new ArrayList<Drawmino>();

	public BoneYardPanel() {
		setPreferredSize(new Dimension(67, 500));
		URL input = getClass().getResource("back.png");
		URL input2 = getClass(). getResource("table.png");
		try {
			back = ImageIO.read(input);
			background = ImageIO.read(input2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		back = back.getSubimage(3, 3, 278, 145);
	}

	public void addDrawmino(Drawmino d) {
		drawminoes.add(d);
	}
	
	private BufferedImage scaleImage(BufferedImage before) {
		BufferedImage after = new BufferedImage(before.getWidth(),
				before.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(0.2, 0.2);
		AffineTransformOp scaleOp = new AffineTransformOp(at,
				AffineTransformOp.TYPE_BILINEAR);
		return scaleOp.filter(before, after);
	}

	private void drawDrawminoes(Graphics g) {
		int x = 5;
		int y = 9;
		for (@SuppressWarnings("unused") Drawmino drawmino : drawminoes) {
			g.drawImage(scaleImage(back), x, y, null);
			y+= 32;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		drawDrawminoes(g);
	}
}
