package com.pxcode.entity;

import java.util.ArrayList;
import java.util.List;

import com.pxcode.entity.unit.Unit;

public abstract class Player {

	private byte teamIndex;
	private String username;
	private int score;
	private List<Unit> units;

	public Player(byte teamIndex, String username) {
		this.teamIndex = teamIndex;
		this.username = username;
		score = 0;
		units = new ArrayList<>();
	}

	public boolean isRoleOver() {
		boolean isAllRolePlayed = true;
		for (Unit u : getUnits()) {
			isAllRolePlayed &= (!u.isDead() && u.isRolePlayed()); 
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

	public void addScore(int v) {
		score += v;
	}

}
