package zeichnenStart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

public abstract class Shape implements Serializable {
	protected Color color;
	protected Rectangle r;

	public Shape(Color c, int x, int y, int width, int height) {
		color = c;
		r = new Rectangle(x, y, width, height);
	}

	public void showInfoWin(ZeichenWin win) {
		InfoDialog info = new InfoDialog(win, "Shape-Info: " + getTitle(), false, this);
		info.setBounds(r.x, r.y, 250, 300);
		info.setVisible(true);
	}

	public abstract void draw(Graphics2D g);

	public abstract double calcArea();

	public abstract double calcUmfang();

	public abstract boolean contains(Point p);

	public abstract String getTitle();

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Rectangle getR() {
		return r;
	}

	public void setR(Rectangle r) {
		this.r = r;
	}
}
