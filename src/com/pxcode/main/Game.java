package com.pxcode.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.pxcode.entity.AIPlayer;
import com.pxcode.entity.HumanPlayer;
import com.pxcode.entity.Map;
import com.pxcode.entity.Player;
import com.pxcode.entity.unit.Graves;
import com.pxcode.entity.unit.Kayle;
import com.pxcode.entity.unit.Nashor;
import com.pxcode.entity.unit.Sion;
import com.pxcode.entity.unit.Unit;
import com.pxcode.graphic.HUD;
import com.pxcode.graphic.Renderer;
import com.pxcode.tiles.Tile;
import com.pxcode.utility.UnitType;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -2728293013612036141L;

	public static Game instance = null;
	public static final String NAME = "Wargame";
	public static final int SCALE_WIDTH = 10;
	public static final int SCALE_HEIGHT = 7;
	public static final int WIDTH = 1130;
	public static final int HEIGHT = 910;
	public static final int PLATFORM_Y_OFFSET = 14;
	public static final int TIMEOUT = 30;
	public static final long ROLE_TIMEOUT = 1000L * TIMEOUT;

	public static boolean isDebug = false;
	public static UnitType unitToBeSpawn;

	public Renderer renderer;
	public Map map;
	public HUD hud;
	public MouseHandler mouse = new MouseHandler();
	public KeyHandler keyboard = new KeyHandler();
	public Graphics2D graphics;
	public Player[] players = new Player[2];
	public byte currentTeamPlaying = 0;
	public int countdownTimer = TIMEOUT;

	private Timer roleTimer;
	private TimerTask roleTimeLimiter;
	private Unit previousUnit = null;
	private Tile previousTile = null;

	private boolean running = false;

	public Game() {
		if (instance == null) {
			instance = this;
		} else {
			System.exit(1);
		}

		Font font = null;
		InputStream fontFile = this.getClass().getResourceAsStream("../../../../res/Gamer.ttf");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		font = font.deriveFont(40f);
		setFont(font);

		renderer = new Renderer(WIDTH, HEIGHT);
		hud = new HUD();

		// Initialize the players
		players[0] = new HumanPlayer((byte) 0, "PxHoussem");
		players[1] = new AIPlayer((byte) 1, "PxAI");

		map = new Map("map201912022361");
		// map = Map.generateMap();

		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addKeyListener(keyboard);

		roleTimeLimiter = new TimerTask() {
			public void run() {
				switchTeams();
				resetTimer();
			}
		};
		roleTimer = new Timer("RoleTimer");
		roleTimer.scheduleAtFixedRate(roleTimeLimiter, 0L, ROLE_TIMEOUT);
	}

	private void notifyPlayers() {
		// TODO: Notify the players for the role changing!
	}

	private void switchTeams() {
		notifyPlayers();
		for (Unit u : players[currentTeamPlaying].getUnits()) {
			u.setRolePlayed(false);
		}
		if (Unit.focusedUnit != null) {
			previousUnit.unfocus();
			previousUnit.hidePossbilities(this);
		}
		currentTeamPlaying = (byte) (currentTeamPlaying == 1 ? 0 : 1);
		countdownTimer = TIMEOUT;
	}

	private void resetTimer() {
		countdownTimer = TIMEOUT;
		roleTimer.cancel();
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
				countdownTimer--;
				if (countdownTimer <= 0) {
					switchTeams();
					resetTimer();
				}
				System.out.println(ticks + " ticks, " + frames + " FPS");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void triggerClick(MouseEvent e) {
		// TODO: Testing Functionalities
		Tile tile = map.pointToTile(e.getPoint());
		if (tile != null) {
			Unit unit = tile.getUnit();

			if (!isDebug) {
				// there is a focused unit
				if (Unit.unitFocused && Unit.focusedUnit != null) {
					if (unit != null && !unit.isRolePlayed()) { // There is a unit on the tile
						// Same team
						if (unit.getTeamIndex() == currentTeamPlaying) {
							// Same unit
							if (unit == previousUnit) {
								unit.unfocus();
								previousUnit.hidePossbilities(this);
								// Ally unit
							} else {
								previousUnit.unfocus();
								previousUnit.hidePossbilities(this);
								unit.focus();
								unit.showPossbilities(this, tile);
								previousUnit = unit;
							}
							// Enemy team
						} else if (unit.getTeamIndex() != currentTeamPlaying) {
							// Attempt Attacking
							if (previousUnit.attack(tile)) {
								if (Unit.focusedUnit != null) {
									previousUnit.unfocus();
									previousUnit.hidePossbilities(this);
								}
							}
						}
					} else { // There is not unit on the tile
						// Attempt Movement
						if (previousUnit.move(tile)) {
							previousTile.setUnit(null);
							if (Unit.focusedUnit != null) {
								previousUnit.unfocus();
								previousUnit.hidePossbilities(this);
							}
						}
					}
				} else if (unit != null && !unit.isRolePlayed()) {
					// first click on an ally unit
					if (unit.getTeamIndex() == currentTeamPlaying) {
						unit.focus();
						unit.showPossbilities(this, tile);
						previousUnit = unit;
					}
				}
			} else {
				switch (unitToBeSpawn) {
				case GRAVES:
					tile.setUnit(new Graves(tile.getX(), tile.getY()));
					break;
				case KAYLE:
					tile.setUnit(new Kayle(tile.getX(), tile.getY()));
					break;
				case SION:
					tile.setUnit(new Sion(tile.getX(), tile.getY()));
					break;
				case NASHOR:
					tile.setUnit(new Nashor(tile.getX() - 10, tile.getY() - 10));
					break;
				}
			}
			previousTile = tile;
		}
	}

	private void update() {
		// check if all roles of current team is played or not
		if (players[currentTeamPlaying].isRoleOver()) {
			switchTeams();
			resetTimer();
		}

		map.overTile(currentTeamPlaying, new Point(mouse.x, mouse.y));
		map.update();
		hud.update();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		graphics = (Graphics2D) bs.getDrawGraphics();
		map.render(renderer);
		hud.render(renderer);
		renderer.render(graphics);
		hud.renderTexts(graphics);

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

	public static int clamp(int value, int min_value, int max_value) {
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
