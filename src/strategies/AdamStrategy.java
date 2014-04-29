package strategies;

import main.Dom;
import main.GameState;
import main.Loc;


public class AdamStrategy implements Strategy {

	private GameState game;
	private int player;
	
	public AdamStrategy() {
	}

	@Override
	public String getName() {
		return "Adam";
	}

	@Override
	public boolean playTile() {
		game = GameState.getInstance();
		this.player = game.getCurrentPlayer();
		
		int left  = game.getBoard().getLeftEnd(),
			right = game.getBoard().getRightEnd();
		
		for(int i = 0; i < game.getHand(player).getDoms().size(); i++){
			Dom d = game.getHand(player).getDoms().get(i);
			if(d.getRight() == left){
				addToBoard(i, Loc.LEFT);
				return true;
			}
			else if(d.getLeft() == left){
				game.getHand(player).getDoms().get(i).flip();
				addToBoard(i, Loc.LEFT);
				return true;
			}
			else if(d.getRight() == right){
				game.getHand(player).getDoms().get(i).flip();
				addToBoard(i, Loc.RIGHT);
				return true;
			}
			else if(d.getLeft() == right){
				addToBoard(i, Loc.RIGHT);
				return true;
			}
		}
		return false;
	}
	
	private void addToBoard(int i, int l){
		game.getBoard().addDom(l, game.getHand(player).getDoms().remove(i));
	}

}
