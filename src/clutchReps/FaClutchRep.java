package clutchReps;

import java.util.ArrayList;

import main.Dom;

public class FaClutchRep implements ClutchRep {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int ANY = -1;

	protected ArrayList<Dom> store;

	public FaClutchRep() {
		store = new ArrayList<Dom>();
	}

	public FaClutchRep(ArrayList<Dom> store) {
		this.store = store;
	}

	@Override
	public void addDom(int location, Dom d) {
		//eliminate duplicates
		for (Dom dom : store) {
			if ((dom.getLeft() == d.getLeft() && dom.getRight() == d.getRight())
					|| (dom.getLeft() == d.getRight() && dom.getRight() == d
							.getLeft())) {
				return;
			}
		}
		if (location == LEFT)
			store.add(0, d);
		else if (location == RIGHT)
			store.add(d);
		else
			store.add(d);
	}

	@Override
	public Dom removeDom(int location) {
		return store.remove(location);
	}

	@Override
	public ArrayList<Dom> getDoms() {
		ArrayList<Dom> list = new ArrayList<Dom>();
		for (Dom d : store)
			list.add(d);
		return list;
	}
}