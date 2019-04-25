package com.pxcode.main.tiles;

import java.util.Random;

import com.pxcode.main.Game;

public class Stone extends Tile {

	public Stone(int x, int y) {
		super(x, y);
		Random r = new Random();
		if (r.nextInt(10) >= 8) {
			int choice = r.nextInt(4);
			switch (choice) {
			case 0:
				tileSprite = Game.loadImage("res/hexastone.png");
				break;
			case 1:
				tileSprite = Game.loadImage("res/hexastone2.png");
				isMovementPermitted = false;
				break;
			case 2:
				tileSprite = Game.loadImage("res/hexastone3.png");
				isMovementPermitted = false;
				break;
			case 3:
				tileSprite = Game.loadImage("res/hexastone4.png");
				isMovementPermitted = false;
				break;
			}
		} else {
			tileSprite = Game.loadImage("res/hexastone.png");
		}
	}

}
