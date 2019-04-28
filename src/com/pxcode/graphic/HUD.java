
package com.pxcode.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
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

	private void renderUnitStats(Graphics2D g) {
		if (unitStats != null) {
			g.setColor(Color.WHITE);
			g.drawString(String.valueOf(unitStats.getAttackDamage()), 81, 765);
			g.drawString(String.valueOf(unitStats.getArmor()), 189, 765);
			g.drawString(String.valueOf(unitStats.getMovementRange()), 81, 820);
			g.drawString(String.valueOf(unitStats.getAttackRange()), 189, 820);
		}
	}
	
	public void renderStrings(Graphics2D g) {
		renderUnitStats(g);
	}

	@Override
	public void update() {

	}

}
