package strategies;

import clutches.Hand;
import main.Dom;
import main.GameState;
import main.Loc;

public class CJStrategy implements Strategy{

    private GameState game;
    private static String name = "CJ";

    public CJStrategy() {
        game = GameState.getInstance();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean playTile() {
        Hand hand = game.getHand(game.getCurrentPlayer());
        int leftBoard = game.getBoard().getLeftEnd();
        int rightBoard = game.getBoard().getRightEnd();
        
        for (int i = 0; i < hand.getDoms().size(); i++) {
            Dom dom = hand.getDoms().get(i);

            //if right dom matches left board, add to board at 0
            if (dom.getRight() == leftBoard) {
                game.getBoard().addDom(Loc.LEFT, hand.removeDom(i));
                return true;

                //if left dom matches left board, flip and add to board at 0
            } else if (dom.getLeft() == leftBoard) {
                dom.flip();
                game.getBoard().addDom(Loc.LEFT,  hand.removeDom(i));
                return true;

                //if left matches right board, add to board at board.size()
            } else if (dom.getLeft() == rightBoard) {
                game.getBoard().addDom(Loc.RIGHT,  hand.removeDom(i));
                return true;
                
                //if right matches right board, flip and add to board at board.size()
            } else if (dom.getRight() == rightBoard) {
                dom.flip();
                game.getBoard().addDom(Loc.RIGHT,  hand.removeDom(i));
                return true;
            }

        }

        return false;
    }
}
