package com.pxcode.entity.unit;

import java.awt.image.BufferedImage;

public class RangedUnit extends Unit {

	public RangedUnit(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
		attackRange = 3;
	}

}
