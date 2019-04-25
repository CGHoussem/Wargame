package com.pxcode.entities;

import com.pxcode.main.Renderer;

public interface GameObject {
	public void render(Renderer renderer);
	public void update();
}
