package de.util;

import java.awt.Point;
import java.util.*;

import de.util.shape.Shape;

public class ShapeList
{

	private Vector<Shape> list = null;
	private Shape selectedShape = null;

	public ShapeList()
	{
		list = new Vector<Shape>();
	}

	public ShapeList(String src)
	{
		list = new Vector<Shape>();
	}

	public void addShape(Shape s)
	{
		list.add(s);
	}

	public Shape getShape(int i)
	{
		return list.get(i);
	}

	public void deleteShape(Shape s)
	{
		list.remove(s);
	}

	public int size()
	{
		return list.size();
	}

	public void clear()
	{
		list.clear();
	}

	public boolean isPointInsideShape(int x, int y)
	{
		Point p = new Point(x, y);
		for (int i = size() - 1; i >= 0; i--) {
			if ( list.get(i).contains(p)) {
				selectedShape =  list.get(i);
				return true;
			}
		}
		return false;
	}

	public Shape getFoundShape()
	{
		return selectedShape;
	}

	public int getFoundShapeIndex()
	{
		return list.indexOf(selectedShape);
	}

	public void removeFoundShape()
	{
		if (selectedShape != null)
			list.remove(selectedShape);
	}

	/**
	 * List die Koordinaten der einzelnen Shapes aus und gibt HTML Code der img Map zurück
	 * @param imgSrc
	 * @param panelW
	 * @param panelH
	 * @return
	 */
	public String getHTML(String imgSrc, int panelW, int panelH)
	{
		String mapName = "MAP";
		
		String str = new String("<img src=\"" + imgSrc + "\" width=\"" + panelW
				+ "\" height=\"" + panelH + "\" usemap=\"#" + mapName
				+ "\" border=\"0\">\n<map name=\"" + mapName + "\">\n");
		
		for (int i = 0; i < size(); i++) {
			Shape s = list.get(i);
			str += s.getHTML();
		}
		str += "</map>";
		return str;
	}

	/**
	 * Leert die Liste der Shapes
	 */
	public void flush()
	{
		this.list.removeAllElements();
	}
}