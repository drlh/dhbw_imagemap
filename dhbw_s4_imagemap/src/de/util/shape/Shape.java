package de.util.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Random;

import zeichnenStart.InfoDialog;
import zeichnenStart.ZeichenWin;

public abstract class Shape implements Serializable {
	protected Color color;
	protected Rectangle r;

	public Shape(int x, int y, int width, int height) {
		color = generateColor();
		r = new Rectangle(x, y, width, height);
	}


	private Color generateColor()
	{
		int r = new Random().nextInt(256);
		int g = new Random().nextInt(256);
		int b = new Random().nextInt(256);
		return new Color(r, g, b);
	}


	public abstract void draw(Graphics2D g);

	public abstract boolean contains(Point p);

	public Rectangle getR() {
		return r;
	}

	public void setR(Rectangle r) {
		this.r = r;
	}
}
