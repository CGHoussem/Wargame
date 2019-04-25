package com.pxcode.gui;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import com.pxcode.entities.GameObject;
import com.pxcode.main.Renderer;

public class HUD implements GameObject {

	private Map<BufferedImage, Point> unitsElements;

	public HUD() {
		unitsElements = new HashMap<>();
	}

	public void addUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.put(sprite, coord);
	}

	public void removeUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.remove(sprite, coord);
	}

	@Override
	public void render(Renderer renderer) {
		try {
			unitsElements
					.forEach((sprite, point) -> renderer.renderImage(sprite, (int) point.getX(), (int) point.getY()));
		} catch (ConcurrentModificationException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void update() {

	}

}
