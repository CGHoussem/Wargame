package com.pxcode.tiles;

import com.pxcode.main.Game;

public class Water extends Tile {

	public Water(int x, int y) {
		super(x, y);
		tileSprite = Game.loadImage("sprites/tiles/hexawater.png");
	}

}
