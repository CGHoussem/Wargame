package com.pxcode.entity.unit;

import com.pxcode.main.Game;

/*
 * Inspired from the game: League of Legends
 */

public class Kayle extends MeleeUnit {

	public Kayle(int x, int y) {
		super(x, y, Game.loadImage("res/kayle.png"));
		mvtRange = 3;
	}

}
