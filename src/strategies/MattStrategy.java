package strategies;
import java.util.ArrayList;
import java.util.Random;

import clutches.Board;
import clutches.Hand;

import main.Dom;
import main.GameState;
import main.Loc;
import main.Status;


public class MattStrategy implements Strategy {
	
	private Random rand = new Random();

	@Override
	public boolean playTile() {
		if(GameState.getInstance().getStatus() != Status.PLAY_STATE) {
			return false;
		}
		
		return playDom();
	}
	
	/**
	 * Plays a random domino from the hand.
	 * @return true if a dom was able to be played
	 */
	private boolean playDom() {
		GameState game = GameState.getInstance();
		Board board = game.getBoard();
		Hand hand = game.getHand(game.getCurrentPlayer());
		
		int left = board.getLeftEnd(),
				right = board.getRightEnd();
		
		ArrayList<Dom> doms = getPlayable(hand.getDoms(), left, right);
		if(doms.isEmpty()) {
			return false;
		}
		
		Dom ranDom = doms.get(rand.nextInt(doms.size()));
		
		int randSide = getSide(ranDom, left, right);
		if( (randSide == Loc.LEFT && ranDom.getRight() != left) || 
				(randSide == Loc.RIGHT && ranDom.getLeft() != right) ){
			ranDom.flip();
		}
		
		// Prevents issues when rendering the hand while it is being updated
		synchronized(hand) {
			board.addDom(randSide, hand.removeDom(getDomLocation(ranDom, hand)));
		}
			
		return true;
	}
	
	/**
	 * @return the location of the given domino in the given hand
	 */
	public int getDomLocation(Dom dom, Hand hand) {
		ArrayList<Dom> hanDoms = hand.getDoms();
		
		for(int i = 0; i < hanDoms.size(); i++) {
			if(hanDoms.get(i) == dom) return i;
		}
		
		return -1;
	}
	
	/**
	 * @return the side of the board to place the domino on
	 */
	private int getSide(Dom dom, int left, int right) {
		boolean canPlaceLeft = dom.getLeft() == left || dom.getRight() == left,
				canPlaceRight = dom.getRight() == right || dom.getLeft() == right;
		
		if(canPlaceLeft && !canPlaceRight) {
			return Loc.LEFT;
		} else if(!canPlaceLeft && canPlaceRight) {
			return Loc.RIGHT;
		} else {
			if(rand.nextBoolean()) {
				return Loc.LEFT;
			}		
			return Loc.RIGHT;
		}
	}
	
	/**
	 * @return all currently playable dominoes in a hand
	 */
	private ArrayList<Dom> getPlayable(ArrayList<Dom> hand, int left, int right) {
		ArrayList<Dom> playable = new ArrayList<Dom>();
		
		for(Dom dom : hand) {
			if(canPlayDom(dom, left, right)) {
				playable.add(dom);
			}
		}
		
		return playable;
	}
	
	/**
	 * Determines if a given domino can be played on a board
	 * @return true if the domino can be played
	 */
	private boolean canPlayDom(Dom dom, int left, int right) {
		int domLeft = dom.getLeft(), domRight = dom.getRight();
		return domLeft == left || domLeft == right || domRight == left || domRight == right;
	}
	
	@Override
	public String getName() {
		return "Matt";
	}
}
