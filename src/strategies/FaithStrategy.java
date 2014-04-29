package strategies;

import java.util.ArrayList;

import clutches.Board;
import clutches.Hand;

import main.Dom;
import main.GameState;
import main.Loc;


public class FaithStrategy implements Strategy {

	private GameState game = GameState.getInstance();

	@Override
	public boolean playTile() {
		Hand hand = game.getHand(game.getCurrentPlayer());
		ArrayList<Dom> doms = hand.getDoms();
		if (doms.isEmpty())
			return false;

		// set left and right ends
		int leftEnd = game.getBoard().getLeftEnd();
		int rightEnd = game.getBoard().getRightEnd();

		// lists of playable Dom weights and indexes
		ArrayList<Integer> weights = new ArrayList<Integer>();
		ArrayList<Integer> matchIndexes = new ArrayList<Integer>();

		for (int i = 0; i < doms.size(); i++) {
			Dom dom = doms.get(i);
			int domLeft = dom.getLeft();
			int domRight = dom.getRight();
			if ((domLeft == rightEnd) || (domRight == rightEnd)
					|| (domLeft == leftEnd) || (domRight == leftEnd)) {
				matchIndexes.add(i); // add a match index
				weights.add(domLeft + domRight); // add its weight
			}
		}

		if (matchIndexes.isEmpty())
			return false;

		int heaviest = -1; // weight of heaviest dom
		int bestMatch = -1; // index of heaviest dom

		for (int i = 0; i < weights.size(); i++) {
			if (weights.get(i) > heaviest) {
				heaviest = weights.get(i);
				bestMatch = matchIndexes.get(i);
			}
		}

		Board board = game.getBoard();
		Dom match = hand.removeDom(bestMatch); // remove heaviest match
		int domLeft = match.getLeft();
		int domRight = match.getRight();

		if (domLeft == rightEnd) {
			board.addDom(Loc.RIGHT, match);
		} else if (domRight == leftEnd) {
			board.addDom(Loc.LEFT, match);
		} else if (domLeft == leftEnd) {
			match.flip();
			board.addDom(Loc.LEFT, match);
		} else if (domRight == rightEnd) {
			match.flip();
			board.addDom(Loc.RIGHT, match);
		}
		return true;
	}

	@Override
	public String getName() {
		return "Faith";
	}
}