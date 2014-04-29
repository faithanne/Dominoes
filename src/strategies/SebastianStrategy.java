package strategies;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;

import main.Dom;
import main.GameState;
import main.Loc;

import clutches.Board;
import clutches.Hand;

/**
 *
 * @author Sebastian
 *
 *
 * Strat: 1. Check how many possible tiles are left in play. 2. Find all
 * possible moves from current hand and board. 3. Simulate domino play and see
 * how many possible moves result from that play. 4. Pick my domino based on
 * lowest possible followups, where I have at least 1 more play.
 */
public class SebastianStrategy implements Strategy {

    private GameState game = GameState.getInstance();
    private int[] tilesLeft;
    private int[] tilesAcquired;

    public SebastianStrategy() {
    }

    @Override
    public boolean playTile() {
        boolean played = true;
        tilesLeft = new int[7];//tiles left unseen
        tilesAcquired = new int[7];//tiles seen
        //init tiles arrays
        for (int i = 0; i < 7; i++) {
            tilesLeft[i] = 7;
            tilesAcquired[i] = 0;
        }
        Board board = game.getBoard();
        Hand hand = game.getHand(game.getCurrentPlayer());
        updateTiles(board, hand);//update tile arrays based on current board and hand
        //lists of possible moves
        ArrayList<Dom> possibleMovesLeft = getPossibleMoves(board, hand, Loc.LEFT);
        ArrayList<Dom> possibleMovesRight = getPossibleMoves(board, hand, Loc.RIGHT);
        //# of moves possible after each possible move
        ArrayList<Integer> simulatedFollowUpsLeft = new ArrayList();
        ArrayList<Integer> simulatedFollowUpsRight = new ArrayList();

        //fill simulated moves arrays
        for (int i = 0; i < possibleMovesLeft.size(); i++) {
            simulatedFollowUpsLeft.add(simulateMoveLeft(possibleMovesLeft.get(i), board));
        }
        for (int i = 0; i < possibleMovesRight.size(); i++) {
            simulatedFollowUpsRight.add(simulateMoveRight(possibleMovesRight.get(i), board));
        }

        int followUpsLeft = 15;//max followups
        int indexLeft = -1;
        for (int i = 0; i < simulatedFollowUpsLeft.size(); i++) {
            int j = simulatedFollowUpsLeft.get(i);
            //get index of the move with the least followups where i have at least 1 move left
            if (j < followUpsLeft && tilesAcquired[possibleMovesLeft.get(i).getLeft()] > 0) {
                indexLeft = i;
                followUpsLeft = j;
            }
        }
        int followUpsRight = 15;//max followups
        int indexRight = -1;
        for (int i = 0; i < simulatedFollowUpsRight.size(); i++) {
            int j = simulatedFollowUpsRight.get(i);
            //get index of the move with the least followups where i have at least 1 move left
            if (j < followUpsRight && tilesAcquired[possibleMovesRight.get(i).getRight()] > 0) {
                indexRight = i;
                followUpsRight = j;
            }
        }

        //find my move, remove from hand, add to the board, and update board ends
        if (!possibleMovesLeft.isEmpty() || !possibleMovesRight.isEmpty()) {
            if (followUpsLeft < followUpsRight) {
                Dom d = possibleMovesLeft.get(indexLeft);
                Dom dom = getDomFromHand(hand, d);
                board.addDom(Loc.LEFT, getDomFromHand(hand, dom));
                board.setLeftEnd(dom.getLeft());
            } else if (followUpsLeft > followUpsRight) {
                Dom d = possibleMovesRight.get(indexRight);
                Dom dom = getDomFromHand(hand, d);
                board.addDom(Loc.RIGHT, getDomFromHand(hand, dom));
                board.setRightEnd(dom.getRight());
           } else if (followUpsLeft == followUpsRight) {
                Dom d = possibleMovesLeft.get(indexLeft);
                Dom dom = getDomFromHand(hand, d);
                board.addDom(Loc.LEFT, getDomFromHand(hand, dom));
                board.setLeftEnd(dom.getLeft());
            }
        }

        //if no moves possible, skip turn
        if (possibleMovesLeft.isEmpty() && possibleMovesRight.isEmpty()) {
            played = false;
        }
        return played;
    }

    //tiles in play and tiles left 
    private void updateTiles(Board b, Hand h) {
        ArrayList<Dom> boardDoms = b.getDoms();
        ArrayList<Dom> handDoms = h.getDoms();

        //update doms visible on board
        for (int i = 0; i < boardDoms.size(); i++) {
            Dom current = boardDoms.get(i);
            tilesLeft[current.getLeft()] = tilesLeft[current.getLeft()] - 1;
            tilesLeft[current.getRight()] = tilesLeft[current.getRight()] - 1;
        }
        
        //update doms from your hand
        for (int i = 0; i < handDoms.size(); i++) {
            Dom current = handDoms.get(i);
            tilesLeft[current.getLeft()] = tilesLeft[current.getLeft()] - 1;
            tilesLeft[current.getRight()] = tilesLeft[current.getRight()] - 1;
            tilesAcquired[current.getLeft()] = tilesAcquired[current.getLeft()] + 1;
            tilesAcquired[current.getRight()] = tilesAcquired[current.getRight()] + 1;
        }

    }

    //possible valid moves
    private ArrayList<Dom> getPossibleMoves(Board b, Hand h, int loc) {
        ArrayList<Dom> boardDoms = b.getDoms();
        ArrayList<Dom> handDoms = h.getDoms();
        ArrayList<Dom> possibleMoves = new ArrayList();
        int leftEnd = b.getLeftEnd();
        int rightEnd = b.getRightEnd();

        //get list of all possible valid moves for the current board, hand, and side
        for (int i = 0; i < handDoms.size(); i++) {
            Dom current = handDoms.get(i);
            if (loc == Loc.LEFT) {
                if (current.getLeft() == leftEnd) {
                    possibleMoves.add(new Dom(current.getRight(), current.getLeft()));
                }
                if (current.getRight() == leftEnd) {
                    possibleMoves.add(current);
                }
            } else if (loc == Loc.RIGHT) {
                if (current.getLeft() == rightEnd) {
                    possibleMoves.add(current);
                }
                if (current.getRight() == rightEnd) {
                    possibleMoves.add(new Dom(current.getRight(), current.getLeft()));
                }
            }
        }

        return possibleMoves;

    }

    //simulate the move and return the possible followups
    private int simulateMoveLeft(Dom d, Board b) {
        int left = d.getLeft();
        int right = b.getRightEnd();

        return tilesLeft[left] - 1 + tilesLeft[right] - 1;
    }

    //simulate the move and return the possible followups
    private int simulateMoveRight(Dom d, Board b) {
        int left = b.getLeftEnd();
        int right = d.getRight();

        return tilesLeft[left] - 1 + tilesLeft[right] - 1;
    }

    //remove and return the dom from the hand
    private Dom getDomFromHand(Hand h, Dom d) {
        Dom dom = d;
        for (int i = 0; i < h.getDoms().size(); i++) {
            Dom current = h.getDoms().get(i);
            //check the orientation of the dom, flipping if necessary
            if (dom.getLeft() == current.getLeft() && dom.getRight() == current.getRight()) {
                dom = h.removeDom(i);
            } else if (d.getLeft() == current.getRight() && dom.getRight() == current.getLeft()) {
                dom = h.removeDom(i);
                dom.flip();
            }
        }

        return dom;
    }

	@Override
	public String getName() {
		return "Sebastian";
	}
}
