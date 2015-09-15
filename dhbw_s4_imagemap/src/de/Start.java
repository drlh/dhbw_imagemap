package de;

import java.awt.EventQueue;

import de.gui.ImgMap;

public class Start
{

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try {
					ImgMap frame = new ImgMap();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
