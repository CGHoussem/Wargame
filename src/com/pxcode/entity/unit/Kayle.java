package com.pxcode.entity.unit;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.pxcode.main.Game;

/*
 * Inspired from the game: League of Legends
 */

public class Kayle extends MeleeUnit {

	public Kayle(int x, int y) {
		super(x, y, Game.loadImage("res/kayle.png"));
		stats.setBaseAttackDamage(40);
		stats.setAttackDamage(40);
	}

	public Kayle(int index, byte teamIndex, Stats stats, Point pos) {
		super(index, teamIndex, stats, pos, Game.loadImage("res/kayle.png"));
	}
}
