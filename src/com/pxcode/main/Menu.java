package com.pxcode.main;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.pxcode.entity.GameObject;
import com.pxcode.graphic.Renderer;
import com.pxcode.utility.GameState;

public class Menu implements GameObject {

	private BufferedImage background;
	private CustomButton playBtn, quitBtn;

	public Menu() {
		background = Game.loadImage("sprites/backgrounds/background_menu.png");
		playBtn = new CustomButton("sprites/buttons/play.png", "sprites/buttons/hover_play.png", new Point(310, 259));
		quitBtn = new CustomButton("sprites/buttons/quit.png", "sprites/buttons/hover_quit.png", new Point(310, 501));
	}

	@Override
	public void render(Renderer renderer) {
		renderer.renderImage(background, 0, 0);
		playBtn.render(renderer);
		quitBtn.render(renderer);
	}

	@Override
	public void update() {
		playBtn.update();
		quitBtn.update();
	}

	public void triggerClick(Point point) {
		System.out.println(point);
		if (playBtn.check(point)) {
			Game.state = GameState.PLAYING;
		} else if (quitBtn.check(point)) {
			System.exit(0);
		}

	}

}
