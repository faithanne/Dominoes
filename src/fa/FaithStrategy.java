package fa;

import java.util.ArrayList;

public class FaithStrategy implements Strategy {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int ANY = -1;
	private GameState game = GameState.getInstance();
	int player;

	public FaithStrategy(int player) {
		this.player = player;
	}

	@Override
	public boolean playTile() {
		// get the hand, the doms in the hand
		Hand hand = game.getHand(player);
		ArrayList<Dom> doms = hand.getDoms();
		if (doms.isEmpty()) {
			game.setStatus(Status.DONE_STATE);
			GameMgr.calculateWinner();
			return false;
		}

		// set left and right ends
		int leftEnd = game.getBoard().getLeftEnd();
		int rightEnd = game.getBoard().getRightEnd();

		Board board = game.getBoard();

		// manually iterate through the hand
		for (int i = 0; i < doms.size(); i++) {
			Dom dom = doms.get(i);
			int domLeft = dom.getLeft();
			int domRight = dom.getRight();
			if (domLeft == rightEnd) { // if domLeft == rightEnd
				board.addDom(RIGHT, dom); // add it to the board right
				hand.removeDom(i); // remove it from the hand
				System.out.println("Hand" + game.getCurrentPlayer() + ": " + hand.getDoms());
				if (hand.getDoms().isEmpty()) setWinner();
				return true;
			} else if (domRight == leftEnd) { // if domRight == leftEnd
				board.addDom(LEFT, dom); // add it to the board left
				hand.removeDom(i); // remove it from the hand
				System.out.println("Hand" + game.getCurrentPlayer() + ": " + hand.getDoms());
				if (hand.getDoms().isEmpty()) setWinner();
				return true;
			} else if (domLeft == leftEnd) { // if domLeft == leftEnd
				dom.flip(); // then flip it
				board.addDom(LEFT, dom); // add it to the board left
				hand.removeDom(i); // remove it from the hand
				System.out.println("Hand" + game.getCurrentPlayer() + ": " + hand.getDoms());
				if (hand.getDoms().isEmpty()) setWinner();
				return true;
			} else if (domRight == rightEnd) { // if domRight == rightEnd
				dom.flip(); // then flip it
				board.addDom(RIGHT, dom); // add it to the board right
				hand.removeDom(i); // remove it from the hand
				System.out.println("Hand" + game.getCurrentPlayer() + ": " + hand.getDoms());
				if (hand.getDoms().isEmpty()) setWinner();
				return true;
			}
		}
		return false; // no match, return false
	}

	public void setWinner() {
		game.setWinner(player);
		game.setStatus(Status.DONE_STATE);
	}

	@Override
	public String getName() {
		return "Faith";
	}
}
