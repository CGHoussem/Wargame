package com.pxcode.main.tiles;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.pxcode.entities.Unit;
import com.pxcode.main.Game;
import com.pxcode.main.Renderer;

public abstract class Tile {

	public final static int WIDTH = 111;
	public final static int HEIGHT = 126;
	public static int count = 0;

	protected int index = 0;
	protected int x, y;
	protected Unit unit;
	protected boolean isAttackPermitted = false;
	protected boolean isMovementPermitted = true;
	protected BufferedImage tileSprite;
	protected BufferedImage canAttackSprite;
	protected BufferedImage cantAttackSprite;
	protected BufferedImage canMoveSprite;
	protected BufferedImage cantMoveSprite;

	public Tile(int x, int y) {
		this.index = (++count);
		this.x = x;
		this.y = y;
		canAttackSprite = Game.loadImage("res/attackGranted.png");
		cantAttackSprite = Game.loadImage("res/attackDenied.png");
		canMoveSprite = Game.loadImage("res/mvtGranted.png");
		cantMoveSprite = Game.loadImage("res/mvtDenied.png");
	}
	
	public void render(Renderer renderer) {
		renderer.renderImage(tileSprite, x, y);
	}

	public void requestMouvement(Game game) {
		game.hud.addUnitElement(canMoveSprite, new Point(x, y));
		isMovementPermitted = true;
	}

	public void requestAttack(Game game) {
		game.hud.addUnitElement(canAttackSprite, new Point(x, y));
		isAttackPermitted = true;
	}

	public void requestBlockMouvement(Game game) {
		game.hud.addUnitElement(cantMoveSprite, new Point(x, y));
		isMovementPermitted = false;
	}

	public void requestBlockAttack(Game game) {
		game.hud.addUnitElement(cantAttackSprite, new Point(x, y));
		isAttackPermitted = false;
	}

	public void unrequestMouvement(Game game) {
		game.hud.removeUnitElement(canMoveSprite, new Point(x, y));
	}

	public void unrequestAttack(Game game) {
		game.hud.removeUnitElement(canAttackSprite, new Point(x, y));
	}

	public void unrequestBlockMouvement(Game game) {
		game.hud.removeUnitElement(cantMoveSprite, new Point(x, y));
	}

	public void unrequestBlockAttack(Game game) {
		game.hud.removeUnitElement(cantAttackSprite, new Point(x, y));
	}

	public void setImage(BufferedImage image) {
		this.tileSprite = image;
	}

	public int getIndex() {
		return index;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public BufferedImage getImage() {
		return tileSprite;
	}

	public boolean isAttackPermitted() {
		return isAttackPermitted;
	}

	public boolean isMovementPermitted() {
		return isMovementPermitted;
	}

	@Override
	public String toString() {
		String tileType = "Grass";
		if (this instanceof Sand)
			tileType = "Sand";
		else if (this instanceof Stone)
			tileType = "Stone";
		else if (this instanceof Water)
			tileType = "Water";
		else if (this instanceof Mud)
			tileType = "Mud";
		else if (this instanceof Snow)
			tileType = "Snow";
		return tileType + " (" + x + ", " + y + ")";
	}

}
