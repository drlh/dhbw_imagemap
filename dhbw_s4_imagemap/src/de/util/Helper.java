package de.util;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Helper
{

	// CONSTANTS
	public static final int LF_CROSS = 1;
	public static final int LF_SYSTEM = 2;
	public static final int LF_MOTIF = 3;

	public Helper()
	{

	}

	public void changeLookAndFeel(int lf, JFrame f)
	{
		switch (lf) {
		case LF_CROSS:
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case LF_SYSTEM:
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case LF_MOTIF:
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		f.repaint();
	}
}
