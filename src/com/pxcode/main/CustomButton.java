package com.pxcode.main;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.pxcode.entity.GameObject;
import com.pxcode.graphic.Renderer;

public class CustomButton implements GameObject {

	private BufferedImage image;
	private BufferedImage hover;
	private BufferedImage targeted;
	private Point pos;

	public CustomButton(String path, String hoverPath, Point pos) {
		this.pos = pos;
		image = Game.loadImage(path);
		hover = Game.loadImage(hoverPath);
		targeted = image;
	}

	@Override
	public void render(Renderer renderer) {
		renderer.renderImage(targeted, pos.x, pos.y);
	}

	@Override
	public void update() {
		Point p = new Point(Game.instance.mouse.x, Game.instance.mouse.y);
		if (check(p)) {
			targeted = hover;
		} else {
			targeted = image;
		}
	}

	public boolean check(Point p) {
		return (p.x >= pos.x && p.x <= pos.x + image.getWidth() && p.y >= pos.y && p.y <= pos.y + image.getHeight());
	}

}
