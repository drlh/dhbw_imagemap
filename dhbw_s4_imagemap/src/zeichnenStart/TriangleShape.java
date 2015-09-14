package zeichnenStart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;

public class TriangleShape extends Shape {

	public TriangleShape(Color c, int x, int y, int width, int height) {
		super(c, x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		Polygon p = getPoly();
		g.fillPolygon(p);
	}

	private Polygon getPoly() {
		Polygon p = new Polygon();
		p.addPoint(r.x + r.width / 2, r.y);
		p.addPoint(r.x, r.y + r.height);
		p.addPoint(r.x + r.width, r.y + r.height);
		return p;
	}

	@Override
	public double calcArea() {
		return ((r.width * r.height) / 2);
	}

	@Override
	public double calcUmfang() {
		double s = Math.sqrt((r.width * r.width) / 4 + r.height * r.height);
		return 2 * s + r.width;
	}

	@Override
	public boolean contains(Point p) {

		return getPoly().contains(p);
	}

	@Override
	public String getTitle() {
		return "Dreieck";
	}

}
