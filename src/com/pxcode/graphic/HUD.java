
package com.pxcode.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import com.pxcode.entity.GameObject;
import com.pxcode.entity.unit.Stats;
import com.pxcode.entity.unit.Unit;
import com.pxcode.main.Game;

public class HUD implements GameObject {

	private Map<BufferedImage, Point> unitsElements;
	private Stats unitStats;
	private BufferedImage overlayImage;
	private BufferedImage blueWonImage;
	private BufferedImage redWonImage;
	private BufferedImage wonImage;
	private Unit enemyUnit;

	public HUD() {
		unitsElements = new HashMap<>();
		overlayImage = Game.loadImage("sprites/overlay.png");
		blueWonImage = Game.loadImage("sprites/indicators/blue_win.png");
		redWonImage = Game.loadImage("sprites/indicators/red_win.png");
	}

	public void setTeamWon(int teamIndex) {
		if (teamIndex == 0) {
			wonImage = blueWonImage;
		} else {
			wonImage = redWonImage;
		}
	}

	public void addUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.put(sprite, coord);
	}

	public void removeUnitElement(BufferedImage sprite, Point coord) {
		unitsElements.remove(sprite, coord);
	}

	public void updateUnitStats(Stats stats) {
		unitStats = stats;
	}

	public void addUnitStats(Stats stats) {
		unitStats = stats;
	}

	public void removeUnitStats() {
		unitStats = null;
	}

	public static void renderOutlinedText(Graphics2D g, String text, Color outline, Color fill, double x, double y) {
		AffineTransform oldAF = g.getTransform();
		g.translate(x, y);
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector glyphVector = Game.instance.getFont().createGlyphVector(frc, text);
		Shape textShape = glyphVector.getOutline();
		g.setColor(outline);
		g.draw(textShape);
		g.setColor(fill);
		g.fill(textShape);
		g.setTransform(oldAF);
	}

	private void renderUnitStats(Graphics2D g) {
		if (unitStats != null) {
			String health = String.valueOf(unitStats.getHealth());
			String ad = String.valueOf(unitStats.getAttackDamage());
			String ar = String.valueOf(unitStats.getArmor());
			String mr = String.valueOf(unitStats.getMovementRange());
			String mvtr = String.valueOf(unitStats.getAttackRange());

			renderOutlinedText(g, health, Color.BLACK, new Color(0x4dbf4c), Unit.focusedUnit.getX() + 50,
					Unit.focusedUnit.getY() + 40);

			double x1 = Math.ceil(81 * Game.GAME_SCALE);
			double x2 = Math.ceil(189 * Game.GAME_SCALE);
			double y1 = Math.ceil(765 * Game.GAME_SCALE);
			double y2 = Math.ceil(820 * Game.GAME_SCALE);

			renderOutlinedText(g, ad, Color.BLACK, Color.WHITE, x1, y1);
			renderOutlinedText(g, ar, Color.BLACK, Color.WHITE, x2, y1);
			renderOutlinedText(g, mr, Color.BLACK, Color.WHITE, x1, y2);
			renderOutlinedText(g, mvtr, Color.BLACK, Color.WHITE, x2, y2);
		}
	}

	public void setEnemyUnit(Unit unit) {
		enemyUnit = unit;
	}

	public void unsetEnemyUnit() {
		enemyUnit = null;
	}

	private void renderEnemyUnitHealth() {
		if (enemyUnit != null) {
			String health = String.valueOf(enemyUnit.getStats().getHealth());
			renderOutlinedText(Game.instance.graphics, health, Color.BLACK, Color.RED, enemyUnit.getX() + 50,
					enemyUnit.getY() + 40);
		}
	}

	public void renderTexts(Graphics2D g) {
		renderUnitStats(g);
		renderEnemyUnitHealth();

		String teamText = "";
		int teamScore = 0;
		Color teamColor = null;
		if (Game.instance.currentTeamPlaying == 0) {
			teamText = "TEAM BLUE";
			teamColor = new Color(100, 74, 166);
		} else {
			teamText = "TEAM RED";
			teamColor = new Color(196, 16, 18);
		}

		teamScore = Game.instance.players[Game.instance.currentTeamPlaying].getScore();
		renderOutlinedText(g, teamText, Color.BLACK, teamColor, Math.ceil(759 * Game.GAME_SCALE),
				Math.ceil(757 * Game.GAME_SCALE));
		renderOutlinedText(g, String.valueOf(teamScore), Color.BLACK, Color.WHITE, Math.ceil(848 * Game.GAME_SCALE),
				Math.ceil(782 * Game.GAME_SCALE));
		renderOutlinedText(g, countdownToTime(Game.instance.countdownTimer), Color.BLACK, Color.WHITE,
				Math.ceil(960 * Game.GAME_SCALE), Math.ceil(795 * Game.GAME_SCALE));
		unsetEnemyUnit();
	}

	private String countdownToTime(int countdown) {
		int minutes = countdown / 60;
		int seconds = countdown % 60;
		StringBuffer minStr = new StringBuffer();
		StringBuffer secStr = new StringBuffer();
		if (minutes >= 10)
			minStr.append(minutes);
		else
			minStr.append("0").append(minutes);
		if (seconds < 10)
			secStr.append(seconds).append("0");
		else
			secStr.append(seconds);
		return minStr + ":" + secStr;
	}

	private void renderCurrentTeamInfo(Renderer renderer) {
		BufferedImage teamFlag = null;

		if (Game.instance.currentTeamPlaying == 0) {
			teamFlag = Game.loadImage("sprites/indicators/blue_flag.png");
		} else {
			teamFlag = Game.loadImage("sprites/indicators/red_flag.png");
		}

		renderer.renderImage(teamFlag, (int) Math.ceil(705 * Game.GAME_SCALE), (int) Math.ceil(712 * Game.GAME_SCALE));
	}

	@Override
	public void render(Renderer renderer) {
		renderer.renderImage(overlayImage, 0, 0);
		try {
			unitsElements.forEach((sprite, point) -> {
				renderer.renderImage(sprite, (int) point.getX(), (int) point.getY(), 0, Game.PLATFORM_Y_OFFSET);
			});
		} catch (ConcurrentModificationException e) {
			System.out.println(e.getMessage());
		}
		renderCurrentTeamInfo(renderer);
		if (wonImage != null) {
			renderer.renderImage(wonImage, 0, 0);
		}
	}


	@Override
	public void update() {
	}

}
