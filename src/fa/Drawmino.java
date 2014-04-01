package fa;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Drawmino extends JPanel {
	
	private BufferedImage image;
	private boolean flip;
	private int left;
	private int right;
	
	public Drawmino(Dom domino) {
		left = domino.getLeft();
		right = domino.getRight();
		this.image = getImage(left, right);
	}
	
	public int getLeft(){
		return left;
	}
	
	public int getRight(){
		return right;
	}

	public boolean getFlip(){
		return flip;
	}
	
	private BufferedImage getImage(int left, int right) {
		flip = false;
		int temp;
		if (left > right) {
			temp = left;
			left = right;
			right = temp;
			flip = true;
		}

		BufferedImage image = getDomImage();	
		BufferedImage subImage = null;

		if (left == 0) {
			if (right == 0) {
				subImage = image.getSubimage(3, 3, 278, 147);
			} else if (right == 1) {
				subImage = image.getSubimage(3, 903, 278, 147);
			} else if (right == 2) {
				subImage = image.getSubimage(3, 753, 278, 147);
			} else if (right == 3) {
				subImage = image.getSubimage(3, 603, 278, 147);
			} else if (right == 4) {
				subImage = image.getSubimage(3, 453, 278, 147);
			} else if (right == 5) {
				subImage = image.getSubimage(3, 303, 278, 147);
			} else if (right == 6) {
				subImage = image.getSubimage(3, 154, 278, 147);
			}
		} else if (left == 1) {
			if (right == 1) {
				subImage = image.getSubimage(287, 903, 278, 147);
			} else if (right == 2) {
				subImage = image.getSubimage(287, 753, 278, 147);
			} else if (right == 3) {
				subImage = image.getSubimage(287, 603, 278, 147);
			} else if (right == 4) {
				subImage = image.getSubimage(287, 453, 278, 147);
			} else if (right == 5) {
				subImage = image.getSubimage(287, 303, 278, 147);
			} else if (right == 6) {
				subImage = image.getSubimage(287, 154, 278, 147);
			}
		} else if (left == 2) {
			if (right == 2) {
				subImage = image.getSubimage(572, 903, 278, 147);
			} else if (right == 3) {
				subImage = image.getSubimage(572, 753, 278, 147);
			} else if (right == 4) {
				subImage = image.getSubimage(572, 603, 278, 147);
			} else if (right == 5) {
				subImage = image.getSubimage(572, 453, 278, 147);
			} else if (right == 6) {
				subImage = image.getSubimage(572, 303, 278, 147);
			}
		} else if (left == 3) {
			if (right == 3) {
				subImage = image.getSubimage(857, 903, 278, 147);
			} else if (right == 4) {
				subImage = image.getSubimage(857, 753, 278, 147);
			} else if (right == 5) {
				subImage = image.getSubimage(857, 603, 278, 147);
			} else if (right == 6) {
				subImage = image.getSubimage(857, 453, 278, 147);
			}
		} else if (left == 4) {
			if (right == 4) {
				subImage = image.getSubimage(857, 303, 278, 147);
			} else if (right == 5) {
				subImage = image.getSubimage(857, 154, 278, 147);
			} else if (right == 6) {
				subImage = image.getSubimage(857, 3, 278, 147);
			}
		} else if (left == 5) {
			if (right == 5) {
				subImage = image.getSubimage(572, 154, 278, 147);
			} else if (right == 6) {
				subImage = image.getSubimage(572, 3, 278, 147);
			}
		} else if (left == 6 && right == 6) {
			subImage = image.getSubimage(287, 3, 278, 147);
		}
		if (flip == false) return scaleImage(subImage);
		else return rotateImage(scaleImage(subImage));
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

	@SuppressWarnings("unused")
	private BufferedImage rotateImage(BufferedImage before, double angle) {
		BufferedImage after = new BufferedImage(before.getWidth(),
				before.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.rotate(angle);
		AffineTransformOp rotateOp = new AffineTransformOp(at,
				AffineTransformOp.TYPE_BILINEAR);
		return rotateOp.filter(before, after);
	}
	
	private BufferedImage rotateImage(BufferedImage before) {
		BufferedImage after = new BufferedImage(before.getWidth(),
				before.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform tx = AffineTransform.getRotateInstance(Math.PI, 278/10, 147/5);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(before, after);
	}

	public BufferedImage getImage() {
		return image;
	}

	private BufferedImage getDomImage() {
		BufferedImage image = null;
		URL input = getClass().getResource("dominoes.png");
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
