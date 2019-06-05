package com.pxcode.main;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

public class SceneManager {
	List<Canvas> scenes;
	
	public SceneManager() {
		scenes = new ArrayList<>();
	}
	
	public void addScene(Canvas c) {
		scenes.add(c);
	}
	
	public Canvas getScene(int index) {
		return scenes.get(index);
	}
}
