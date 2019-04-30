package com.pxcode.entity.unit;

import java.awt.Point;

import com.pxcode.main.Game;

/*
 * Inspired from the game: League of Legends
 */

public class Nashor extends RangedUnit {

	public Nashor(int x, int y) {
		super(x, y, Game.loadImage("res/nashor.png"));
		stats.setBaseMovementRange(0);
		stats.setMovementRange(0);
		
		stats.setBaseAttackDamage(60);
		stats.setAttackDamage(60);
		
		stats.setBaseAttackRange(5);
		stats.setAttackRange(5);
		
		stats.setBaseArmor(50);
		stats.setArmor(50);
		
		stats.setBaseHealthPoints(500);
		stats.setHealth(500);
	}

	public Nashor(int index, byte teamIndex, Stats stats, Point pos) {
		super(index, teamIndex, stats, pos, Game.loadImage("res/nashor.png"));
	}
	
}
