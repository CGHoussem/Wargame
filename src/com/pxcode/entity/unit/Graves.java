package com.pxcode.entity.unit;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.pxcode.main.Game;

/*
 * Inspired from the game: League of Legends
 */

public class Graves extends RangedUnit {

	public Graves(int x, int y) {
		super(x, y, Game.loadImage("res/graves.png"));
	}

	public Graves(int index, byte teamIndex, Stats stats, Point pos, BufferedImage sprite) {
		super(index, teamIndex, stats, pos, sprite);
	}
}
