package fa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Display extends JPanel {

	private Timer timer = new Timer(2000, new TimerListener());
	private GameState game;
	private JTextField field;
	private PlayerPanel player2Panel;
	private PlayerPanel player1Panel;
	private BoardPanel boardPanel;
	private JLabel label;

	public Display() {
		game = GameState.getInstance();
		setLayout(new BorderLayout());

		// create boneyard panel, add it to the west
		BoneYardPanel bonePanel = new BoneYardPanel();
		add(bonePanel, BorderLayout.WEST);

		for (Dom d : game.getBoneYard().getDoms()) {
			bonePanel.addDrawmino(new Drawmino(d));
		}

		// create player 2 panel, add it north
		player2Panel = new PlayerPanel();
		add(player2Panel, BorderLayout.NORTH);
		for (Dom d : game.getHand(1).getDoms()) {
			player2Panel.addDrawmino(new Drawmino(d));
		}

		// create player 1 panel, add it south
		player1Panel = new PlayerPanel();
		add(player1Panel, BorderLayout.SOUTH);
		for (Dom d : game.getHand(0).getDoms()) {
			player1Panel.addDrawmino(new Drawmino(d));
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

		JPanel j = new JPanel(new GridLayout(1, 5));

		JButton left = new JButton("Add Left");
		left.setBackground(Color.BLACK);
		left.setForeground(Color.WHITE);
		JButton pass = new JButton("Pass");
		pass.setBackground(Color.BLACK);
		pass.setForeground(Color.WHITE);
		JButton right = new JButton("Add Right");
		right.setBackground(Color.BLACK);
		right.setForeground(Color.WHITE);
		// label = new JLabel("   Player" + g.getCurrentPlayer() +
		// ": Which tile?");

		label = new JLabel();
		label.setForeground(Color.WHITE);
		label.setOpaque(true);
		label.setBackground(Color.BLACK);

		field = new JTextField();
		j.add(left);
		j.add(label);
		j.add(field);
		j.add(pass);
		j.add(right);
		center.add(j, BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);

		timer.start();
	}

	public void callRepaint() {

		player1Panel.clearDrawminoes();
		for (Dom d : game.getHand(0).getDoms()) {
			player1Panel.addDrawmino(new Drawmino(d));
		}
		player1Panel.repaint();

		player2Panel.clearDrawminoes();
		for (Dom d : game.getHand(1).getDoms()) {
			player2Panel.addDrawmino(new Drawmino(d));
		}
		player2Panel.repaint();

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

		if (game.getStatus() == Status.DONE_STATE) {
			int winner = game.getWinner();
			if (winner == -2) {
				label.setText("Tie!");
			} else if (winner != -1) {
				label.setText("     Winner: "
						+ game.getStrategy(winner).getName() + "!");
			}
		}
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public class TimerListener implements ActionListener {
		int pass = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			int currentPlayer = game.getCurrentPlayer();
			if (game.getStatus() != Status.PLAY_STATE) {
				timer.stop();
				callRepaint();
			} else if (game.getStrategy(currentPlayer).playTile()) {
				pass = 0;
				callRepaint();
			} else {
				pass++;
				if (pass == 2) {
					game.setStatus(Status.DONE_STATE);
					GameMgr.calculateWinner();
					return;
				}
			}
			GameMgr.setNextPlayer(currentPlayer);
		}
	}
}