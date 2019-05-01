package com.pxcode.tiles;

import com.pxcode.main.Game;

public class OverlayTile extends Tile {

	public OverlayTile(int x, int y) {
		super(x, y);
		tileSprite = Game.loadImage("sprites/tiles/hexaborder.png");
	}

}
