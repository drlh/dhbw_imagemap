package de.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Helper
{

	// CONSTANTS
	public static final int LF_CROSS = 1;
	public static final int LF_SYSTEM = 2;
	public static final int LF_MOTIF = 3;

	public Helper()
	{

	}

	/**
	 * Ändert das Look and Feel des übergebenen Frames und zeichnet dieses neu
	 * Konstanten: LF_CROSS = 1, LF_SYSTEM = 2, LF_MOTIF = 3
	 * 
	 * @param lf
	 *            Variable für die Konstante
	 * @param f
	 */
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

	public void saveHtml(String html_map, JFrame f)
	{
		String html = "<html>" + "<head><title>Image Map</title></head>"
				+ "<body>" + html_map + "</body></html>";

		try {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Html (.html)", "html", "txt");
			fileChooser.setFileFilter(filter);
			fileChooser.addChoosableFileFilter(filter);
			fileChooser.setDialogTitle("Datei zum Speicher auswählen");
			
			int userSelection = fileChooser.showSaveDialog(f);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();

				FileWriter fw = new FileWriter(fileToSave);
				fw.write(html);
				fw.close();
				System.out.println("Datei gespeichert!");
			}

		} catch (IOException iox) {
			JOptionPane.showMessageDialog(null,
					"Datei konnte nicht gespeichert werden!",
					"Fehler beim Speichern!", JOptionPane.ERROR_MESSAGE);
			iox.printStackTrace();
		}
	}
}
