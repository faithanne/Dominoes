package fa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Display extends JPanel {

	private Timer timer = new Timer(3000, new TimerListener());
	private ArrayList<Dom> dominoes;
	private GameState g;
	private JTextField field;
	private int skip = 0;
	private PlayerPanel p2;
	private PlayerPanel p1;
	private BoardPanel boardPanel;
	private JLabel label;
	private int leftEnd = -1;
	private int rightEnd = -1;

	public Display() {
		this.g = GameState.getInstance();
		dominoes = new ArrayList<Dom>();

		setLayout(new BorderLayout());

		BoneYardPanel bonePanel = new BoneYardPanel();
		add(bonePanel, BorderLayout.WEST);

		dominoes = g.getBoneYard().getDoms();
		for (Dom d : dominoes) {
			bonePanel.addDrawmino(new Drawmino(d));
		}

		p2 = new PlayerPanel();
		add(p2, BorderLayout.NORTH);
		dominoes.clear();
		dominoes = g.getHand(1).getDoms();
		for (Dom d : dominoes) {
			p2.addDrawmino(new Drawmino(d));
		}

		p1 = new PlayerPanel();
		add(p1, BorderLayout.SOUTH);
		dominoes.clear();
		dominoes = g.getHand(0).getDoms();
		for (Dom d : dominoes) {
			p1.addDrawmino(new Drawmino(d));
		}

		JPanel center = new JPanel(new BorderLayout());
		boardPanel = new BoardPanel();
		center.add(boardPanel, BorderLayout.CENTER);

		FaBoardRep r = (FaBoardRep) g.getBoard().getRep();
		ArrayList<Dom> leftDoms = r.getLeftDoms();
		ArrayList<Dom> rightDoms = r.getRightDoms();

		for (Dom d : leftDoms) {
			boardPanel.addLeftDrawmino(new Drawmino(d));
		}
		for (Dom d : rightDoms) {
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

	public void changeCurrentPlayer() {
		int current = g.getCurrentPlayer();
		g.setCurrentPlayer((current + 1) % g.getNumPlayers());
		if (skip == 2) {
			g.setStatus(Status.DONE_STATE);
		}
		// label.setText("   Player" + g.getCurrentPlayer() + ": Which tile?");
	}

	public void callRepaint() {

		ArrayList<Dom> doms = new ArrayList<Dom>();
		if (g.getCurrentPlayer() == 0) {
			p1.clearDrawminoes();
			doms = g.getHand(0).getDoms();
			for (Dom d : doms) {
				p1.addDrawmino(new Drawmino(d));
			}
			p1.repaint();
			doms.clear();
		} else {
			p2.clearDrawminoes();
			doms = g.getHand(1).getDoms();
			for (Dom d : doms) {
				p2.addDrawmino(new Drawmino(d));
			}
			p2.repaint();
			doms.clear();
		}

		FaBoardRep rep = (FaBoardRep) g.getBoard().getRep();

		if (g.getBoard().getLeftEnd() != leftEnd) {
			boardPanel.addLeftDrawmino(new Drawmino(rep.peekLeft()));
			leftEnd = g.getBoard().getLeftEnd();
		} else if (rep.getRightDoms().isEmpty()){
			rightEnd = g.getBoard().getRightEnd();
		} else if (g.getBoard().getRightEnd() != rightEnd) {
			boardPanel.addRightDrawmino(new Drawmino(rep.peekRight()));
			rightEnd = g.getBoard().getRightEnd();
		}

		boardPanel.repaint();
		int winner = g.getWinner();
		if (winner == -2) {
			label.setText("Tie!");
		} else if (winner != -1) {
			label.setText("     Winner: " + g.getStrategy(winner).getName()
					+ "!");
		}
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public class TimerListener implements ActionListener {

		int pass = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			int currentPlayer = g.getCurrentPlayer();
			if (g.getStatus() != Status.PLAY_STATE)
				return;
			if (g.getStrategy(currentPlayer).playTile()) {
				pass = 0;
				if (g.getHand(currentPlayer).getDoms().size() == 0) {
					calculateWinner();
					return;
				}
				callRepaint();
			} else {
				pass++;
				if (pass == 2) {
					g.setStatus(Status.DONE_STATE);
					calculateWinner();
					return;
				}
			}
			changeCurrentPlayer();
		}
	}

	public void calculateWinner() {
		if (g.getHand(0).getDoms().isEmpty())
			g.setWinner(0);
		else if (g.getHand(0).getDoms().isEmpty())
			g.setWinner(1);
		else {
			ArrayList<ArrayList<Dom>> doms = new ArrayList<ArrayList<Dom>>();
			doms.add(g.getHand(0).getDoms());
			doms.add(g.getHand(1).getDoms());

			int[] totals = new int[2];

			for (int i = 0; i < 2; i++) {
				for (Dom d : doms.get(i)) {
					totals[i] += d.getLeft() + d.getRight();
				}
			}
			if (totals[0] < totals[1]) {
				g.setWinner(0);
			} else if (totals[1] < totals[0]) {
				g.setWinner(1);
			} else
				g.setWinner(-2);
		}
		timer.stop();
		callRepaint();
	}
}