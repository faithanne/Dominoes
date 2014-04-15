package fa;

import java.util.ArrayList;

public class FaBoardRep extends FaClutchRep {

	protected ArrayList<Dom> leftStore;
	protected ArrayList<Dom> rightStore;
	
	public FaBoardRep() {
		leftStore = new ArrayList<Dom>();
		rightStore = new ArrayList<Dom>();
	}
	
	public void addDom(int location, Dom d){
		if (location == LEFT) leftStore.add(d);
		else if (location == RIGHT) rightStore.add(d);
	}
	
	public ArrayList<Dom> getDoms(){
		ArrayList<Dom> list = new ArrayList<Dom>();
		list.addAll(leftStore);
		list.addAll(rightStore);
		return list;
	}
	
	public Dom peekLeft(){
		return leftStore.get(leftStore.size() -1);
	}
	
	public Dom peekRight(){
		return rightStore.get(rightStore.size() -1);
	}
	
	public Dom removeDom(int location){
		if (location == LEFT) return leftStore.remove(leftStore.size() - 1);
		else if (location == RIGHT) return rightStore.remove(rightStore.size() - 1);
		else return null;
	}
	
	public ArrayList<Dom> getLeftDoms(){
		return leftStore;
	}
	
	public ArrayList<Dom> getRightDoms(){
		return rightStore;
	}
}
