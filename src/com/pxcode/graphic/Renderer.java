package com.pxcode.graphic;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.pxcode.main.Game;

public class Renderer {
	private BufferedImage view;
	private int[] pixels;

	public Renderer(int width, int height) {
		view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int scaledWidth = (int) (width * Game.GAME_SCALE);
		int scaledHeight = (int) (height * Game.GAME_SCALE);
		view = toBufferedImage(view.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST));
		pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
	}

	private static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D bGr = bImage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bImage;
	}

	private static BufferedImage scaleImage(BufferedImage img, float scale) {
		// Create new (blank) image of required (scaled) size
		int scaledWidth = (int) (img.getWidth() * scale);
		int scaledHeight = (int) (img.getHeight() * scale);
		BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);

		// Paint scaled version of image to new image
		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		graphics2D.drawImage(img, 0, 0, scaledWidth, scaledHeight, null);

		// clean up
		graphics2D.dispose();

		return scaledImage;
	}

	public void render(Graphics2D graphics) {
		graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
	}

	public void renderText(final String text, int posX, int posY) {
		Game.instance.graphics.drawString(text, posX, posY);
	}

	public void renderRectangle(Rectangle rectangle, int color) {
		for (int y = 0; y < rectangle.getHeight(); y++) {
			for (int x = 0; x < rectangle.getWidth(); x++) {
				setPixel(color, (int) (x + rectangle.getX()), (int) (y + rectangle.getY()));
			}
		}
	}

	public void renderImage(BufferedImage image, int posX, int posY, int zoomX, int zoomY, int offX, int offY) {
		image = scaleImage(image, Game.GAME_SCALE);
		int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				for (int zoomYIndex = 0; zoomYIndex < zoomY; zoomYIndex++) {
					for (int zoomXIndex = 0; zoomXIndex < zoomX; zoomXIndex++) {
						setPixel(imagePixels[y * image.getWidth() + x], (x * zoomX) + posX + offX + zoomXIndex,
								(y * zoomY) + posY + offY + zoomYIndex);
					}
				}
			}
		}
	}

	public void renderImage(BufferedImage image, int posX, int posY, int offX, int offY) {
		renderImage(image, posX, posY, 1, 1, offX, offY);
	}

	public void renderImage(BufferedImage image, int posX, int posY) {
		renderImage(image, posX, posY, 1, 1, 0, 0);
	}

	private void setPixel(int pixel, int posX, int posY) {
		if (pixel != -0xFF01) {
			int index = posY * view.getWidth() + posX;
			if (index < pixels.length && index >= 0)
				pixels[index] = pixel;
		}
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
		renderImage(Game.instance.map.getBackground(), 0, 0, 0, Game.PLATFORM_Y_OFFSET);
	}
}
