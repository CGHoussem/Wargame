
package com.pxcode.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import com.pxcode.entity.GameObject;
import com.pxcode.entity.unit.Stats;
import com.pxcode.entity.unit.Unit;
import com.pxcode.main.Game;

public class HUD implements GameObject {

	private Map<BufferedImage, Point> unitsElements;
	private Stats unitStats;
	private BufferedImage overlayImage;
	private Unit enemyUnit;

	public HUD() {
		unitsElements = new HashMap<>();
		overlayImage = Game.loadImage("res/overlay.png");
	}

	public void addUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.put(sprite, coord);
	}

	public void removeUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.remove(sprite, coord);
	}

	public void updateUnitStats(Stats stats) {
		unitStats = stats;
	}

	public void addUnitStats(Stats stats) {
		unitStats = stats;
	}

	public void removeUnitStats() {
		unitStats = null;
	}

	@Override
	public void render(Renderer renderer) {
		try {
			unitsElements.forEach((sprite, point) -> {
				renderer.renderImage(sprite, (int) point.getX(), (int) point.getY(), 0, Game.PLATFORM_Y_OFFSET);
			});
			renderer.renderImage(overlayImage, 0, 0);
		} catch (ConcurrentModificationException e) {
			System.out.println(e.getMessage());
		}
	}

	private void renderOutlinedText(Graphics2D g, String text, Color outline, Color fill, int x, int y) {
		AffineTransform oldAF = g.getTransform();
		g.translate(x, y);
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector glyphVector = Game.instance.getFont().createGlyphVector(frc, text);
		Shape textShape = glyphVector.getOutline();
		g.setColor(outline);
		g.draw(textShape);
		g.setColor(fill);
		g.fill(textShape);
		g.setTransform(oldAF);
	}

	private void renderUnitStats(Graphics2D g) {
		if (unitStats != null) {
			String health = String.valueOf(unitStats.getHealth());
			String ad = String.valueOf(unitStats.getAttackDamage());
			String ar = String.valueOf(unitStats.getArmor());
			String mr = String.valueOf(unitStats.getMovementRange());
			String mvtr = String.valueOf(unitStats.getAttackRange());

			renderOutlinedText(g, health, Color.BLACK, new Color(0x4dbf4c), Unit.focusedUnit.getX() + 50,
					Unit.focusedUnit.getY() + 40);
			renderOutlinedText(g, ad, Color.BLACK, Color.WHITE, 81, 765);
			renderOutlinedText(g, ar, Color.BLACK, Color.WHITE, 189, 765);
			renderOutlinedText(g, mr, Color.BLACK, Color.WHITE, 81, 820);
			renderOutlinedText(g, mvtr, Color.BLACK, Color.WHITE, 189, 820);

		}
	}

	public void setEnemyUnit(Unit unit) {
		enemyUnit = unit;
	}

	public void unsetEnemyUnit() {
		enemyUnit = null;
	}

	private void renderEnemyUnitHealth() {
		if (enemyUnit != null) {
			String health = String.valueOf(enemyUnit.getStats().getHealth());
			renderOutlinedText(Game.instance.graphics, health, Color.BLACK, Color.RED, enemyUnit.getX() + 50,
					enemyUnit.getY() + 40);
		}
	}

	public void renderTexts(Graphics2D g) {
		renderUnitStats(g);
		renderEnemyUnitHealth();
		unsetEnemyUnit();
	}

	@Override
	public void update() {
	}

}
