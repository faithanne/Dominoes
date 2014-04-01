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

@SuppressWarnings("serial")
public class Display extends JPanel {

	private ArrayList<Dom> dominoes;
	private GameState g;
	private JTextField field;
	private int skip = 0;
	private PlayerPanel p2;
	private PlayerPanel p1;
	private BoardPanel boardPanel;
	private JLabel label;
	
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
		label = new JLabel("   Player" + g.getCurrentPlayer() + ": Which tile?");
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
		System.out.println(g.getCurrentPlayer() + " at Display constructor");

		left.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(field.getText());
				field.setText("");

				Hand hand = g.getHand(g.getCurrentPlayer());
				Dom d = hand.removeDom(index);
				Board board = g.getBoard();
				int leftEnd = board.getLeftEnd();
				if (d.getRight() == leftEnd) {
					board.addDom(0, d);
					boardPanel.addLeftDrawmino(new Drawmino(d));
					if (g.getCurrentPlayer() == 0) {
						p1.removeDrawmino(index);
					} else {
						p2.removeDrawmino(index);
					}
					skip = 0;
					board.setLeftEnd(d.getLeft());
					callRepaint();
				} else if (d.getLeft() == leftEnd) {
					d.flip();
					board.addDom(0, d);
					boardPanel.addLeftDrawmino(new Drawmino(d));
					if (g.getCurrentPlayer() == 0) {
						p1.removeDrawmino(index);
					} else {
						p2.removeDrawmino(index);
					}
					skip = 0;
					board.setLeftEnd(d.getLeft());
					callRepaint();
				} else {
					skip++;
					hand.addDom(-1, d);
					if (g.getCurrentPlayer() == 0) {
						p1.addDrawmino(new Drawmino(d));
					} else {
						p2.addDrawmino(new Drawmino(d));
					}
				}
				changeCurrentPlayer();
			}
		});

		right.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int index = Integer.parseInt(field.getText());
				field.setText("");
				Hand hand = g.getHand(g.getCurrentPlayer());

				Dom d = hand.removeDom(index);
				Board board = g.getBoard();
				int rightEnd = board.getRightEnd();
				if (d.getLeft() == rightEnd) {
					board.addDom(1, d);
					board.setRightEnd(d.getRight());
					boardPanel.addRightDrawmino(new Drawmino(d));
					if (g.getCurrentPlayer() == 0) {
						p1.removeDrawmino(index);
					} else {
						p2.removeDrawmino(index);
					}
					skip = 0;
					board.setRightEnd(d.getRight());
					callRepaint();
				} else if (d.getRight() == rightEnd) {
					d.flip();
					board.addDom(1, d);
					board.setRightEnd(d.getRight());
					boardPanel.addRightDrawmino(new Drawmino(d));
					if (g.getCurrentPlayer() == 0) {
						p1.removeDrawmino(index);
					} else {
						p2.removeDrawmino(index);
					}
					skip = 0;
					board.setRightEnd(d.getRight());
					callRepaint();
				} else {
					skip++;
					hand.addDom(-1, d);
					if (g.getCurrentPlayer() == 0) {
						p1.addDrawmino(new Drawmino(d));
					} else {
						p2.addDrawmino(new Drawmino(d));
					}
				}
				changeCurrentPlayer();
			}
		});

		pass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				skip++;
				changeCurrentPlayer();
			}
		});
	}

	public void changeCurrentPlayer() {
		int current = g.getCurrentPlayer();
		g.setCurrentPlayer((current + 1) % g.getNumPlayers());
		if (skip == 2) {
			g.setStatus(Status.DONE_STATE);
		}
		label.setText("   Player" + g.getCurrentPlayer() + ": Which tile?");
	}

	public void callRepaint() {
		p1.repaint();
		p2.repaint();
		boardPanel.repaint();
	}
}
