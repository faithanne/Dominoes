package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import clutchReps.FaBoardRep;
import clutches.Hand;
import displays.BoardPanel;
import displays.BoneYardPanel;
import displays.PlayerPanel;

@SuppressWarnings("serial")
public class Display extends JPanel {

	private Timer timer = new Timer(200, new TimerListener());
	private GameState game;
	private JTextField field;
	private PlayerPanel player0Panel;
	private PlayerPanel player1Panel;
	private BoardPanel boardPanel;
	private JLabel player0Score;
	private JLabel player1Score;
	private JLabel gamesLeft;
	private JLabel player1Label;
	private JLabel player0Label;

	public void startTimer() {
		timer.start();
	}

	public Display() {
		game = GameState.getInstance();
		setLayout(new BorderLayout());

		// create boneyard panel, add it to the west
		BoneYardPanel bonePanel = new BoneYardPanel();
		add(bonePanel, BorderLayout.WEST);

		for (Dom d : game.getBoneYard().getDoms()) {
			bonePanel.addDrawmino(new Drawmino(d));
		}

		// create player 1 panel, add it north
		player1Label = new JLabel(game.getStrategy(1).getName() + " Score: 0",
				JLabel.CENTER);
		player1Label.setForeground(Color.WHITE);
		player1Label.setOpaque(true);
		player1Label.setBackground(Color.BLACK);

		player1Panel = new PlayerPanel();
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(player1Label, BorderLayout.NORTH);
		panel1.add(player1Panel, BorderLayout.CENTER);
		add(panel1, BorderLayout.NORTH);
		for (Dom d : game.getHand(1).getDoms()) {
			player1Panel.addDrawmino(new Drawmino(d));
		}

		// create player 0 panel, add it south
		player0Label = new JLabel(game.getStrategy(0).getName() + " Score: 0",
				JLabel.CENTER);
		player0Label.setForeground(Color.WHITE);
		player0Label.setOpaque(true);
		player0Label.setBackground(Color.BLACK);

		player0Panel = new PlayerPanel();
		JPanel panel0 = new JPanel(new BorderLayout());
		panel0.add(player0Label, BorderLayout.SOUTH);
		panel0.add(player0Panel, BorderLayout.CENTER);
		add(panel0, BorderLayout.SOUTH);
		for (Dom d : game.getHand(0).getDoms()) {
			player0Panel.addDrawmino(new Drawmino(d));
		}

		// create board panel, add it center
		JPanel center = new JPanel(new BorderLayout());
		boardPanel = new BoardPanel();
		center.add(boardPanel, BorderLayout.CENTER);

		FaBoardRep boardRep = (FaBoardRep) game.getBoard().getRep();
		for (Dom d : boardRep.getLeftDoms()) {
			boardPanel.addLeftDrawmino(new Drawmino(d));
		}
		for (Dom d : boardRep.getRightDoms()) {
			boardPanel.addRightDrawmino(new Drawmino(d));
		}

		gamesLeft = new JLabel("Games Left: " + GameMgr.getGamesLeft(),
				JLabel.CENTER);
		gamesLeft.setForeground(Color.WHITE);
		gamesLeft.setOpaque(true);
		gamesLeft.setBackground(Color.BLACK);
		center.add(gamesLeft, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		timer.start();
	}

	public void callRepaint() {

		player0Panel.clearDrawminoes();
		for (Dom d : game.getHand(0).getDoms()) {
			player0Panel.addDrawmino(new Drawmino(d));
		}
		System.out.println("Hand" + game.getStrategy(0).getName() + ": "
				+ game.getHand(0).getDoms());
		player0Label.setText(game.getStrategy(0).getName() + " Score: "
				+ game.getScore(0));
		player0Panel.repaint();

		player1Panel.clearDrawminoes();
		for (Dom d : game.getHand(1).getDoms()) {
			player1Panel.addDrawmino(new Drawmino(d));
		}
		System.out.println("Hand" + game.getStrategy(1).getName() + ": "
				+ game.getHand(1).getDoms());
		player1Label.setText(game.getStrategy(1).getName() + " Score: "
				+ game.getScore(1));
		player1Panel.repaint();

		boardPanel.clearDrawminoes();
		FaBoardRep rep = (FaBoardRep) game.getBoard().getRep();

		System.out.println("display, leftDoms: " + rep.getLeftDoms());
		for (Dom d : rep.getLeftDoms()) {
			boardPanel.addLeftDrawmino(new Drawmino(d));
		}

		System.out.println("display, rightDoms: " + rep.getRightDoms());
		for (Dom d : rep.getRightDoms()) {
			boardPanel.addRightDrawmino(new Drawmino(d));
		}
		boardPanel.repaint();
		gamesLeft.setText("Games Left: " + GameMgr.getGamesLeft());
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public class TimerListener implements ActionListener {
		int pass = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			int currentPlayer = game.getCurrentPlayer();
			Hand hand = game.getHand(game.getCurrentPlayer());
			ArrayList<Dom> doms = hand.getDoms();
			if (game.getStatus() != Status.PLAY_STATE) {
				timer.stop();
				callRepaint();
				return;
			} else if (game.getStrategy(currentPlayer).playTile()) {
				pass = 0;
				callRepaint();
				if (hand.getDoms().isEmpty())
					GameMgr.calculateWinner();
			} else {
				if (doms.isEmpty()) {
					GameMgr.calculateWinner();
					return;
				}
				pass++;
				if (pass == 2) {
					game.setStatus(Status.DONE_STATE);
					GameMgr.calculateWinner();
					return;
				}
			}
			GameMgr.setNextPlayer();
		}
	}
}