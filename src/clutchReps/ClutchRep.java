package clutchReps;

import java.util.ArrayList;

import main.Dom;

public interface ClutchRep {

	public void addDom(int location, Dom d);
	public Dom removeDom(int location);
	public ArrayList<Dom> getDoms();
}