package com.pxcode.utility;

import java.io.Serializable;

import com.pxcode.tiles.Grass;
import com.pxcode.tiles.Mud;
import com.pxcode.tiles.Sand;
import com.pxcode.tiles.Snow;
import com.pxcode.tiles.Stone;
import com.pxcode.tiles.Tile;
import com.pxcode.tiles.Water;

public class SerializableTile implements Serializable {

	private static final long serialVersionUID = -5494448102429934537L;

	private TileType type;
	private int x;
	private int y;
	private boolean isAccessible;
	private SerializableUnit unit;

	public SerializableTile(Tile tile) {
		if (tile instanceof Grass)
			this.type = TileType.GRASS;
		else if (tile instanceof Sand)
			this.type = TileType.SAND;
		else if (tile instanceof Stone)
			this.type = TileType.STONE;
		else if (tile instanceof Water)
			this.type = TileType.WATER;
		else if (tile instanceof Mud)
			this.type = TileType.MUD;
		else if (tile instanceof Snow)
			this.type = TileType.SNOW;
		this.x = tile.getX();
		this.y = tile.getY();
		isAccessible = tile.isMovementPermitted();
		if (tile.getUnit() != null)
			unit = new SerializableUnit(tile.getUnit());
	}

	public Tile convertToTile() {
		Tile tile = null;
		switch (type) {
		case GRASS:
			tile = new Grass(x, y);
			break;
		case SAND:
			tile = new Sand(x, y, isAccessible);
			break;
		case STONE:
			tile = new Stone(x, y, isAccessible);
			break;
		case WATER:
			tile = new Water(x, y);
			break;
		case MUD:
			tile = new Mud(x, y);
			break;
		case SNOW:
			tile = new Snow(x, y);
			break;
		}
		if (unit != null)
			tile.setUnit(unit.convertToUnit());
		return tile;
	}

}
