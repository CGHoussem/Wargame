package com.pxcode.entity.unit;

import com.pxcode.main.Game;

/*
 * Inspired from the game: League of Legends
 */

public class Graves extends RangedUnit {

	public Graves(int x, int y) {
		super(x, y, Game.loadImage("res/graves.png"));
	}

}
