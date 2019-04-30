package com.pxcode.utility;

import java.awt.Point;
import java.io.Serializable;

import com.pxcode.entity.unit.Graves;
import com.pxcode.entity.unit.Kayle;
import com.pxcode.entity.unit.Nashor;
import com.pxcode.entity.unit.Sion;
import com.pxcode.entity.unit.Stats;
import com.pxcode.entity.unit.Unit;

public class SerializableUnit implements Serializable {
	private static final long serialVersionUID = -2324065978327189004L;
	
	private final int count;
	private int index;
	private byte teamIndex;
	private Stats stats;
	private int x, y;
	private UnitType type;

	public SerializableUnit(Unit unit) {
		this.index = unit.getIndex();
		this.teamIndex = unit.getTeamIndex();
		this.stats = unit.getStats();
		count = Unit.count;
		x = unit.getX();
		y = unit.getY();
		if (unit instanceof Graves) {
			type = UnitType.GRAVES;
		} else if (unit instanceof Kayle) {
			type = UnitType.KAYLE;
		} else if (unit instanceof Sion) {
			type = UnitType.SION;
		} else if (unit instanceof Nashor) {
			type = UnitType.NASHOR;
		}
	}

	public Unit convertToUnit() {
		Unit unit = null;
		Point pos = new Point(x, y);
		Unit.count = count;
		switch (type) {
		case GRAVES:
			unit = new Graves(index, teamIndex, stats, pos);
			break;
		case KAYLE:
			unit = new Kayle(index, teamIndex, stats, pos);
			break;
		case SION:
			unit = new Sion(index, teamIndex, stats, pos);
			break;
		case NASHOR:
			unit = new Nashor(index, teamIndex, stats, pos);
			break;
		}
		return unit;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public byte getTeamIndex() {
		return teamIndex;
	}

	public void setTeamIndex(byte teamIndex) {
		this.teamIndex = teamIndex;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
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

	public UnitType getType() {
		return type;
	}

	public void setType(UnitType type) {
		this.type = type;
	}

}
