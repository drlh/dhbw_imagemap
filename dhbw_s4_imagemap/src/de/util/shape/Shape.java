package de.util.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Random;

@SuppressWarnings("serial")
public abstract class Shape implements Serializable
{
	protected final int RECT = 1;
	protected final int CIRCLE = 2;
	protected int type = 0;

	protected Color color;
	protected Rectangle rect;
	protected String href;

	protected Shape()
	{

	}

	protected Shape(int x, int y, int width, int height)
	{
		color = generateColor();
		rect = new Rectangle(x, y, width, height);
		href = "";
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

	public Rectangle getShape()
	{
		return rect;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public void setShape(Rectangle r)
	{
		this.rect = r;
	}

	public String getHTML()
	{
		String s = "";
		if (type == RECT) {
			s = new String("<area shape=rect coords=\"" + rect.x + "," + rect.y
					+ "," + (int) (rect.x + rect.width) + ","
					+ (int) (rect.y + rect.height) + "\" href=\"" + href
					+ "\">\n");
		} else if (type == CIRCLE) {
			s = new String("<area shape=circle coords=\"" + rect.x + ","
					+ rect.y + "," + (int) (rect.x + rect.width) + ","
					+ (int) (rect.y + rect.height) + "\" href=\"" + href
					+ "\">\n");
		}
		return s;
	}
}
