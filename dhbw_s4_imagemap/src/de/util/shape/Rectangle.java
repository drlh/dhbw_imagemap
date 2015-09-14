package de.util.shape;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shape
{

	private int width;
	private int height;

	
	public Rectangle(int x, int y)
	{
		super(x, y);
		this.width = 0;
		this.height = 0;

	}
	
	public Rectangle(int x, int y, int width, int height)
	{
		super(x, y);
		this.width = width;
		this.height = height;

	}

	@Override
	public void draw(Graphics2D g)
	{
		g.fillRect(start.x, start.y, width, height);
	}

	@Override
	public boolean contains(Point p)
	{
		Rectangle2D e = new Rectangle2D.Double(start.x, start.y, width, height);
		return e.contains(p);
	}

	@Override
	public Object getShape()
	{
		Rectangle2D e = new Rectangle2D.Double(start.x, start.y, width, height);
		return e;
	}

	public void setShape(Rectangle2D e)
	{
		start.setLocation(e.getCenterX(), e.getCenterY());
		height = (int) e.getHeight();
		width = (int) e.getWidth();
	}

	@Override
	public String getHTML()
	{
		String str = new String();
		str = new String("<area shape=rect coords=\"" + start.x + "," + start.y
				+ "," + (int) (start.x + width) + ","
				+ (int) (start.y + height));
		return str;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	@Override
	public void setEnd(int x, int y)
	{
		this.width = x - start.x;
		this.height = y - start.y;
	}
	
	

}
