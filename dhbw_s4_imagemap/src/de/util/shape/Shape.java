package de.util.shape;

import java.awt.Point;

public abstract class Shape 
{
	private Point start;
	private static int ID = 0;
	
	public Shape()
	{
		ID++;
	}
}
