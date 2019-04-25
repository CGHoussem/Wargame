package com.pxcode.tiles;

import java.util.Random;

import com.pxcode.main.Game;

public class Grass extends Tile {

	public Grass(int x, int y) {
		super(x, y);
		Random r = new Random();
		switch (r.nextInt(3)) {
		case 0:
			tileSprite = Game.loadImage("res/hexagrass.png");
			break;
		case 1:
			tileSprite = Game.loadImage("res/hexagrass2.png");
			break;
		case 2:
			tileSprite = Game.loadImage("res/hexagrass3.png");
			break;
		}
	}

}
