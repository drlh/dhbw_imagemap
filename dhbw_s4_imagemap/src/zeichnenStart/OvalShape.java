package zeichnenStart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class OvalShape extends Shape {

	public OvalShape(Color c, int x, int y, int width, int height) {
		super(c, x, y, width, height);

	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillOval(r.x, r.y, r.width, r.height);
	}

	@Override
	public double calcArea() {

		return r.width * r.height * Math.PI / 4;
	}

	@Override
	public double calcUmfang() {
		double a = r.width / 2;
		double b = r.height / 2;

		double lambda = (a - b) / (a + b);
		double lambda3 = lambda * lambda * 3;

		return Math.PI * (a + b)
				* (1 + (lambda3 / (10 + Math.sqrt(4 - lambda3))));
	}

	@Override
	public boolean contains(Point startPoint) {
		Ellipse2D e = new Ellipse2D.Double(r.x, r.y, r.width, r.height);

		return e.contains(startPoint);
	}

	@Override
	public String getTitle() {

		return "Oval";
	}

}
