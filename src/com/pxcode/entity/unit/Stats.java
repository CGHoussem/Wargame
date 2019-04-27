package com.pxcode.entity.unit;

import java.io.Serializable;

public class Stats implements Serializable {
	private int baseHealthPoints;
	private int baseArmor;
	private int baseAttackDamage;
	private int baseMovementRange;
	private int baseAttackRange;

	private int health;
	private int armor;
	private int attackDamage;
	private int movementRange;
	private int attackRange;

	public Stats(int baseHealthPoints, int baseArmor, int baseAttackDamage, int baseMovementRange,
			int baseAttackRange) {
		super();
		this.baseHealthPoints = health = baseHealthPoints;
		this.baseArmor = armor = baseArmor;
		this.baseAttackDamage = attackDamage = baseAttackDamage;
		this.baseMovementRange = movementRange = baseMovementRange;
		this.baseAttackRange = attackRange = baseAttackRange;
	}

	public int getBaseHealthPoints() {
		return baseHealthPoints;
	}

	public void setBaseHealthPoints(int baseHealthPoints) {
		this.baseHealthPoints = baseHealthPoints;
	}

	public int getBaseArmor() {
		return baseArmor;
	}

	public void setBaseArmor(int baseArmor) {
		this.baseArmor = baseArmor;
	}

	public int getBaseAttackDamage() {
		return baseAttackDamage;
	}

	public void setBaseAttackDamage(int baseAttackDamage) {
		this.baseAttackDamage = baseAttackDamage;
	}

	public int getBaseMovementRange() {
		return baseMovementRange;
	}

	public void setBaseMovementRange(int baseMovementRange) {
		this.baseMovementRange = baseMovementRange;
	}

	public int getBaseAttackRange() {
		return baseAttackRange;
	}

	public void setBaseAttackRange(int baseAttackRange) {
		this.baseAttackRange = baseAttackRange;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getMovementRange() {
		return movementRange;
	}

	public void setMovementRange(int movementRange) {
		this.movementRange = movementRange;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

}
