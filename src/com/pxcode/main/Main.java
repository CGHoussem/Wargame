package com.pxcode.main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String args[]) {
		try {
			Game game = new Game();

			JFrame frame = new JFrame(Game.NAME);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());
			frame.add(game, BorderLayout.CENTER);

			int width = (int) Math.ceil(Game.WIDTH * Game.GAME_SCALE);
			int height = (int) Math.ceil(Game.HEIGHT * Game.GAME_SCALE);
			Dimension d = new Dimension(width, height);
			frame.setSize(d);
			frame.setPreferredSize(d);
			frame.setMinimumSize(d);
			frame.setMaximumSize(d);
			frame.setVisible(true);
			frame.setResizable(false);

			game.start();
		} catch (Exception e) {
			StringBuffer str = new StringBuffer();
			for (StackTraceElement elt : e.getStackTrace()) {
				str.append("\n" + elt.toString());
			}
			JOptionPane.showMessageDialog(new JFrame(), "Exception: " + e.getMessage() + str, "Dialog",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
