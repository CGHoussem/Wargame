package com.pxcode.tiles;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.pxcode.entity.GameObject;
import com.pxcode.entity.unit.Graves;
import com.pxcode.entity.unit.Kayle;
import com.pxcode.entity.unit.Stats;
import com.pxcode.entity.unit.Unit;
import com.pxcode.graphic.Renderer;
import com.pxcode.main.Game;

public abstract class Tile implements GameObject {

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

	@Override
	public void update() {
		if (unit != null) {
			Stats stats = unit.getStats();
			if (unit instanceof Graves) { // Graves
				if (this instanceof Mud) { // Mud
					// -1 movement range
					stats.setMovementRange(stats.getBaseMovementRange() - 1);
				} else if (this instanceof Water) { // Water
					// -2 movement range
					stats.setMovementRange(stats.getBaseMovementRange() - 2);
					// -25 armor
					stats.setArmor(stats.getBaseArmor() - 25);
				} else if (this instanceof Grass) { // Grass
					// +50 attack damage
					stats.setAttackDamage(stats.getBaseAttackDamage() + 50);
				} else if (this instanceof Sand) { // Sand
					// +2 attack range
					stats.setAttackRange(stats.getBaseAttackRange() + 2);
				} else if (this instanceof Snow) { // Snow
					// -1 movement range
					stats.setMovementRange(stats.getBaseMovementRange() - 1);
				} else {
					// reset
					stats.reset();
				}
			} else if (unit instanceof Kayle) { // Kayle
				if (this instanceof Snow) { // Snow
					// -1 movement range
					stats.setMovementRange(stats.getBaseMovementRange() - 1);
					// -35 armor
					stats.setArmor(stats.getBaseArmor() - 35);
				} else if (this instanceof Grass) { // Grass
					// +35 attack damage
					stats.setAttackDamage(stats.getBaseAttackDamage() + 35);
				} else if (this instanceof Water) { // Water
					// +30 armor
					stats.setArmor(stats.getBaseArmor() + 30);
				} else {
					stats.reset();
				}
			}
			unit.setStats(stats);
		}

	}

	@Override
	public void render(Renderer renderer) {
		renderer.renderImage(tileSprite, x, y, 0, Game.PLATFORM_Y_OFFSET);
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
}
