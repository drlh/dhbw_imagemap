package de.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import de.util.ShapeList;
import de.util.shape.Circle;
import de.util.shape.Rectangle;
import de.util.shape.Shape;

@SuppressWarnings("serial")
public class ImagemapPanel extends JPanel implements MouseListener, MouseMotionListener
{

	private ImgMap imgmap;
	private Image img;
	private ShapeList shapeList;
	private Shape currentShape;

	/**
	 * Create the panel.
	 * 
	 * @param lblStatusbar
	 * @param img
	 * @param shapeList
	 */
	public ImagemapPanel(ImgMap im, Image img, ShapeList shapeList)
	{
		this.imgmap = im;
		this.img = img;
		this.shapeList = shapeList;
		this.setSize(img.getWidth(null), img.getHeight(null));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.setVisible(true);

	}

	public void setImage(Image img)
	{
		this.img = img;
		this.setSize(img.getWidth(null), img.getHeight(null));
		revalidate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (img != null) {
			g2.drawImage(img, 0, 0, null);
		}
		if (shapeList.size() > 0) {
			for (int i = 0; i < shapeList.size(); i++) {
				shapeList.getShape(i).draw(g2);
			}
		}
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("clicked");
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		System.out.println("pressed");
		Point p = e.getPoint();
		if (imgmap.tool == imgmap.RECTANGLE) 
		{
			currentShape = new Rectangle(p.x, p.y);
		} else if (imgmap.tool == imgmap.CIRCLE) 
		{
			currentShape = new Circle(p.x, p.y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		System.out.println("released");
		Point p = e.getPoint();
		if (imgmap.tool == imgmap.RECTANGLE || imgmap.tool == imgmap.CIRCLE) 
		{
			currentShape.setEnd(p.x, p.y);
			this.shapeList.addShape(currentShape);
		} 
		currentShape = null;
		for (int i = 0; i < shapeList.size(); i++) {
			System.out.println(shapeList.getShape(i).getShape().toString());
		}

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
//		System.out.println("moved");
//		Point p = e.getPoint();
//		imgmap.setStatusbarText("X: " + p.getX() + ", Y: " + p.getY());
	}

}
