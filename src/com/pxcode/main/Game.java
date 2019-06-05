package com.pxcode.main;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

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
import com.pxcode.utility.GameState;
import com.pxcode.utility.UnitType;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -2728293013612036141L;

	public static Game instance = null;
	public static final String NAME = "Wargame";
	public static final int SCALE_WIDTH = 10;
	public static final int SCALE_HEIGHT = 7;
	public static final int WIDTH = 1130;
	public static final int HEIGHT = 910;
	public static float GAME_SCALE = 1f;
	public static final int PLATFORM_Y_OFFSET = 14;
	public static final int TIMEOUT = 60;
	public static final long ROLE_TIMEOUT = 1000L * TIMEOUT;

	public static boolean isDebug = false;
	public static UnitType unitToBeSpawn;
	public static GameState state = GameState.MENU;

	public Renderer renderer;
	public Map map;
	public HUD hud;
	public Menu menu;
	public MouseHandler mouse = new MouseHandler();
	public KeyHandler keyboard = new KeyHandler();
	public Graphics2D graphics;
	public Player[] players = new Player[2];
	public byte currentTeamPlaying = 0;
	public int countdownTimer = TIMEOUT;
	public boolean isPaused = false;

	private Timer roleTimer;
	private Unit previousUnit = null;
	private Tile previousTile = null;
	private boolean hasAIPlayed = false;

	public Game() {
		if (instance == null) {
			instance = this;
		} else {
			System.exit(1);
		}

		// Initializing the font
		Font font = null;
		InputStream fontFile = ResourceLoader.load("fonts/Gamer.ttf");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		font = font.deriveFont(40f * Game.GAME_SCALE);
		setFont(font);

		// Initializing the renderer
		renderer = new Renderer(WIDTH, HEIGHT);

		// Initializing MENU + HUD
		menu = new Menu();
		hud = new HUD();

		// Initialize the players
		players[0] = new HumanPlayer((byte) 0, "PxHoussem");
		players[1] = new AIPlayer((byte) 1, "PxAI");

		// Loading / Generating a map
		// map = new Map("map20191211640");
		map = Map.generateMap();

		// Initialize the listeners
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addKeyListener(keyboard);

		new TimerTask() {
			public void run() {
				switchTeams();
				resetTimer();
			}
		};
	}

	private void switchTeams() {
		// hud.notifyPlayers(renderer);
		for (Unit u : players[currentTeamPlaying].getUnits()) {
			u.setRolePlayed(false);
		}
		if (Unit.focusedUnit != null) {
			previousUnit.unfocus();
			previousUnit.hidePossbilities(this);
		}
		currentTeamPlaying = (byte) (currentTeamPlaying == 1 ? 0 : 1);
		if (currentTeamPlaying == 0) {
			hasAIPlayed = false;
		}
		countdownTimer = TIMEOUT;
	}

	private void resetTimer() {
		countdownTimer = TIMEOUT;
		if (roleTimer != null)
			roleTimer.cancel();
	}

	private void pauseTimer() {
		if (roleTimer != null) {
			roleTimer.cancel();
			isPaused = true;
		}
	}

	public void start() {
		new Thread(this).start();
	}

	public void stop() {

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
				update();
				ticks++;
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
				if (!isPaused) {
					countdownTimer--;
					if (countdownTimer <= 0) {
						switchTeams();
						resetTimer();
					}
				}
				System.out.println(ticks + " ticks, " + frames + " FPS");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void triggerClick(MouseEvent e) {
		if (state == GameState.PLAYING) {
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
				} else if (unitToBeSpawn != null && tile.isMovementPermitted()) {
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
		} else if (state == GameState.MENU) {
			menu.triggerClick(e.getPoint());
		}
	}

	private void update() {
		if (state == GameState.PLAYING) {
			// check if one of the teams won
			if (players[0].getScore() != 0 || players[1].getScore() != 0) {
				if (players[0].isAllUnitsDead() || players[1].isAllUnitsDead()) {
					// blue team won
					if (players[0].getUnits().size() == 0) {
						hud.setTeamWon(1);
					} // red team won
					else {
						hud.setTeamWon(0);
					}
					pauseTimer();
					return;
				}
			}

			// play AI
			if (players[currentTeamPlaying] instanceof AIPlayer) {
				if (!hasAIPlayed) {
					((AIPlayer) players[currentTeamPlaying]).playRole(this);
					hasAIPlayed = true;
					switchTeams();
				}
			}

			// check if all roles of current team is played or not
			if (players[currentTeamPlaying] instanceof HumanPlayer && players[currentTeamPlaying].isRoleOver()) {
				switchTeams();
				resetTimer();
			}

			map.overTile(currentTeamPlaying, new Point(mouse.x, mouse.y));
			map.update();
			hud.update();
		} else if (state == GameState.MENU) {
			menu.update();
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		graphics = (Graphics2D) bs.getDrawGraphics();
		switch (state) {
		case MENU:
			menu.render(renderer);
			renderer.render(graphics);
			break;
		case INPUTTING:
			break;
		case PLAYING:
			map.render(renderer);
			hud.render(renderer);
			renderer.render(graphics);
			hud.renderTexts(graphics);
			break;
		case PAUSED:
			break;
		}

		graphics.dispose();
		bs.show();
		renderer.clear();
	}

	public static BufferedImage loadImage(String path) {
		try {
			BufferedImage loadedImage = ImageIO.read(ResourceLoader.load(path));
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

}
