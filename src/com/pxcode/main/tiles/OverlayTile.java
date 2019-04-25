package com.pxcode.main.tiles;

import com.pxcode.main.Game;

public class OverlayTile extends Tile {

	public OverlayTile(int x, int y) {
		super(x, y);
		tileSprite = Game.loadImage("res/hexaborder.png");
	}

}
