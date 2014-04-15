package fa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.Timer;

public class GameMgr {

	private static int players;
	
	private static Display display;
	private GameState g;
	private int pass = 0;

	public GameMgr(int players) {
		this.players = players;
	}

	public void resetGame() {

		g = GameState.getInstance();

		// set number of players
		g.setNumPlayers(players);

		// create all Dominoes
		int k = 0;
		Dom[] dominoes = new Dom[28];
		for (int i = 0; i < 7; i++) {
			for (int j = i; j < 7; j++) {
				dominoes[k++] = new Dom(i, j);
			}
		}
		// shuffle Dominoes
		Collections.shuffle(Arrays.asList(dominoes));

		// get 1st 14 for boneYard
		BoneYard boneYard = new BoneYard();
		boneYard.setRep(new FaBoneYardRep());
		for (int i = 0; i < 14; i++) {
			boneYard.addDom(-1, dominoes[i]);
		}
		g.setBoneYard(boneYard);

		int j = 14;
		Hand[] hands = new Hand[2];
		hands[0] = new Hand();
		hands[1] = new Hand();
		hands[0].setRep(new FaHandRep());
		hands[1].setRep(new FaHandRep());

		// get 7 for each hand
		for (int i = 0; i < 7; i++) {
			hands[0].addDom(-1, dominoes[j++]);
			hands[1].addDom(-1, dominoes[j++]);
		}
		g.setHands(hands);

		// initialize board
		Board board = new Board();
		board.setRep(new FaBoardRep());

		// results[0] = player index, results[1] = domino index
		int[] results = getStartingDom(hands);

		Dom firstDom = hands[results[0]].getRep().removeDom(results[1]);
		board.addDom(0, firstDom);

		g.setBoard(board);
		board.setLeftEnd(firstDom.getLeft());
		board.setRightEnd(firstDom.getRight());

		// set scores
		int[] scores = { 0, 0 };
		g.setScores(scores);

		// set status and first player
		g.setStatus(Status.INTRO_STATE);
		g.setCurrentPlayer(getNextPlayer(results[0]));

		// set play state
		g.setStatus(Status.PLAY_STATE);
		g.setWinner(-1);

		// set strategies
		Strategy[] strategies = new Strategy[players];
		strategies[0] = new FaithStrategy(0);
		strategies[1] = new FaithStrategy(1);
		g.setStrategies(strategies);
		
		//timer.start();
	}

	public static int[] getStartingDom(Hand[] hands) {
		ArrayList<Dom> hand0 = hands[0].getDoms();
		ArrayList<Dom> hand1 = hands[1].getDoms();

		// result[0] = index, result[1] = weight
		int[] result0 = getHighestDouble(hand0);
		int[] result1 = getHighestDouble(hand1);

		int starter = 0;
		int index = -1;

		// player 0 starts with highest double
		if (result0[1] > result1[1]) {
			starter = 0;
			index = result0[0];
		} else if (result0[1] < result1[1]) {
			// player 1 starts with highest double
			starter = 1;
			index = result1[0];
		} else {
			// equal highest double == no doubles
			result0 = getHeaviest(hand0);
			result1 = getHeaviest(hand1);

			// player 0 starts with heaviest Dom
			if (result0[1] > result1[1]) {
				starter = 0;
				index = result0[0];
			}
			// player 1 starts with heaviest Dom
			else if (result0[1] < result1[1]) {
				starter = 1;
				index = result1[0];
			}
			// equal weights mean heaviest side starts
			else {
				// if heavy side is player0
				if (result0[2] > result1[2]) {
					starter = 0;
					index = result0[0];
				}
				// if heavy side is player1;
				// heavy sides cannot be equal because weights are equal
				else {
					starter = 1;
					index = result1[0];
				}
			}
		}

		int[] results = { starter, index };
		return results;
	}

	public static int getNextPlayer(int player) {
		player++;
		return (player) % players;
	}

	public static int[] getHighestDouble(ArrayList<Dom> hand) {
		int highest = -1;
		int i = 0;
		int index = -1;
		for (Dom d : hand) {
			if (d.isDouble()) {
				int left = d.getLeft();
				if (left > highest) {
					highest = left;
					index = i;
				}
			}
			i++;
		}

		int[] results = { index, highest };
		return results;
	}

	public static int[] getHeaviest(ArrayList<Dom> hand) {
		int weight = -1;
		int heavySide = -1;
		int index = -1;
		int i = 0;
		for (Dom d : hand) {
			int points = d.points();
			if (points > weight) {
				weight = points;
				index = i;
				if (d.getRight() > d.getLeft()) {
					heavySide = d.getRight();
				} else {
					heavySide = d.getLeft();
				}

			}
		}
		int[] results = { index, weight, heavySide };
		return results;
	}

	public void callRepaint() {
		display.callRepaint();
	}

	

	public static void main(String[] args) {

		int players = 2;
		GameMgr gm = new GameMgr(players);
		gm.resetGame();

		display = new Display();
		JFrame frame = new JFrame("Dominoes!");
		frame.add(display);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 700);
		frame.setVisible(true);
	}
}