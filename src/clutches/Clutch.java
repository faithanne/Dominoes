package clutches;

import java.util.ArrayList;

import clutchReps.ClutchRep;
import clutchReps.FaClutchRep;

import main.Dom;

public abstract class Clutch {

	protected ClutchRep rep = new FaClutchRep();

	public Clutch() {
	}

	public void addDom(int location, Dom d) {
		rep.addDom(location, d);
	}

	public Dom removeDom(int location) {
		return rep.removeDom(location);
	}

	public ArrayList<Dom> getDoms() {
		return rep.getDoms();
	}

	public void setRep(ClutchRep rep) {
		this.rep = rep;
	}

	public ClutchRep getRep() {
		return rep;
	}
}
