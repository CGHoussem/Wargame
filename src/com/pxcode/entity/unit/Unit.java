package com.pxcode.entity.unit;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.pxcode.entity.GameObject;
import com.pxcode.graphic.Renderer;
import com.pxcode.main.Game;
import com.pxcode.tiles.Tile;

public abstract class Unit implements GameObject {

	public static int count = 0;

	protected int index = 0;
	protected byte teamIndex;
	protected Point pos;
	protected final int MAX_HEALTH = 100;
	protected Stats stats;

	protected boolean isDead;
	protected boolean isFlipped;
	protected boolean isFocused;
	protected BufferedImage sprite, teamIndicator;
	protected List<List<Tile>> possibilities;

	protected static BufferedImage focusIndicator;
	public static boolean unitFocused = false;
	public static Unit focusedUnit = null;

	public Unit(int x, int y, BufferedImage sprite) {
		index = (count++);
		pos = new Point(x, y);
		this.sprite = sprite;
		isDead = isFlipped = isFocused = false;
		stats = new Stats(MAX_HEALTH, 50, 25, 2, 3);
		possibilities = new ArrayList<>();
		if (focusIndicator == null)
			focusIndicator = Game.loadImage("res/focusIndicator.png");
		setTeamIndex((byte) 0);
		if (x > Game.WIDTH / 2) {
			setTeamIndex((byte) 1);
			flip();
		}
	}

	public Unit(int index, byte teamIndex, Stats stats, Point pos, BufferedImage sprite) {
		this.index = index;
		setTeamIndex(teamIndex);
		this.stats = stats;
		this.pos = pos;
		this.sprite = sprite;
		possibilities = new ArrayList<>();
		if (focusIndicator == null)
			focusIndicator = Game.loadImage("res/focusIndicator.png");
		if (pos.x > Game.WIDTH / 2)
			flip();
	}

	public int getIndex() {
		return index;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isFlipped() {
		return isFlipped;
	}

	public void setFlipped(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public int getX() {
		return pos.x;
	}

	public void setX(int x) {
		this.pos.x = x;
	}

	public int getY() {
		return pos.y;
	}

	public void setY(int y) {
		this.pos.y = y;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public byte getTeamIndex() {
		return teamIndex;
	}

	public void flip() {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-sprite.getWidth(null), 0);

		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		sprite = op.filter(sprite, null);
	}

	public void setTeamIndex(byte teamIndex) {
		this.teamIndex = teamIndex;
		if (teamIndex == 0)
			teamIndicator = Game.loadImage("res/blueUnitIndicator.png");
		else
			teamIndicator = Game.loadImage("res/redUnitIndicator.png");
	}

	private void renderHealthBar(Renderer renderer) {
		int xOffset = teamIndex == 0 ? 20 : 88;
		int healthToY = (int) ((100 - stats.getHealth()) * 48 * 0.01);
		int fillY = 48 - healthToY;
		healthToY += 21;

		Rectangle rect = new Rectangle(pos.x + xOffset, pos.y + 20 + Game.PLATFORM_Y_OFFSET, 10, 50);
		// DRAWING BORDER with BLACK COLOR
		renderer.renderRectangle(rect, 0x0);
		// DRAWING BACKGROUND with RED COLOR
		rect = new Rectangle(pos.x + xOffset + 1, pos.y + 21 + Game.PLATFORM_Y_OFFSET, 8, 48);
		renderer.renderRectangle(rect, 0xFF0000);
		// DRAWING HEALTH with GREEN COLOR
		rect = new Rectangle(pos.x + xOffset + 1, pos.y + healthToY + Game.PLATFORM_Y_OFFSET, 8, fillY);
		renderer.renderRectangle(rect, 0x00FF00);
	}

	@Override
	public void render(Renderer renderer) {
		renderer.renderImage(teamIndicator, pos.x, pos.y, 0, Game.PLATFORM_Y_OFFSET);
		renderer.renderImage(sprite, pos.x, pos.y, 0, Game.PLATFORM_Y_OFFSET);
		renderHealthBar(renderer);
		if (isFocused) {
			renderer.renderImage(focusIndicator, pos.x, pos.y, 0, Game.PLATFORM_Y_OFFSET);
		}
	}

	@Override
	public void update() {
		if (isFocused)
			Game.instance.hud.updateUnitStats(stats);
	}

	public void focus() {
		isFocused = true;
		unitFocused = true;
		Unit.focusedUnit = this;
		Game.instance.hud.addUnitStats(stats);
	}

	public void unfocus() {
		isFocused = false;
		unitFocused = false;
		Unit.focusedUnit = null;
		Game.instance.hud.removeUnitStats();
	}

	private void setPossibility(Game game, List<Tile> list, int index) {
		if (index < game.map.getTiles().size() && index >= 0)
			list.add(game.map.getTiles().get(index));
	}

	private void calculatePossibilities(Game game, Tile tile) {
		int tileIndex = game.map.getTiles().indexOf(tile);
		List<Tile> list1 = new ArrayList<>(), list2 = new ArrayList<>(), list3 = new ArrayList<>(),
				list4 = new ArrayList<>(), list5 = new ArrayList<>(), list6 = new ArrayList<>();
		for (int i = 1; i <= stats.getMovementRange(); i++) {
			setPossibility(game, list1, tileIndex + i);
			setPossibility(game, list2, tileIndex - i);
			setPossibility(game, list3, tileIndex + 10 * i - i);
			setPossibility(game, list4, tileIndex + 10 * i);
			setPossibility(game, list5, tileIndex - 10 * i);
			setPossibility(game, list6, tileIndex - 10 * i + i);
		}

		possibilities.add(list1);
		possibilities.add(list2);
		possibilities.add(list3);
		possibilities.add(list4);
		possibilities.add(list5);
		possibilities.add(list6);

	}

	public void showPossbilities(Game game, Tile tile) {
		calculatePossibilities(game, tile);

		possibilities.forEach(list -> {
			for (Tile t : list) {
				Unit u = t.getUnit();
				if (u != null) {
					if (u.getTeamIndex() != this.teamIndex) {
						t.requestAttack(game);
						break;
					} else {
						t.requestBlockMouvement(game);
						break;
					}
				} else if (t.isMovementPermitted()) {
					t.requestMouvement(game);
				} else {
					t.requestBlockMouvement(game);
					break;
				}
			}
		});

	}

	public void hidePossbilities(Game game) {
		possibilities.forEach(list -> {
			list.forEach(t -> {
				t.unrequestMouvement(game);
				t.unrequestAttack(game);
				t.unrequestBlockMouvement(game);
				t.unrequestBlockAttack(game);
			});
		});
		possibilities.clear();
	}

	private boolean isInPossibilities(Tile tile) {
		boolean contains = false;
		for (List<Tile> list : possibilities) {
			contains |= list.contains(tile);
		}
		return contains;
	}

	public boolean move(Tile destination) {
		if (destination.isMovementPermitted() && isInPossibilities(destination)) {
			pos.x = destination.getX();
			pos.y = destination.getY();
			destination.setUnit(this);
			return true;
		}
		return false;
	}

	public boolean attack(Tile enemyTile) {
		if (enemyTile.isAttackPermitted()) {
			enemyTile.getUnit().hurt(stats.getAttackDamage());
			return true;
		}
		return false;
	}

	public void die() {
		isDead = true;
		Game.instance.hud.removeUnitStats();
	}

	public void hurt(float damage) {
		float damageMultiplier = damage / (damage + stats.getArmor());
		int totalDamage = (int) (damage * damageMultiplier);
		if (totalDamage > 0) {
			stats.setHealth(stats.getHealth() - totalDamage);
			stats.setHealth(Game.clamp(stats.getHealth(), 0, MAX_HEALTH));
			if (stats.getHealth() == 0) {
				die();
			}
		}
	}

	public void setAsEnemyHUD() {
		Game.instance.hud.setEnemyUnit(this);
	}

}
