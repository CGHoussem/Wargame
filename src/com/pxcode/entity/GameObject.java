package com.pxcode.entity;

import com.pxcode.graphic.Renderer;

public interface GameObject {
	public void render(Renderer renderer);
	public void update();
}
