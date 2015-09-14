package de.util.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Rect extends Shape {

	public Rect(int x, int y, int width, int height) {
		super(x, y, width, height);

	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(r.x, r.y, r.width, r.height);
	}


	@Override
	public boolean contains(Point p) {
		Rectangle2D e = new Rectangle2D.Double(r.x, r.y, r.width, r.height);
		return e.contains(p);
	}

}
