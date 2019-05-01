package com.pxcode.tiles;

import java.util.Random;

import com.pxcode.main.Game;

public class Mud extends Tile {

	public Mud(int x, int y) {
		super(x, y);
		Random r = new Random();
		switch (r.nextInt(3)) {
		case 0:
			tileSprite = Game.loadImage("sprites/tiles/hexamud.png");
			break;
		case 1:
			tileSprite = Game.loadImage("sprites/tiles/hexamud2.png");
			break;
		case 2:
			tileSprite = Game.loadImage("sprites/tiles/hexamud3.png");
			break;
		}
	}

}
