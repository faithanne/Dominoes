package strategies;

import java.util.ArrayList;

import clutches.Board;
import clutches.Hand;

import main.Dom;
import main.GameState;

/**
 * 
 * @author VestigialHeart13
 */
public class PaulStrategy implements Strategy {

	@Override
	public boolean playTile() {
		return playHeaviestTile();
	}

	public ArrayList<Dom> sortHand(Hand unsorted) {
		ArrayList<Dom> sortedDoms = unsorted.getDoms();

		Boolean sorted = false;

		while (!sorted) {
			Dom tempDom;
			int count = 0;
			for (int i = 1; i < sortedDoms.size(); i++) {
				if (sortedDoms.get(i).getLeft() + sortedDoms.get(i).getRight() < sortedDoms
						.get(i - 1).getLeft()
						+ sortedDoms.get(i - 1).getRight()) {
					tempDom = sortedDoms.get(i);
					sortedDoms.set(i, sortedDoms.get(i - 1));
					sortedDoms.set(i - 1, tempDom);
					count++;
				}
			}
			if (count == 0) {
				sorted = true;
			}
		}

		return sortedDoms;
	}

	public boolean playHeaviestTile() {
		GameState game = GameState.getInstance();
		Board board = game.getBoard();
		Hand hand = game.getHand(game.getCurrentPlayer());

		ArrayList<Dom> store = sortHand(hand);

		boolean played = false;
		for (int i = store.size() - 1; !played && i >= 0; i--) {
			if (store.get(i).getLeft() == board.getLeftEnd()) {
				Dom temp = hand.removeDom(i);
				temp.flip();
				board.addDom(0, temp);
				played = true;
			} else if (store.get(i).getLeft() == board.getRightEnd()) {
				board.addDom(1, hand.removeDom(i));
				played = true;
			} else if (store.get(i).getRight() == board.getLeftEnd()) {
				board.addDom(0, hand.removeDom(i));
				played = true;
			} else if (store.get(i).getRight() == board.getRightEnd()) {
				Dom temp = hand.removeDom(i);
				temp.flip();
				board.addDom(1, temp);
				played = true;
			}
		}
		return played;
	}

	@Override
	public String getName() {
		return "Paul";
	}
}
