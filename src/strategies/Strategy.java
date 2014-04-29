package strategies;

import main.GameState;

public interface Strategy {
	
	GameState game = GameState.getInstance();
	public String getName();
	public boolean playTile();
}
