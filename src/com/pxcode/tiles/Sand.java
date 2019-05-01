package com.pxcode.tiles;

import java.util.Random;

import com.pxcode.main.Game;

public class Sand extends Tile {

	public Sand(int x, int y) {
		super(x, y);
		Random r = new Random();
		if (r.nextInt(10) > 7) {
			tileSprite = Game.loadImage("sprites/tiles/hexasand_noaccess.png");
			isMovementPermitted = false;
		} else
			tileSprite = Game.loadImage("sprites/tiles/hexasand.png");
	}

	public Sand(int x, int y, boolean isAccessible) {
		super(x, y);
		if (!isAccessible)
			tileSprite = Game.loadImage("sprites/tiles/hexasand_noaccess.png");
		else
			tileSprite = Game.loadImage("sprites/tiles/hexasand.png");

		isMovementPermitted = isAccessible;
	}

}
