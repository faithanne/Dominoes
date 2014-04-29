package strategies;

import main.Dom;
import main.GameState;
import clutches.Board;
import clutches.Hand;

public class EthanStrategy implements Strategy {
	private GameState game;
	
	public EthanStrategy() {
		game = GameState.getInstance();
	}

	/* Adds a domino to the game board.
	 * The domino with the most pips (that matches) is "The Chosen One".
	 */
	@Override
	public boolean playTile() {
		Board board = game.getBoard();
		Hand hand = game.getHand(game.getCurrentPlayer());
		
		int highestPoints = -1;
		int highestDom = 0;
		int playSide = 0;
		boolean matchFound = false;
		
		int leftPips = board.getLeftEnd();
		int rightPips = board.getRightEnd();
			
		for (int i = 0; i < hand.getDoms().size(); i ++) {	
			boolean isMatch = false;
			int matchingSide = 0;
				
			Dom dom = hand.getDoms().get(i);
				
			if (dom.getRight() == leftPips) {
				isMatch = true;
				matchingSide = 0;
			} else if (dom.getLeft() == rightPips) {
				isMatch = true;
				matchingSide = 1;
			} else if (dom.getRight() == rightPips) {
				isMatch = true;
				matchingSide = 1;
				dom.flip();
			} else if (dom.getLeft() == leftPips) {
				isMatch = true;
				matchingSide = 0;
				dom.flip();
			}
				
			if ((dom.points() > highestPoints) && (isMatch)) {
				highestPoints = dom.points();
				highestDom = i;
				playSide = matchingSide;
				matchFound = true;
			}
		}
	
		if (matchFound) board.addDom(playSide, hand.removeDom(highestDom));
		return matchFound;
	}
	
	@Override
	public String getName() {
		return "EthanStrategy";
	}
}