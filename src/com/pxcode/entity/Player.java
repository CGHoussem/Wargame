package com.pxcode.entity;

import java.util.ArrayList;
import java.util.List;

import com.pxcode.entity.unit.Unit;

public abstract class Player {

	protected byte teamIndex;
	protected String username;
	protected int score;
	protected List<Unit> units;

	public Player(byte teamIndex, String username) {
		this.teamIndex = teamIndex;
		this.username = username;
		score = 0;
		units = new ArrayList<>();
	}

	public boolean isRoleOver() {
		boolean isAllRolePlayed = true;
		for (Unit u : getUnits()) {
			if (u.isDead())
				continue;
			isAllRolePlayed &= u.isRolePlayed(); 
		}
		return isAllRolePlayed;
	}
	
	public void addUnit(Unit unit) {
		units.add(unit);
	}

	public void removeUnit(Unit unit) {
		units.remove(unit);
	}

	public byte getTeamIndex() {
		return teamIndex;
	}

	public void setTeamIndex(byte teamIndex) {
		this.teamIndex = teamIndex;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	
	public boolean isAllUnitsDead() {
		for (Unit u: units) {
			if (!u.isDead())
				return false;
		}
		return true;
	}

	public void addScore(int v) {
		score += v;
	}

}
