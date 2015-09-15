package de.util.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Circle extends Shape {

	public Circle(int x, int y, int width, int height) {
		super(x, y, width, height);
		type = CIRCLE;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillOval(rect.x, rect.y, rect.width, rect.height);
	}


	@Override
	public boolean contains(Point startPoint) {
		Ellipse2D e = new Ellipse2D.Double(rect.x, rect.y, rect.width, rect.height);

		return e.contains(startPoint);
	}


}
