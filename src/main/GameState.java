package main;

import clutches.Board;
import clutches.BoneYard;
import clutches.Hand;
import strategies.Strategy;

public class GameState {

	public static final GameState INSTANCE = new GameState();
	
	private BoneYard boneYard;
	private Board board;
	private Hand[] hands;
	private Strategy[] strategies;
	private int[] scores;
	private Status status;
	private int numPlayers;
	private int currentPlayer;
	private int whoDominoed;
	private int winner;
	
	private GameState() {
	}
	
	public static GameState getInstance(){
		return INSTANCE;
	}
	
	public void setBoneYard(BoneYard boneYard){
		this.boneYard = boneYard;
	}
	
	public BoneYard getBoneYard(){
		return boneYard;
	}
	
	public void setBoard(Board board){
		this.board = board;
	}
	
	public Board getBoard(){
		return board;
	}
	
	public void setHands(Hand[] hands){
		this.hands = hands;
	}
	
	public Hand[] getHands(){
		return hands;
	}
	
	public void setStrategies(Strategy[] strategies){
		this.strategies = strategies;
	}
	
	public Strategy[] getStrategies(){
		return strategies;
	}
	
	public void setScores(int[] scores){
		this.scores = scores;
	}
	
	public int[] getScores(){
		return scores;
	}
	
	public void setStatus(Status status){
		this.status = status;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void setNumPlayers(int numPlayers){
		this.numPlayers = numPlayers;
	}
	
	public int getNumPlayers(){
		return numPlayers;
	}
	
	public void setCurrentPlayer(int currentPlayer){
		this.currentPlayer = currentPlayer;
	}
	
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	
	public void setWhoDominoed(int whoDominoed){
		this.whoDominoed = whoDominoed;
	}
	
	public int getWhoDominoed(){
		return whoDominoed;
	}
	
	public void setWinner(int winner){
		this.winner = winner;
	}
	
	public int getWinner(){
		return winner;
	}

	public void setStrategy(int player, Strategy strategy){
		this.strategies[player] = strategy;
	}
	
	public Strategy getStrategy(int player){
		return strategies[player];
	}

	public void setHand(int player, Hand hand){
		this.hands[player] = hand;
	}
	
	public Hand getHand(int player){
		return hands[player];
	}
	
	public void setScore(int player, int score){
		this.scores[player] = score;
	}
	
	public int getScore(int player){
		return scores[player];
	}
}