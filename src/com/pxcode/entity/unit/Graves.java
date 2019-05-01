package com.pxcode.entity.unit;

import java.awt.Point;

import com.pxcode.main.Game;

/*
 * Inspired from the game: League of Legends
 */

public class Graves extends RangedUnit {

	public Graves(int x, int y) {
		super(x, y, Game.loadImage("sprites/units/graves.png"));
	}

	public Graves(int index, byte teamIndex, Stats stats, Point pos) {
		super(index, teamIndex, stats, pos, Game.loadImage("sprites/units/graves.png"));
	}
}
