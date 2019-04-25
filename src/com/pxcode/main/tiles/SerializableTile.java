package com.pxcode.main.tiles;

import java.io.Serializable;

public class SerializableTile implements Serializable {

	private static final long serialVersionUID = -5494448102429934537L;

	private TileType type;
	private int x;
	private int y;

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
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Tile convertToTile() {
		Tile tile = null;
		switch (type) {
		case GRASS:
			tile = new Grass(x, y);
			break;
		case SAND:
			tile = new Sand(x, y);
			break;
		case STONE:
			tile = new Stone(x, y);
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

		return tile;
	}

	@Override
	public String toString() {
		return "SerializableTile [type=" + type + ", x=" + x + ", y=" + y + "]";
	}

}
