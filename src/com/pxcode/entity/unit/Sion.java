package com.pxcode.entity.unit;

import java.awt.Point;

import com.pxcode.main.Game;


/*
 * Inspired from the game: League of Legends
 */

public class Sion extends MeleeUnit {

	public Sion(int x, int y) {
		super(x, y, Game.loadImage("res/sion.png"));
		stats.setBaseArmor(80);
		stats.setArmor(80);
		stats.setBaseAttackDamage(17);
		stats.setAttackDamage(17);
		stats.setMovementRange(1);
		stats.setBaseMovementRange(1);
	}

	public Sion(int index, byte teamIndex, Stats stats, Point pos) {
		super(index, teamIndex, stats, pos, Game.loadImage("res/sion.png"));
	}

}
