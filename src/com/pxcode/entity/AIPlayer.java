package com.pxcode.entity;

import com.pxcode.entity.unit.Unit;
import com.pxcode.main.Game;

public class AIPlayer extends Player {

	public AIPlayer(byte teamIndex, String username) {
		super(teamIndex, username);
	}

	public void playRole(Game game){
		Thread AIThread = new Thread() {
			public void run() {
				for (Unit u : units) {
					if (!u.isDead() && !u.isRolePlayed()) {
						try {
							u.playAI(game, u.getTile());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						u.setRolePlayed(false);
					}
				}
			}
		};
		AIThread.start();

	}

}
