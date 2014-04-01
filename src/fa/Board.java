package fa;

public class Board extends Clutch {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int ANY = -1;
	
	private int leftEnd;
	private int rightEnd;
	
	public Board(){
		this.rep = new FaBoardRep();
	}
	
	public Board(Dom d) {
		this.leftEnd = d.getLeft();
		this.rightEnd = d.getRight();
	}

	public void setLeftEnd(int leftEnd){
		this.leftEnd = leftEnd;
	}
	
	public int getLeftEnd(){
		return leftEnd;
	}
	
	public void setRightEnd(int rightEnd){
		this.rightEnd = rightEnd;
	}
	
	public int getRightEnd(){
		return rightEnd;
	}
	
	public void addDom(int location, Dom d){
		if (location == LEFT){
			this.leftEnd = d.getLeft();
			rep.addDom(LEFT, d);
		}
		else if (location == RIGHT){
			this.rightEnd = d.getRight();
			rep.addDom(RIGHT, d);
		}
	}
}