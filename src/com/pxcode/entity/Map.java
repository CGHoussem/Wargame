package com.pxcode.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.pxcode.entity.unit.Unit;
import com.pxcode.graphic.Renderer;
import com.pxcode.main.Game;
import com.pxcode.tiles.Grass;
import com.pxcode.tiles.Mud;
import com.pxcode.tiles.OverlayTile;
import com.pxcode.tiles.Sand;
import com.pxcode.tiles.Snow;
import com.pxcode.tiles.Stone;
import com.pxcode.tiles.Tile;
import com.pxcode.tiles.Water;
import com.pxcode.utility.MapType;
import com.pxcode.utility.SerializableTile;
import com.pxcode.utility.TileType;

public class Map implements GameObject, Serializable {

	private static final long serialVersionUID = 7803934008037618715L;
	private static final int MAX_TILES = 67;

	private long index;
	private MapType type;
	private List<Tile> tiles;
	private List<Tile> overlay;
	private int hoverX, hoverY;
	private static BufferedImage backgroundImage = null;

	public Map() {
		hoverX = hoverY = 0;
		tiles = new ArrayList<>();
		index = generateIndex();
		type = chooseRandomType();
		overlay = initializeOverlay();
	}

	public Map(final String levelName) {
		hoverX = hoverY = 0;
		tiles = new ArrayList<>();
		overlay = initializeOverlay();
		loadMap(levelName);
	}

	private MapType chooseRandomType() {
		List<MapType> types = new ArrayList<>();
		types.add(MapType.MOUNTAIN);
		types.add(MapType.FOREST);
		types.add(MapType.DESERT);
		Random r = new Random();
		return types.get(r.nextInt(3));
	}

	private long generateIndex() {
		try {
			LocalDateTime ldt = LocalDateTime.now(ZoneId.systemDefault());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uDHmS", Locale.getDefault());
			String indexFormatted = ldt.format(dtf);
			return Long.parseLong(indexFormatted);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private List<Tile> initializeOverlay() {
		List<Tile> overlay = new ArrayList<>();
		int w = Tile.WIDTH;
		int h = Tile.HEIGHT;
		int offsetX = 58;
		int offsetY = 34;
		boolean oddRow = true;

		for (int y = 0; y < Game.SCALE_HEIGHT; y++) {
			for (int x = 0; x < Game.SCALE_WIDTH; x++) {
				Tile tile = new OverlayTile(w * x, (h - offsetY) * y);
				if (!oddRow) {
					tile.setX(w * x + offsetX);
				}
				if (!oddRow && x == 9)
					continue;
				overlay.add(tile);
			}
			oddRow = !oddRow;
		}
		return overlay;
	}

	public static Map generateMap() {
		Random rand = new Random();
		Map map = new Map();
		List<TileType> tileTypes = new ArrayList<>();
		switch (map.getType()) {
		case MOUNTAIN:
			tileTypes.add(TileType.STONE);
			tileTypes.add(TileType.SNOW);
			backgroundImage = Game.loadImage("res/background_mountain.png");
			break;
		case FOREST:
			tileTypes.add(TileType.GRASS);
			tileTypes.add(TileType.WATER);
			backgroundImage = Game.loadImage("res/background_forest.png");
			break;
		case DESERT:
			tileTypes.add(TileType.SAND);
			tileTypes.add(TileType.MUD);
			backgroundImage = Game.loadImage("res/background_desert.png");
			break;
		}
		int w = Tile.WIDTH;
		int h = Tile.HEIGHT;
		int offsetX = 58;
		int offsetY = 34;
		boolean oddRow = true;

		for (int y = 0; y < Game.SCALE_HEIGHT; y++) {
			for (int x = 0; x < Game.SCALE_WIDTH; x++) {
				TileType choice = tileTypes.get(rand.nextInt(tileTypes.size()));
				Tile tile = null;
				switch (choice) {
				case GRASS:
					tile = new Grass(w * x, (h - offsetY) * y);
					break;
				case SAND:
					tile = new Sand(w * x, (h - offsetY) * y);
					break;
				case STONE:
					tile = new Stone(w * x, (h - offsetY) * y);
					break;
				case WATER:
					tile = new Water(w * x, (h - offsetY) * y);
					break;
				case MUD:
					tile = new Mud(w * x, (h - offsetY) * y);
					break;
				case SNOW:
					tile = new Snow(w * x, (h - offsetY) * y);
					break;
				}
				if (!oddRow) {
					tile.setX(w * x + offsetX);
				}
				if (!oddRow && x == 9)
					continue;
				map.tiles.add(tile);
			}
			oddRow = !oddRow;
		}

		return map;
	}

	public void saveMap() {
		try {
			String filePath = "res/map" + index + ".mp";

			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

			objectOut.writeObject(index);
			objectOut.writeObject(type);
			for (Tile tile : tiles) {
				objectOut.writeObject(new SerializableTile(tile));
			}

			objectOut.close();
			fileOut.close();
			System.out.println("The map has been saved to " + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMap(final String levelName) {
		try {
			String filePath = "res/" + levelName + ".mp";
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			List<Object> objects = new ArrayList<>();
			int count = 0;

			index = (long) objectIn.readObject();
			type = (MapType) objectIn.readObject();
			switch (type) {
			case MOUNTAIN:
				backgroundImage = Game.loadImage("res/background_mountain.png");
				break;
			case DESERT:
				backgroundImage = Game.loadImage("res/background_desert.png");
				break;
			case FOREST:
				backgroundImage = Game.loadImage("res/background_forest.png");
				break;
			}
			while ((count++) < MAX_TILES) {
				try {
					objects.add(objectIn.readObject());
				} catch (Exception e) {
					System.out.println("Exception Loading Map data number " + count);
				}
			}

			tiles.clear();
			objects.forEach(obj -> tiles.add(((SerializableTile) obj).convertToTile()));

			objectIn.close();
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), "Error loading the level " + levelName, "Dialog",
					JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException("Error loading the map");
		}
	}

	public void overTile(byte currentTeamPlaying, Point p) {
		Tile tile = pointToTile(p);
		if (tile != null) {
			Unit unit = tile.getUnit();
			if (unit != null && unit.getTeamIndex() != currentTeamPlaying) {
				unit.setAsEnemyHUD();
			}
		}

		hoverX = p.x;
		hoverY = p.y;
	}

	public Tile pointToTile(Point p) {
		int x = (int) p.getX();
		int y = (int) p.getY();
		for (Tile tile : tiles) {
			if (x > tile.getX() && x < tile.getX() + Tile.WIDTH && y > tile.getY() && y < tile.getY() + Tile.HEIGHT) {
				return tile;
			}
		}
		return null;
	}

	private void renderOverlay(Renderer renderer) {
		if (overlay != null) {
			for (int i = 0; i < overlay.size(); i++) {
				if (tiles.get(i).isMovementPermitted()) {
					Tile tile = overlay.get(i);
					if (hoverX > tile.getX() && hoverX < tile.getX() + Tile.WIDTH && hoverY > tile.getY()
							&& hoverY < tile.getY() + Tile.HEIGHT) {
						tile.render(renderer);
						break;
					}
				}
			}
		}
	}

	private void renderMap(Renderer renderer) {
		for (Tile tile : tiles) {
			tile.render(renderer);
		}

	}

	private void renderUnits(Renderer renderer) {
		tiles.forEach(tile -> {
			if (tile.getUnit() != null) {
				tile.getUnit().render(renderer);
			}
		});
	}

	@Override
	public void render(Renderer renderer) {
		renderMap(renderer);
		renderOverlay(renderer);
		renderUnits(renderer);
	}

	@Override
	public void update() {
		tiles.forEach(t -> {
			t.update();
			if (t.getUnit() != null && t.getUnit().isDead()) {
				t.setUnit(null);
			}
		});
	}

	public BufferedImage getBackground() {
		return backgroundImage;
	}

	public long getIndex() {
		return index;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}

	public MapType getType() {
		return type;
	}

}
