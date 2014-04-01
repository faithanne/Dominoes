package fa;

import java.util.ArrayList;

public interface ClutchRep {

	public void addDom(int location, Dom d);
	public Dom removeDom(int location);
	public ArrayList<Dom> getDoms();
}