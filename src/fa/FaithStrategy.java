package fa;

import java.util.ArrayList;

public class FaithStrategy implements Strategy {

	private GameState game = GameState.getInstance();
	int player;

	public FaithStrategy(int player) {
		this.player = player;

	}

	@Override
	public boolean playTile() {
		Hand hand = game.getHand(player);
		ArrayList<Dom> doms = hand.getDoms();
		int leftEnd = game.getBoard().getLeftEnd();
		int rightEnd = game.getBoard().getRightEnd();
		Board board = game.getBoard();
		int i = 0;
		for (Dom d : doms) {
			int left = d.getLeft();
			int right = d.getRight();
			if (left == leftEnd) {
				d.flip();
				board.addDom(0, d);
				hand.removeDom(i);

				/*
				boardPanel.addLeftDrawmino(new Drawmino(d));
				if (g.getCurrentPlayer() == 0) {
					p1.removeDrawmino(index);
				} else {
					p2.removeDrawmino(index);
				}
				*/
				
				//board.setLeftEnd(d.getLeft());
				return true;
			} else if (right == rightEnd) {
				d.flip();
				board.addDom(1, d);
				hand.removeDom(i);
				
				/*
				boardPanel.addRightDrawmino(new Drawmino(d));
				if (g.getCurrentPlayer() == 0) {
					p1.removeDrawmino(index);
				} else {
					p2.removeDrawmino(index);
				}
				*/
				
				//board.setRightEnd(d.getRight());
				return true;
			} else if (left == rightEnd) {
				board.addDom(1, d);
				hand.removeDom(i);
				
				/*
				boardPanel.addRightDrawmino(new Drawmino(d));
				if (g.getCurrentPlayer() == 0) {
					p1.removeDrawmino(index);
				} else {
					p2.removeDrawmino(index);
				}
				*/
		
				//board.setRightEnd(d.getRight());
				return true;
			} else if (right == leftEnd) {
				board.addDom(0, d);
				hand.removeDom(i);
				
				/*
				boardPanel.addLeftDrawmino(new Drawmino(d));
				if (g.getCurrentPlayer() == 0) {
					p1.removeDrawmino(index);
				} else {
					p2.removeDrawmino(index);
				}
				*/
				
				//board.setLeftEnd(d.getLeft());
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Faith";
	}

}
