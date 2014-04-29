package displays;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.Drawmino;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	private BufferedImage background = null;
	private List<Drawmino> leftDrawminoes = new ArrayList<Drawmino>();
	private List<Drawmino> rightDrawminoes = new ArrayList<Drawmino>();

	public BoardPanel() {
		setPreferredSize(new Dimension(900, 500));
		URL input = getClass().getResource("table.png");
		try {
			background = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearDrawminoes() {
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

	public Drawmino getLeftDrawmino() {
		return leftDrawminoes.get(leftDrawminoes.size() - 1);
	}

	public Drawmino getRightDrawmino() {
		return rightDrawminoes.get(rightDrawminoes.size() - 1);
	}

	private void drawDrawminoes(Graphics g) {
		int x = 425;
		int y = 200;
		int yflip = 171;
		for (Drawmino drawmino : leftDrawminoes) {
			if (drawmino.getFlip()) {
				if (x > 30) {
					g.drawImage(drawmino.getImage(), x, yflip, null);
					x -= 58;
				} else {
					if (yflip < 450) {
						g.drawImage(rotate(drawmino.getImage(), Math.PI / -2),
								x, yflip + 27, null);
						yflip += 58;
						y = yflip + 27;
					} else {
						g.drawImage(rotate(drawmino.getImage(), Math.PI), x,
								yflip + 27, null);
						x += 58;
					}
				}
			} else {
				if (x > 30) {
					g.drawImage(drawmino.getImage(), x, y, null);
					x -= 58;
				} else {
					if (y < 450) {
						g.drawImage(rotate(drawmino.getImage(), Math.PI / -2),
								x + 27, y, null);
						y += 58;
						yflip = y - 27;
					} else {
						g.drawImage(rotate(drawmino.getImage(), Math.PI),
								x, y, null);
						x += 58;
					}
				}
			}
		}

		x = 482;
		y = 200;
		yflip = 171;
		for (Drawmino drawmino : rightDrawminoes) {
			if (drawmino.getFlip()) {
				if (x < 870) {
					x += 1;
					g.drawImage(drawmino.getImage(), x, yflip, null);
					x += 58;
				} else {
					if (yflip > 30) {
						g.drawImage(rotate(drawmino.getImage(), Math.PI / 2),
								x, yflip + 27, null);
						yflip -= 58;
						y = yflip + 27;
					} else {
						x -= 27;
						g.drawImage(rotate(drawmino.getImage(), Math.PI), x,
								yflip, null);
						x += 85;
					}
				}
			} else {
				if (x < 870) {
					x -= 1;
					g.drawImage(drawmino.getImage(), x, y, null);
					x += 58;
				} else {
					if (y > 30) {
						g.drawImage(rotate(drawmino.getImage(), Math.PI / 2),
								x, y, null);
						y -= 58;
						yflip = y - 27;
					} else {
						x -= 27;
						g.drawImage(rotate(drawmino.getImage(), Math.PI), x, y,
								null);
						x += 85;
					}
				}
			}
		}
	}

	private Image rotate(BufferedImage before, double angle) {
		BufferedImage after = new BufferedImage(before.getWidth(),
				before.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform tx = AffineTransform.getRotateInstance(angle, 278 / 10,
				147 / 5);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_BILINEAR);
		return op.filter(before, after);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		drawDrawminoes(g);
	}
}
