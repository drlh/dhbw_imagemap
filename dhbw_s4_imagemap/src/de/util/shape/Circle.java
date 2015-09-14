package de.util.shape;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Circle extends Shape
{
	private int radius;

	public Circle(int x, int y)
	{
		super(x, y);
		radius = 0;
	}

	public Circle(int x, int y, int r)
	{
		super(x, y);
		radius = r;
	}

	@Override
	public void draw(Graphics2D g)
	{
		int x = start.x - (radius / 2);
		int y = start.y - (radius / 2);
		g.fillOval(x, y, radius, radius);
	}

	@Override
	public boolean contains(Point startPoint)
	{
		Ellipse2D e = new Ellipse2D.Double(start.x, start.y, radius, radius);
		return e.contains(startPoint);
	}

	@Override
	public Object getShape()
	{
		Ellipse2D e = new Ellipse2D.Double(start.x, start.y, radius, radius);
		return e;
	}

	public void setShape(Ellipse2D e)
	{
		start.setLocation(e.getCenterX(), e.getCenterY());
		radius = (int) e.getHeight();
	}

	@Override
	public String getHTML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEnd(int x, int y)
	{
		this.radius = (x/2) - start.x;
	}

}
