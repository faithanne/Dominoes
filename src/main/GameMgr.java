package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import strategies.AdamStrategy;
import strategies.CJStrategy;
import strategies.CalumStrategy;
import strategies.EthanStrategy;
import strategies.FaithStrategy;
import strategies.JacobStrategy;
import strategies.MattStrategy;
import strategies.PaulStrategy;
import strategies.SebastianStrategy;
import strategies.Strategy;
import clutchReps.FaBoardRep;
import clutchReps.FaBoneYardRep;
import clutchReps.FaHandRep;
import clutches.Board;
import clutches.BoneYard;
import clutches.Hand;

public class GameMgr {

	private static GameState game;
	private static int[] scores = new int[2];
	private Strategy opponent;
	private static int gamesRemaining;

	public GameMgr() {
	}

	public int[] resetGame(int players, int[] scores) {

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
		this.scores = scores;
		game.setScores(scores);

		// set status and first player
		game.setStatus(Status.INTRO_STATE);
		game.setCurrentPlayer(results[0]);
		setNextPlayer();

		// set play state
		game.setStatus(Status.PLAY_STATE);
		game.setWinner(-1);

		// set strategies
		Strategy[] strategies = new Strategy[players];
		strategies[0] = new FaithStrategy();
		Object Strategy;
		if (gamesRemaining == 5){
			opponent = chooseStrategy();
		}
		strategies[1] = opponent;
		game.setStrategies(strategies);
		
		return scores;
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

	public static void setWinner() {
		game.setWinner(game.getCurrentPlayer());
		game.setStatus(Status.DONE_STATE);
	}

	public static int setNextPlayer() {
		int p = (game.getCurrentPlayer() + 1) % game.getNumPlayers();
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
			scores[0] += totals[1] - totals[0];
			// if 1 > 2, 2 wins
		} else if (totals[1] < totals[0]) {
			game.setWinner(1);
			scores[1] += totals[0] - totals[1];
			// otherwise there is a tie
		} else {
			game.setWinner(-2);
		}
		game.setStatus(Status.DONE_STATE);
	}

	public static Strategy chooseStrategy() {
		String[] list = { "Adam", "Calum", "CJ", "Ethan", "Faith-Anne",
				"Jacob", "Matt", "Paul", "Sebastian" };
		JComboBox<String> jcb = new JComboBox<String>(list);
		String opponent = (String) JOptionPane.showInputDialog(null, "Choose an Opponent:", null, JOptionPane.QUESTION_MESSAGE, null, list, list[0]);
		switch (opponent) {
		case "Adam":
			return new AdamStrategy();
		case "Calum":
			return new CalumStrategy();
		case "CJ":
			return new CJStrategy();
		case "Ethan":
			return new EthanStrategy();
		case "Faith-Anne":
			return new FaithStrategy();
		case "Jacob":
			return new JacobStrategy();
		case "Matt":
			return new MattStrategy();
		case "Paul":
			return new PaulStrategy();
		case "Sebastian":
			return new SebastianStrategy();
		default:
			return new FaithStrategy();
		}
	}
	
	public static String getGamesLeft() {
		return String.valueOf(gamesRemaining);
	}

	public static void main(String[] args) {
		int players = 2;
		int[] scores = { 0, 0 };
		gamesRemaining = 5;
		GameMgr gm = new GameMgr();

		// Load the initial game.
		gm.resetGame(players, scores);
		// And decrement the games remaining.
		gamesRemaining--;

		Display display = new Display();
		JFrame frame = new JFrame("Dominoes!");
		frame.add(display);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(967, 700);
		frame.setVisible(true);

		boolean playing = true;
		// Continue to play until we've exhausted all the remaining games.
		while (playing) {
			if (game.getStatus() == Status.DONE_STATE) {
				scores = gm.resetGame(players, scores);
				gamesRemaining--;
			}
			if (gamesRemaining <= 0) {
				playing = false;
			}
		}
	}
}