package com.pxcode.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.pxcode.entity.Map;
import com.pxcode.utility.GameState;
import com.pxcode.utility.UnitType;

public class KeyHandler implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_S:
			Game.instance.map.saveMap();
			break;
		case KeyEvent.VK_L:
			Game.instance.map = new Map("map"+String.valueOf(Game.instance.map.getIndex()));
			break;
		case KeyEvent.VK_D:
			Game.isDebug = !Game.isDebug;
			break;
		case KeyEvent.VK_N:
			Game.unitToBeSpawn = UnitType.NASHOR;
			break;
		case KeyEvent.VK_G:
			Game.unitToBeSpawn = UnitType.GRAVES;
			break;
		case KeyEvent.VK_K:
			Game.unitToBeSpawn = UnitType.KAYLE;
			break;
		case KeyEvent.VK_I:
			Game.unitToBeSpawn = UnitType.SION;
			break;
		case KeyEvent.VK_ENTER:
			Game.state = GameState.PLAYING;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
