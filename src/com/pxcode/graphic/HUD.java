
package com.pxcode.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import com.pxcode.entity.GameObject;
import com.pxcode.entity.unit.Unit;
import com.pxcode.main.Game;

public class HUD implements GameObject {

	private Map<BufferedImage, Point> unitsElements;
	private Map<Integer, Unit> units;
	private BufferedImage overlayImage;

	public HUD() {
		unitsElements = new HashMap<>();
		units = new HashMap<>();
		overlayImage = Game.loadImage("res/overlay.png");
	}

	public void addUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.put(sprite, coord);
	}

	public void removeUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.remove(sprite, coord);
	}

	public void updateUnitStats(Unit unit) {
		units.remove(unit.getIndex());
		units.put(unit.getIndex(), unit);
	}

	public void addUnitStats(Unit unit) {
		// add unit
		units.put(unit.getIndex(), unit);
	}

	public void removeUnitStats(Unit unit) {
		// delete unit
		units.remove(unit.getIndex());
	}

	@Override
	public void render(Renderer renderer) {
		try {
			unitsElements.forEach((sprite, point) -> {
				renderer.renderImage(sprite, (int) point.getX(), (int) point.getY());
			});
			renderer.renderImage(overlayImage, 0, 0);
		} catch (ConcurrentModificationException e) {
			System.out.println(e.getMessage());
		}
	}

	public void renderStrings(Graphics2D g) {
		try {
			units.forEach((unitIndex, unit) -> {
				// DRAWING THE UNIT INDEX
				g.setColor(Color.BLACK);
				g.drawString(String.valueOf(unit.getIndex()), unit.getX() + 55, unit.getY() + 100);
				g.setColor(Color.RED);
				g.drawString(String.valueOf("AD: " + (int) unit.getStats().getAttackDamage()), unit.getX() + 35, unit.getY() + 20);
				g.setColor(Color.DARK_GRAY);
				g.drawString(String.valueOf("AR: " + (int) unit.getStats().getArmor()), unit.getX() + 35, unit.getY() + 35);
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	

	@Override
	public void update() {

	}

}
