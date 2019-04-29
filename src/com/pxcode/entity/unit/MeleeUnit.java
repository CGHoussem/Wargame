package com.pxcode.entity.unit;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class MeleeUnit extends Unit {

	public MeleeUnit(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
		stats.setBaseAttackRange(1);
		stats.setAttackRange(1);
	}

	public MeleeUnit(int index, byte teamIndex, Stats stats, Point pos, BufferedImage sprite) {
		super(index, teamIndex, stats, pos, sprite);
	}
	
}
