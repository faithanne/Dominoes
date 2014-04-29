package strategies;
import java.util.ArrayList;

import main.Dom;
import main.GameState;
import main.Loc;

public class CalumStrategy implements Strategy {

	static ArrayList<Dom> CalumDominoes = new ArrayList<Dom>();
	static int leftEnd;
	static int rightEnd;
	static int lastLeft = 0;
	static int lastRight = 0;
	int turn = 1;
	static int[] rankDoms;
	static int[] likelyRank;
	static int[] cantPlay = new int[7];
	double averageWeight;
	int dominoesInPlay = 0;

	void setDoms() {
		GameState game = GameState.getInstance();
		CalumDominoes = game.getHand(game.getCurrentPlayer()).getDoms();
	}

	@Override
	public boolean playTile() {
		GameState game = GameState.getInstance();
		setDoms();

		leftEnd = game.getBoard().getLeftEnd();
		rightEnd = game.getBoard().getRightEnd();
		rankDoms = new int[CalumDominoes.size()];
		likelyRank = new int[7];

		if (game.getBoard().getDoms().size() == turn
				&& CalumDominoes.size() != 7) {
			cantPlay[leftEnd] += 10;
			cantPlay[rightEnd] += 10;
		}

		if (game.getBoard().getDoms().size() < turn) {
			cantPlay = new int[7];
			turn = game.getBoard().getDoms().size();
			lastLeft = 0;
			lastRight = 0;
		}
		turn = game.getBoard().getDoms().size();

		rankLastPlay();
		rankWeight();
		loadLikeliness();
		rankLikeliness();
		canYouPlay();
		lastLeft = leftEnd;
		lastRight = rightEnd;

		int playDomIndex = selectDom();
		Dom dom;
		if (playDomIndex > -1) {
			turn++;
			dom = CalumDominoes.get(playDomIndex);
			game.getHand(game.getCurrentPlayer()).getDoms()
					.remove(playDomIndex);
			if (dom.getLeft() == leftEnd || dom.getRight() == leftEnd) {
				if (dom.getRight() != leftEnd) {
					dom.flip();
				}
				game.getBoard().addDom(Loc.LEFT, dom);
			} else {
				if (dom.getLeft() != rightEnd) {
					dom.flip();
				}
				game.getBoard().addDom(Loc.RIGHT, dom);
			}
			return true;
		}
		return false;
	}

	void rankWeight() {
		setAverageWeight();
		for (int i = 0; i < CalumDominoes.size(); i++) {
			Dom dom = CalumDominoes.get(i);
			if (dom.points() > 6) {
				rankDoms[i] += 6;
			}
			if (dom.points() > averageWeight) {
				rankDoms[i] += 2;
			}
			rankDoms[i] += dom.points();
		}
	}

	void canYouPlay() {
		for (int i = 0; i < CalumDominoes.size(); i++) {
			Dom dom = CalumDominoes.get(i);
			if (dom.getLeft() == leftEnd || dom.getLeft() == rightEnd
					|| dom.getRight() == leftEnd || dom.getRight() == rightEnd) {
				rankDoms[i] += 10;
			} else {
				rankDoms[i] *= -1;
				rankDoms[i] += -10;
			}
		}
	}

	int selectDom() {
		int gauge = -1;
		int playIt = -1;
		for (int i = 0; i < rankDoms.length; i++) {
			if (rankDoms[i] > gauge) {
				gauge = rankDoms[i];
				playIt = i;
			}
		}
		return playIt;
	}

	@Override
	public String getName() {
		return "Calum";
	}

	public double getAverageWeight() {
		return averageWeight;
	}

	public void setAverageWeight() {
		double total = 0;
		for (int i = 0; i < CalumDominoes.size(); i++) {
			Dom dom = CalumDominoes.get(i);
			total += dom.points();
		}
		averageWeight = total / CalumDominoes.size();
	}

	public void loadLikeliness() {
		GameState game = GameState.getInstance();
		for (int i = 0; i < game.getBoard().getDoms().size(); i++) {
			Dom dom = game.getBoard().getDoms().get(i);
			likelyRank[dom.getLeft()] += 2;
			likelyRank[dom.getRight()] += 2;
		}
		for (int i = 0; i < CalumDominoes.size(); i++) {
			Dom dom = CalumDominoes.get(i);
			likelyRank[dom.getLeft()] += 2;
			likelyRank[dom.getRight()] += 2;
		}
	}

	public void rankLikeliness() {
		for (int i = 0; i < CalumDominoes.size(); i++) {
			Dom dom = CalumDominoes.get(i);
			if (dom.getLeft() == leftEnd) {
				rankDoms[i] += likelyRank[rightEnd];
				rankDoms[i] += cantPlay[rightEnd];
			}
			if (dom.getLeft() == rightEnd) {
				rankDoms[i] += likelyRank[leftEnd];
				rankDoms[i] += cantPlay[leftEnd];
			}
			if (dom.getRight() == leftEnd) {
				rankDoms[i] += likelyRank[rightEnd];
				rankDoms[i] += cantPlay[rightEnd];
			}
			if (dom.getRight() == rightEnd) {
				rankDoms[i] += likelyRank[leftEnd];
				rankDoms[i] += cantPlay[leftEnd];
			}
		}
	}

	void rankLastPlay() {
		if (lastLeft > lastRight && lastRight != rightEnd) {
			likelyRank[lastLeft] += 5;
		}
		if (lastRight > lastLeft && lastLeft != leftEnd) {
			likelyRank[lastRight] += 5;
		}
	}
}