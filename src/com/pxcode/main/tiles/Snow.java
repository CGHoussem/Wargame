package com.pxcode.main.tiles;

import com.pxcode.main.Game;

public class Snow extends Tile {

	public Snow(int x, int y) {
		super(x, y);
		tileSprite = Game.loadImage("res/hexasnow.png");
	}

}
