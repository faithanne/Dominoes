package clutches;

import clutchReps.FaBoardRep;
import main.Dom;
import main.Loc;

public class Board extends Clutch {
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
		if (location == Loc.LEFT) {					// if location is LEFT
			if (leftEnd != d.getRight()) {		// and leftEnd != right
				if (leftEnd == d.getLeft()) {	// and leftEnd == left
					d.flip();					// then flip it!
				} else							// otherwise don't add
					return;
			}
			this.leftEnd = d.getLeft();			//set the new leftEnd
			rep.addDom(Loc.LEFT, d);
		} else if (location == Loc.RIGHT) {			// if location is RIGHT
			if (rightEnd != d.getLeft()) {		// and rightEnd != left
				if (rightEnd == d.getRight()) {	// and rightEnd == right
					d.flip();					// then flip it
				} else							// otherwise don't add
					return;
			}
			this.rightEnd = d.getRight();		// set the new rightEnd
			rep.addDom(Loc.RIGHT, d);
		}
	}
}