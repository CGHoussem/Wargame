package com.pxcode.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.pxcode.entities.Graves;
import com.pxcode.entities.Map;
import com.pxcode.entities.Unit;
import com.pxcode.gui.HUD;
import com.pxcode.main.tiles.Tile;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -2728293013612036141L;

	public static Game instance = null;
	public static final String NAME = "Wargame";
	public static final int SCALE_WIDTH = 10;
	public static final int SCALE_HEIGHT = 7;
	public static final int WIDTH = 1110 + 20;
	public static final int HEIGHT = 720;

	public Renderer renderer;
	public Map map;
	public HUD hud;
	public MouseHandler mouse = new MouseHandler();
	public Player[] players = new Player[2];

	private Tile previousTile = null;
	private Unit previousUnit = null;

	public Graphics2D graphics;

	private boolean running = false;

	public Game() {
		if (instance == null) {
			instance = this;
		} else {
			System.exit(1);
		}

		// Initialize the players
		players[0] = new HumanPlayer((byte) 0, "PxHoussem");
		players[1] = new AIPlayer((byte) 1, "PxAI");

		renderer = new Renderer(WIDTH, HEIGHT);
		hud = new HUD();
		// map = new Map("map20191120255");
		map = Map.generateMap();
		// map.saveMap();

		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nanoSecondConversion = 1000000000.0 / 60;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		int ticks = 0;

		while (true) {
			boolean shouldRender = true;
			long now = System.nanoTime();
			delta += (now - lastTime) / nanoSecondConversion;
			lastTime = now;
			while (delta >= 1) {
				ticks++;
				update();
				delta--;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				render();
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(ticks + " ticks, " + frames + " FPS");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void triggerClick(MouseEvent e) {
		// TODO: Testing Functionalities
		Tile tile = map.pointToTile(e.getPoint());
		Unit unit = tile.getUnit();

		// If there isn't a unit in the tile
		if (unit == null) {
			// If the tile is accessible
			if (tile.isMovementPermitted()) {
				// if there isn't a focused unit
				if (!Unit.unitFocused) {
					// Instantiate Unit
					unit = new Graves(tile.getX(), tile.getY());
					tile.setUnit(unit);
				} else {
					// Move Unit
					if (previousUnit.move(tile)) {
						previousTile.setUnit(null);
						previousUnit.unfocus();
						previousUnit.hidePossbilities(this);
					}
				}
			}
		} else {
			// If it is the same unit in focus
			if (unit.isFocused()) {
				// UnFocus
				unit.unfocus();
				unit.hidePossbilities(this);
			} else if (!Unit.unitFocused) { // If there is not unit in focus
				unit.focus();
				unit.showPossbilities(this, tile);
				previousUnit = unit;
			} else { // If it is not the same unit + a unit is already in focus
				// Surely it's an enemy
				// Attack
				if (unit.attack(tile)) {
					unit.unfocus();
					unit.hidePossbilities(this);
				}
			}
		}
		previousTile = tile;
	}

	private void update() {
		map.overTile(new Point(mouse.x, mouse.y));
		map.update();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();
		map.render(renderer);
		hud.render(renderer);
		renderer.render(graphics);

		graphics.dispose();
		bs.show();
		renderer.clear();
	}

	public static BufferedImage loadImage(String path) {
		try {
			File f = new File(path);
			BufferedImage loadedImage = ImageIO.read(f);
			BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

			return formattedImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static float clamp(float value, float max_value, float min_value) {
		if (value >= max_value) {
			return max_value;
		} else if (value <= min_value) {
			return min_value;
		}

		return value;
	}

	public static void main(String args[]) {
		Game game = new Game();

		JFrame frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		frame.setVisible(true);
		frame.setResizable(false);

		game.start();
	}

}
