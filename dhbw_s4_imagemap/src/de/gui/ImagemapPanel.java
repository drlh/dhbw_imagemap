package de.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class ImagemapPanel extends JPanel implements MouseInputListener{
	
	ImgMap imgmap;
	JLabel statusbar;

	/**
	 * Create the panel.
	 * @param lblStatusbar 
	 */
	public ImagemapPanel(ImgMap im, JLabel lblStatusbar, Image img) 
	{
		this.imgmap = im;
		this.statusbar = lblStatusbar;
		setLayout(new BorderLayout(0, 0));
		this.addMouseMotionListener(this);
		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
				
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		Point p = e.getPoint();
		statusbar.setText("Mausposition:   X: " + p.getX() + ", Y: "+p.getY());
		
	}

	


	
	
}
