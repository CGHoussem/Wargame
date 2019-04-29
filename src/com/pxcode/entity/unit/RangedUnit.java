package com.pxcode.entity.unit;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class RangedUnit extends Unit {

	public RangedUnit(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
		stats.setBaseAttackRange(3);
		stats.setAttackRange(3);
	}

	public RangedUnit(int index, byte teamIndex, Stats stats, Point pos, BufferedImage sprite) {
		super(index, teamIndex, stats, pos, sprite);
	}

}
