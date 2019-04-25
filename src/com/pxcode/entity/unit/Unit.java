package com.pxcode.entity.unit;

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

	protected byte teamIndex;
	protected int x, y;
	protected final float MAX_HEALTH = 100f;
	protected float health;
	protected float armor;
	protected int attackDamage;
	protected int mvtRange;
	protected int attackRange;
	protected BufferedImage sprite, teamIndicator, focusIndicator;
	protected boolean isDead;
	protected boolean isFlipped;
	protected boolean isFocused;
	protected List<List<Tile>> possibilities;

	public static boolean unitFocused = false;
	public static Unit focusedUnit = null;

	public Unit(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		isDead = isFlipped = isFocused = false;
		possibilities = new ArrayList<>();
		focusIndicator = Game.loadImage("res/focusIndicator.png");
		initStats();
		setTeamIndex((byte) 0);
		if (x > Game.WIDTH / 2) {
			flip();
			setTeamIndex((byte) 1);
		}
	}

	private void initStats() {
		health = 100f;
		armor = 50f;
		attackDamage = 25;
		mvtRange = 2;
		attackRange = 3;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getArmor() {
		return armor;
	}

	public void setArmor(float armor) {
		this.armor = armor;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public BufferedImage getTeamIndicator() {
		return teamIndicator;
	}

	public void setTeamIndicator(BufferedImage teamIndicator) {
		this.teamIndicator = teamIndicator;
	}

	public BufferedImage getFocusIndicator() {
		return focusIndicator;
	}

	public void setFocusIndicator(BufferedImage focusIndicator) {
		this.focusIndicator = focusIndicator;
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

	private void renderStats(Renderer renderer) {
		int xOffset = teamIndex == 0 ? 20 : 88;
		int healthToY = (int) ((100 - health) * 48 * 0.01);
		int fillY = 48 - healthToY;
		healthToY += 21;

		Rectangle rect = new Rectangle(x + xOffset, y + 20, 10, 50);
		// DRAWING BORDER with BLACK COLOR
		renderer.renderRectangle(rect, 0x0);
		// DRAWING BACKGROUND with RED COLOR
		rect = new Rectangle(x + xOffset + 1, y + 21, 8, 48);
		renderer.renderRectangle(rect, 0xFF0000);
		// DRAWING HEALTH with GREEN COLOR
		rect = new Rectangle(x + xOffset + 1, y + healthToY, 8, fillY);
		renderer.renderRectangle(rect, 0x00FF00);
	}

	@Override
	public void render(Renderer renderer) {
		renderer.renderImage(teamIndicator, x, y);
		renderer.renderImage(sprite, x, y);
		renderStats(renderer);
		if (isFocused) {
			renderer.renderImage(focusIndicator, x, y);
		}
	}

	@Override
	public void update() {
	}

	public void focus() {
		isFocused = true;
		unitFocused = true;
		Unit.focusedUnit = this;
	}

	public void unfocus() {
		isFocused = false;
		unitFocused = false;
		Unit.focusedUnit = null;
	}

	private void setPossibility(Game game, List<Tile> list, int index) {
		if (index < game.map.getTiles().size() && index >= 0)
			list.add(game.map.getTiles().get(index));
	}

	private void calculatePossibilities(Game game, Tile tile) {
		// TODO: Check for limits
		int tileIndex = game.map.getTiles().indexOf(tile);
		List<Tile> list1 = new ArrayList<>(), list2 = new ArrayList<>(), list3 = new ArrayList<>(),
				list4 = new ArrayList<>(), list5 = new ArrayList<>(), list6 = new ArrayList<>();
		for (int i = 1; i <= mvtRange; i++) {
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

	public boolean move(Tile destination) {
		if (destination.isMovementPermitted()) {
			x = destination.getX();
			y = destination.getY();
			destination.setUnit(this);
			return true;
		}
		return false;
	}

	public boolean attack(Unit enemyUnit) {
		System.out.println(
				"Attack " + enemyUnit.getClass().getSimpleName() + " [TEAM: " + enemyUnit.getTeamIndex() + "]");
		enemyUnit.hurt(attackDamage);
		return true;
	}

	public void die() {
		isDead = true;
		System.out.println("Unit " + getClass().getSimpleName() + " [TEAM: " + teamIndex + "] died!");
	}

	public void hurt(float damage) {
		float damageMultiplier = damage / (damage + armor);
		float totalDamage = damage * damageMultiplier;
		if (totalDamage > 0) {
			health -= totalDamage;
			health = Game.clamp(health, MAX_HEALTH, 0);
			System.out.println("Unit " + getClass().getSimpleName() + " [TEAM: " + teamIndex + "] took " + totalDamage
					+ " damage!");
			if (health == 0) {
				die();
			}
		} else {
			System.out.println("Damage blocked!");
		}
	}

	@Override
	public String toString() {
		String description = "Unit [NAME: " + getClass().getSimpleName() + "\nTEAM: " + getTeamIndex() + "\nHEALTH: "
				+ health + "\nARMOR: " + armor + "\nAD: " + attackDamage + "\nMVT RANGE: " + mvtRange + "]";
		return description;
	}

}
