package com.pxcode.entity.unit;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pxcode.entity.GameObject;
import com.pxcode.graphic.Renderer;
import com.pxcode.main.Game;
import com.pxcode.tiles.Tile;

public abstract class Unit implements GameObject {

	public static int count = 0;

	protected int index = 0;
	protected byte teamIndex;
	protected Point pos;
	protected Stats stats;

	protected boolean isDead;
	protected boolean isFlipped;
	protected boolean isFocused;
	protected boolean isRolePlayed;
	protected BufferedImage sprite, teamIndicator;
	protected List<List<Tile>> attackPossibilities;
	protected List<List<Tile>> movementPossibilities;

	protected static BufferedImage focusIndicator, roleOverIndicator;
	public static boolean unitFocused = false;
	public static Unit focusedUnit = null;

	public Unit(int x, int y, BufferedImage sprite) {
		index = (count++);
		pos = new Point(x, y);
		this.sprite = sprite;
		isDead = isFlipped = isFocused = isRolePlayed = false;
		stats = new Stats(100, 50, 25, 2, 3);
		attackPossibilities = new ArrayList<>();
		movementPossibilities = new ArrayList<>();
		focusIndicator = Game.loadImage("sprites/indicators/focusIndicator.png");
		roleOverIndicator = Game.loadImage("sprites/indicators/roleOver.png");
		setTeamIndex((byte) 0);
		if (x > Game.WIDTH * Game.GAME_SCALE / 2) {
			setTeamIndex((byte) 1);
			flip();
		}
		Game.instance.players[teamIndex].addUnit(this);
	}

	public Unit(int index, byte teamIndex, Stats stats, Point pos, BufferedImage sprite) {
		this.index = index;
		setTeamIndex(teamIndex);
		this.stats = stats;
		this.pos = pos;
		this.sprite = sprite;
		attackPossibilities = new ArrayList<>();
		movementPossibilities = new ArrayList<>();
		focusIndicator = Game.loadImage("sprites/indicators/focusIndicator.png");
		roleOverIndicator = Game.loadImage("sprites/indicators/roleOver.png");
		if (teamIndex == 1)
			flip();
		Game.instance.players[teamIndex].addUnit(this);
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

	public void setRolePlayed(boolean isRolePlayed) {
		this.isRolePlayed = isRolePlayed;
	}

	public boolean isRolePlayed() {
		return isRolePlayed;
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
			teamIndicator = Game.loadImage("sprites/indicators/blueUnitIndicator.png");
		else
			teamIndicator = Game.loadImage("sprites/indicators/redUnitIndicator.png");
	}

	private void renderHealthBar(Renderer renderer) {
		int xOffset = teamIndex == 0 ? 20 : 88;
		int healthToY = (int) ((stats.getBaseHealthPoints() - stats.getHealth()) * 48 * 0.01);
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
		if (isRolePlayed) {
			renderer.renderImage(roleOverIndicator, pos.x, pos.y, 0, Game.PLATFORM_Y_OFFSET);
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

	private void calculateAttackPossibilities(Game game, Tile tile) {
		int tileIndex = game.map.getTiles().indexOf(tile);
		List<Tile> list1 = new ArrayList<>(), list2 = new ArrayList<>(), list3 = new ArrayList<>(),
				list4 = new ArrayList<>(), list5 = new ArrayList<>(), list6 = new ArrayList<>();
		for (int i = 1; i <= stats.getAttackRange(); i++) {
			setPossibility(game, list1, tileIndex + i);
			setPossibility(game, list2, tileIndex - i);
			setPossibility(game, list3, tileIndex + 10 * i - i);
			setPossibility(game, list4, tileIndex + 10 * i);
			setPossibility(game, list5, tileIndex - 10 * i);
			setPossibility(game, list6, tileIndex - 10 * i + i);
		}

		attackPossibilities.add(list1);
		attackPossibilities.add(list2);
		attackPossibilities.add(list3);
		attackPossibilities.add(list4);
		attackPossibilities.add(list5);
		attackPossibilities.add(list6);
	}

	private void calculateMovementPossibilities(Game game, Tile tile) {
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

		movementPossibilities.add(list1);
		movementPossibilities.add(list2);
		movementPossibilities.add(list3);
		movementPossibilities.add(list4);
		movementPossibilities.add(list5);
		movementPossibilities.add(list6);
	}

	private void showAttackPossibilities(Game game, Tile tile) {
		attackPossibilities.forEach(list -> {
			for (Tile t : list) {
				Unit u = t.getUnit();
				if (u != null && u.getTeamIndex() != this.teamIndex) {
					t.unrequestBlockMouvement(game);
					t.requestAttack(game);
					break;
				}
			}
		});
	}

	private void showMovementPossibilities(Game game, Tile tile) {
		movementPossibilities.forEach(list -> {
			for (Tile t : list) {
				Unit u = t.getUnit();
				if (u != null || !t.isMovementPermitted()) {
					t.requestBlockMouvement(game);
					break;
				} else {
					t.requestMouvement(game);
				}
			}
		});
	}

	public void showPossbilities(Game game, Tile tile) {
		calculateMovementPossibilities(game, tile);
		calculateAttackPossibilities(game, tile);

		showMovementPossibilities(game, tile);
		showAttackPossibilities(game, tile);
	}

	public void hidePossbilities(Game game) {
		movementPossibilities.forEach(list -> {
			list.forEach(t -> {
				t.unrequestMouvement(game);
				t.unrequestBlockMouvement(game);
			});
		});
		attackPossibilities.forEach(list -> {
			list.forEach(t -> {
				t.unrequestAttack(game);
				t.unrequestBlockAttack(game);
			});
		});
		movementPossibilities.clear();
		attackPossibilities.clear();
	}

	private boolean isInAttackPossibilities(Tile tile) {
		boolean contains = false;
		for (List<Tile> list : attackPossibilities) {
			contains |= list.contains(tile);
		}
		return contains;
	}

	private boolean isInMovementPossibilities(Tile tile) {
		boolean contains = false;
		for (List<Tile> list : movementPossibilities) {
			contains |= list.contains(tile);
		}
		return contains;
	}

	public boolean move(Tile destination) {
		if (destination.isMovementPermitted() && isInMovementPossibilities(destination)) {
			pos.x = destination.getX();
			pos.y = destination.getY();
			destination.setUnit(this);
			isRolePlayed = true;
			return true;
		}
		return false;
	}

	public boolean attack(Tile enemyTile) {
		if (enemyTile.isAttackPermitted() && isInAttackPossibilities(enemyTile)) {
			enemyTile.getUnit().hurt(stats.getAttackDamage());
			isRolePlayed = true;
			return true;
		}
		return false;
	}

	private void AIAttack(Tile enemyTile) {
		enemyTile.getUnit().hurt(stats.getAttackDamage());
		isRolePlayed = true;
	}

	public void die() {
		isDead = true;
		Game.instance.hud.removeUnitStats();
		if (teamIndex == 1)
			Game.instance.players[0].addScore(1);
		else
			Game.instance.players[1].addScore(1);
	}

	public void hurt(float damage) {
		float damageMultiplier = damage / (damage + stats.getArmor());
		int totalDamage = (int) (damage * damageMultiplier);
		if (totalDamage > 0) {
			stats.setHealth(stats.getHealth() - totalDamage);
			stats.setHealth(Game.clamp(stats.getHealth(), 0, stats.getBaseHealthPoints()));
			if (stats.getHealth() == 0) {
				die();
			}
		}
	}

	public void setAsEnemyHUD() {
		Game.instance.hud.setEnemyUnit(this);
	}

	public Tile getTile() {
		// TODO: different conditions for big units
		List<Tile> tiles = Game.instance.map.getTiles();
		int offset = 0;
		if (this instanceof Nashor) {
			offset = 10;
		}
		for (Tile t : tiles) {
			if (pos.x + offset == t.getX() && pos.y + offset == t.getY()) {
				return t;
			}
		}
		return null;
	}

	public void playAI(Game game, Tile tile) throws InterruptedException {
		if (tile != null) {
			calculateAttackPossibilities(game, tile);
			calculateMovementPossibilities(game, tile);

			// execute attack move if possible and break the execution the method
			for (List<Tile> list : attackPossibilities) {
				for (Tile t : list) {
					Unit u = t.getUnit();
					if (u != null && u.getTeamIndex() != teamIndex) {
						AIAttack(t);
						TimeUnit.MILLISECONDS.sleep(300);
						Game.instance.render();
						return;
					}
				}
			}
			// execute first movement possibility
			for (List<Tile> list : movementPossibilities) {
				for (Tile t : list) {
					if (t.getUnit() == null && move(t)) {
						tile.setUnit(null);
						TimeUnit.MILLISECONDS.sleep(300);
						Game.instance.render();
						return;
					}
				}
			}
		} else {
			throw new RuntimeException("AI Unit Tile not found!");
		}
	}
}
