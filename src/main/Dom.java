package main;

public class Dom {

	private int left;
	private int right;
	
	public Dom(int left, int right) {
		this.left = left;
		this.right = right;
	}

	public int getLeft(){
		return left;
	}
	
	public void setLeft(int left){
		this.left = left;
	}
	
	public int getRight(){
		return right;
	}
	
	public void setRight(int right){
		this.right = right;
	}
	
	public boolean isDouble(){
		return right == left;
	}
	
	public boolean isBlank(){
		if (isDouble() && left == '0'){
			return true;
		}
		else return false;
	}
	
	public int points(){
		return left + right;
	}
	
	public void flip(){
		int temp;
		temp = left;
		left = right;
		right = temp;
	}
	
	public String toString(){
		return "[" + getLeft() + ", " + getRight() + "]";
	}
}
