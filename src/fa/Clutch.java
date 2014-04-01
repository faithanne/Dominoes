package fa;

import java.util.ArrayList;

public abstract class Clutch {

	protected ClutchRep rep = new FaClutchRep();
		
	public Clutch() {
	}

	public void addDom(int location, Dom d){
		rep.addDom(location, d);
	}
	
	public Dom removeDom(int location){
		return rep.removeDom(location);
	}
	
	public ArrayList<Dom> getDoms(){
		ArrayList<Dom> doms = new ArrayList<Dom>();
		for (Dom d: rep.getDoms()) doms.add(d);
		return doms;
	}
	
	public void setRep(ClutchRep rep){
		this.rep = rep;
	}
	
	public ClutchRep getRep(){
		return rep;
	}
}
