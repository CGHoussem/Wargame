package com.pxcode.tiles;

import com.pxcode.main.Game;

public class Snow extends Tile {

	public Snow(int x, int y) {
		super(x, y);
		tileSprite = Game.loadImage("sprites/tiles/hexasnow.png");
	}

}
