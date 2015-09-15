package de.util.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Rect extends Shape {

	public Rect(int x, int y, int width, int height) {
		super(x, y, width, height);
		type = RECT;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}


	@Override
	public boolean contains(Point p) {
		Rectangle2D e = new Rectangle2D.Double(rect.x, rect.y, rect.width, rect.height);
		return e.contains(p);
	}

}
