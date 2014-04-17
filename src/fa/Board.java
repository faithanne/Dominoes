package fa;

public class Board extends Clutch {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int ANY = -1;

	private int leftEnd;
	private int rightEnd;

	public Board() {
		this.rep = new FaBoardRep();
	}

	public Board(Dom d) {
		this.leftEnd = d.getLeft();
		this.rightEnd = d.getRight();
	}

	public void setLeftEnd(int leftEnd) {
		this.leftEnd = leftEnd;
	}

	public int getLeftEnd() {
		return leftEnd;
	}

	public void setRightEnd(int rightEnd) {
		this.rightEnd = rightEnd;
	}

	public int getRightEnd() {
		return rightEnd;
	}

	public void addDom(int location, Dom d) {
		if (location == LEFT) {					// if location is LEFT
			if (leftEnd != d.getRight()) {		// and leftEnd != right
				if (leftEnd == d.getLeft()) {	// and leftEnd == left
					d.flip();					// then flip it!
				} else							// otherwise don't add
					return;
			}
			this.leftEnd = d.getLeft();			//set the new leftEnd
			rep.addDom(LEFT, d);
		} else if (location == RIGHT) {			// if location is RIGHT
			if (rightEnd != d.getLeft()) {		// and rightEnd != left
				if (rightEnd == d.getRight()) {	// and rightEnd == right
					d.flip();					// then flip it
				} else							// otherwise don't add
					return;
			}
			this.rightEnd = d.getRight();		// set the new rightEnd
			rep.addDom(RIGHT, d);
		}
	}
}