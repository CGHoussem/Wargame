package com.pxcode.entities;

import java.awt.image.BufferedImage;

public class MeleeUnit extends Unit {

	public MeleeUnit(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
		attackRange = 1;
	}

}
