package clutchReps;

import java.util.ArrayList;

import main.Dom;

public class FaHandRep extends FaClutchRep {

	public FaHandRep() {
	}

	public FaHandRep(ArrayList<Dom> store) {
		super(store);
		this.store = store;
	}

	@Override
	public void addDom(int location, Dom d) {
		// eliminate duplicates
		for (Dom dom : store) {
			if ((dom.getLeft() == d.getLeft() && dom.getRight() == d.getRight())
					|| (dom.getLeft() == d.getRight() && dom.getRight() == d
							.getLeft())) {
				return;
			}
		}
		store.add(d);
	}

	@Override
	public Dom removeDom(int location) {
		return store.remove(location);
	}

	@Override
	public ArrayList<Dom> getDoms() {
		return store;
	}
}