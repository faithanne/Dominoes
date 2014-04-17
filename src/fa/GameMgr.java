package fa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

public class GameMgr {

	private static int players;

	private static Display display;
	private static GameState game;

	@SuppressWarnings("static-access")
	public GameMgr(int players) {
		this.players = players;
	}

	public void resetGame() {

		game = GameState.getInstance();

		// set number of players
		game.setNumPlayers(players);

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
		game.setBoneYard(boneYard);

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
		game.setHands(hands);

		System.out.println("hand0: " + hands[0].getRep().getDoms());
		System.out.println("hand1: " + hands[1].getRep().getDoms());
		// initialize board
		Board board = new Board();
		board.setRep(new FaBoardRep());

		// results[0] = player index
		// results[1] = domino index
		int[] results = getStartingDom(hands);

		Dom firstDom = hands[results[0]].getRep().removeDom(results[1]);
		board.setLeftEnd(firstDom.getLeft());
		board.setRightEnd(firstDom.getRight());
		board.addDom(0, firstDom);

		game.setBoard(board);
		board.setLeftEnd(firstDom.getLeft());
		board.setRightEnd(firstDom.getRight());

		// set scores
		int[] scores = { 0, 0 };
		game.setScores(scores);

		// set status and first player
		game.setStatus(Status.INTRO_STATE);
		setNextPlayer(results[0]);

		// set play state
		game.setStatus(Status.PLAY_STATE);
		game.setWinner(-1);

		// set strategies
		Strategy[] strategies = new Strategy[players];
		strategies[0] = new FaithStrategy(0);
		strategies[1] = new FaithStrategy(1);
		game.setStrategies(strategies);
	}

	public static int[] getStartingDom(Hand[] hands) {
		ArrayList<Dom> hand0 = hands[0].getDoms();
		ArrayList<Dom> hand1 = hands[1].getDoms();

		// result[0] = index
		// result[1] = weight
		int[] result0 = getHighestDouble(hand0);
		int[] result1 = getHighestDouble(hand1);

		int starter = 0;
		int index = -1;

		if (result0[1] > result1[1]) {
			// player 0 starts with highest double
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

	public static int setNextPlayer(int player) {
		player++;
		int p = (player) % players;
		game.setCurrentPlayer(p);
		return p;
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

	public static void calculateWinner() {
		// get both hands, put them in arrays
		ArrayList<ArrayList<Dom>> doms = new ArrayList<ArrayList<Dom>>();
		doms.add(game.getHand(0).getDoms());
		doms.add(game.getHand(1).getDoms());

		int[] totals = new int[2];

		// get the total pips for each
		for (int i = 0; i < 2; i++) {
			System.out.println("calculate, hand" + i + ": " + doms.get(i));
			for (Dom d : doms.get(i)) {
				totals[i] += d.getLeft() + d.getRight();
			}
		}
		
		// if 1 < 2, 1 wins
		if (totals[0] < totals[1]) {
			game.setWinner(0);
		// if 1 > 2, 2 wins
		} else if (totals[1] < totals[0]) {
			game.setWinner(1);
		// otherwise there is a tie
		} else
			game.setWinner(-2);
		game.setStatus(Status.DONE_STATE);
	}

	public static void main(String[] args) {
		int players = 2;
		GameMgr gm = new GameMgr(players);
		gm.resetGame();

		display = new Display();
		JFrame frame = new JFrame("Dominoes!");
		frame.add(display);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(967, 700);
		frame.setVisible(true);
	}
}