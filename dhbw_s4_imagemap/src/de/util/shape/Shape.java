package de.util.shape;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public abstract class Shape implements Serializable
{

	protected Point start;
	private String link;

	protected Shape(int x, int y)
	{
		start = new Point(x, y);
	}

	public abstract void draw(Graphics2D g);

	public abstract boolean contains(Point p);

	public abstract Object getShape();
	
	public abstract String getHTML();
	
	public abstract void setEnd(int x, int y);

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

}
