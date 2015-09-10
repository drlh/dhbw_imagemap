package de;

/*
ImageMap Client-side imagemap creator
Copyright (C) 2001-2003  Andreas Tetzl
www.tetzl.de
andreas@tetzl.de


This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.geom.*;
import javax.swing.border.*;


/**
 * Title:        ImageMap
 * Description:  client side imagemap creator
 * Copyright:    Copyright (c) 2001
 * Company:      webdesign-pirna.de
 * @author Andreas Tetzl
 * @version 1.0
 *
 */

public class Imagemap extends JFrame implements ActionListener{
  String imageSrc = "";
  Image image;

  ShapeList shapeList = new ShapeList();
  ImagemapPanel imagemapPanel;

  BufferedImage emptyImage = new BufferedImage(600, 500, BufferedImage.TYPE_INT_RGB);

  JPopupMenu popup;
  JMenuItem menuItem_title, menuItem_rempoint, menuItem_remobject, menuItem_edithref, menuItem_editalt, menuItem_convert;

  JToolBar jToolBar1 = new JToolBar();
  JButton jButton_rectangle = new JButton();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JScrollPane jScrollPane_image;
  JScrollPane jScrollPane_html = new JScrollPane();
  JButton jButton_circle = new JButton();
  JButton jButton_polygon = new JButton();
  JButton jButton_parsehtml = new JButton();
  JButton jButton_openimage = new JButton();

  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenu_file = new JMenu();
  JMenuItem jMenuItem_new = new JMenuItem();
  JMenuItem jMenuItem_openimage = new JMenuItem();
  JMenuItem jMenuItem_parsehtml = new JMenuItem();
  JMenuItem jMenuItem_exit = new JMenuItem();
  JMenu jMenu_help = new JMenu();
  JMenuItem jMenuItem_about = new JMenuItem();
  Component component1;
  Component component2;
  JButton jButton_about = new JButton();
  JButton jButton_new = new JButton();
  BorderLayout borderLayout1 = new BorderLayout();
  JToolBar jToolBar3 = new JToolBar();
  JLabel statusBar = new JLabel();
  JMenu jMenu1 = new JMenu();
  JCheckBoxMenuItem jCheckBoxMenuItem_snap = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jCheckBoxMenuItem_antialias = new JCheckBoxMenuItem();
  JPanel jPanel_html = new JPanel();
  JButton jButton_savehtml = new JButton();
  JButton jButton_copyhtml = new JButton();
  JToolBar jToolBar2 = new JToolBar();
  BorderLayout borderLayout3 = new BorderLayout();
  JTextArea jTextArea_html = new JTextArea();
  JButton jButton_addpoint = new JButton();
  JButton jButton_delpoint = new JButton();
  TitledBorder titledBorder1;
  JButton jButton_help = new JButton();
  JMenuItem jMenuItem_help = new JMenuItem();
  JMenu jMenu_zoom = new JMenu();
  JRadioButtonMenuItem jMenuItem_zoom1 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem jMenuItem_zoom2 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem jMenuItem_zoom4 = new JRadioButtonMenuItem();
  JRadioButtonMenuItem jMenuItem_zoom8 = new JRadioButtonMenuItem();
  ButtonGroup zoomButtonGroup = new ButtonGroup();

  Rule hRule=null;
  Rule vRule=null;

  /**Construct */
  public Imagemap() {
    //Image si = new ImageIcon(Imagemap.class.getResource("images/splash.jpg")).getImage();
    //Splash splash = new Splash(this, si);

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**Component initialization*/
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    setIconImage(Toolkit.getDefaultToolkit().createImage(Imagemap.class.getResource("images/frameicon.gif")));

    component1 = Box.createHorizontalStrut(8);
    component2 = Box.createHorizontalStrut(8);

    this.setSize(new Dimension(600,500));
    this.setTitle("ImageMap 1.0");

    Graphics2D g=emptyImage.createGraphics();
/*    GradientPaint gp = new GradientPaint(50.0f, 50.0f, new Color(0xAAAAAA),
                        550.0f, 350.0f, new Color(0xCCCCCC));
    g.setPaint(gp);
    g.fillRect(0, 0, 600, 400);
        float dash1[] = {3.0f};
             BasicStroke bs = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                      BasicStroke.JOIN_MITER, 3.0f, dash1, 0.0f);
             g.setStroke(bs);
             Line2D line = new Line2D.Float(20.0f, 10.0f, 300.0f, 10.0f);
             g.setColor(Color.black);
             g.draw(line);
*/
    g.setColor(Color.gray);
    g.fillRect(0, 0, 600, 500);
    image=emptyImage;
    imagemapPanel = new ImagemapPanel(image, shapeList, statusBar, jCheckBoxMenuItem_snap, jCheckBoxMenuItem_antialias, this);
    jScrollPane_image = new JScrollPane(imagemapPanel);

    jCheckBoxMenuItem_snap.setSelected(true);
    jCheckBoxMenuItem_antialias.setSelected(true);



    jMenu_file.setText("File");
    jMenuItem_new.setText("New");
    jMenuItem_new.setAccelerator(javax.swing.KeyStroke.getKeyStroke(78, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_new.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_new_actionPerformed(e);
      }
    });
    jMenuItem_openimage.setText("Open Image ...");
    jMenuItem_openimage.setAccelerator(javax.swing.KeyStroke.getKeyStroke(79, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_openimage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_openimage_actionPerformed(e);
      }
    });
    jMenuItem_parsehtml.setText("Read map from HTML ...");
    jMenuItem_parsehtml.setAccelerator(javax.swing.KeyStroke.getKeyStroke(72, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_parsehtml.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_parsehtml_actionPerformed(e);
      }
    });
    jMenuItem_exit.setText("Exit");
    jMenuItem_exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(88, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_exit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_exit_actionPerformed(e);
      }
    });
    jMenu_help.setText("Help");
    jMenuItem_about.setText("About Imagemap");
    jMenuItem_about.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_about_actionPerformed(e);
      }
    });
    jButton_about.setToolTipText("About ImageMap");
    jButton_about.setIcon(new ImageIcon(Imagemap.class.getResource("images/about.gif")));
    jButton_about.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_about_actionPerformed(e);
      }
    });
    jButton_new.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_new_actionPerformed(e);
      }
    });
    jScrollPane_image.setDoubleBuffered(true);
    jButton_new.setToolTipText("Clear Imagemap and Image");
    jButton_new.setIcon(new ImageIcon(Imagemap.class.getResource("images/new.gif")));
    jButton_new.setMnemonic('N');
    jButton_openimage.setToolTipText("Load Image File");
    jButton_openimage.setIcon(new ImageIcon(Imagemap.class.getResource("images/image.gif")));
    jButton_openimage.setMnemonic('O');
    jButton_parsehtml.setToolTipText("Parse HTML File for Imagemap");
    jButton_parsehtml.setIcon(new ImageIcon(Imagemap.class.getResource("images/html.gif")));
    jButton_parsehtml.setMnemonic('H');
    jButton_rectangle.setToolTipText("Draw Rectangular Shape");
    jButton_rectangle.setIcon(new ImageIcon(Imagemap.class.getResource("images/rect.gif")));
    jButton_rectangle.setMnemonic('R');
    jButton_circle.setToolTipText("Draw Circle Shape");
    jButton_circle.setIcon(new ImageIcon(Imagemap.class.getResource("images/circle.gif")));
    jButton_circle.setMnemonic('C');
    jButton_polygon.setToolTipText("Draw Polygon Shape");
    jButton_polygon.setIcon(new ImageIcon(Imagemap.class.getResource("images/poly.gif")));
    jButton_polygon.setMnemonic('P');
    statusBar.setText(" ");
    jMenu1.setText("Options");
    jCheckBoxMenuItem_snap.setText("snap to existing points");
    jCheckBoxMenuItem_antialias.setText("anti alias lines");
    jButton_savehtml.setToolTipText("Save HTML Code to File");
    jButton_savehtml.setIcon(new ImageIcon(Imagemap.class.getResource("images/save.gif")));
    jButton_savehtml.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_savehtml_actionPerformed(e);
      }
    });
    jButton_copyhtml.setToolTipText("Copy HTML Code to Clipboard");
    jButton_copyhtml.setIcon(new ImageIcon(Imagemap.class.getResource("images/copy.gif")));
    jButton_copyhtml.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_copyhtml_actionPerformed(e);
      }
    });
    jPanel_html.setLayout(borderLayout3);
    jButton_addpoint.setToolTipText("Add Point to Polygon");
    jButton_addpoint.setIcon(new ImageIcon(Imagemap.class.getResource("images/addpoint.gif")));
    jButton_addpoint.setMnemonic('A');
    jButton_addpoint.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_addpoint_actionPerformed(e);
      }
    });
    jButton_delpoint.setToolTipText("Remove Point from Polygon");
    jButton_delpoint.setIcon(new ImageIcon(Imagemap.class.getResource("images/delpoint.gif")));
    jButton_delpoint.setMnemonic('D');
    jButton_delpoint.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_delpoint_actionPerformed(e);
      }
    });
    jButton_help.setIcon(new ImageIcon(Imagemap.class.getResource("images/help.gif")));
    jButton_help.setToolTipText("Help");
    jButton_help.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_help_actionPerformed(e);
      }
    });
    jMenuItem_help.setText("Help");
    jMenuItem_help.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_help_actionPerformed(e);
      }
    });

    jMenu_zoom.setText("Zoom");
    jMenuItem_zoom1.setText("Zoom Factor 1");
    jMenuItem_zoom1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(49, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_zoom1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_zoom_actionPerformed(e);
      }
    });
    jMenuItem_zoom2.setText("Zoom Factor 2");
    jMenuItem_zoom2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(50, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_zoom2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_zoom_actionPerformed(e);
      }
    });
    jMenuItem_zoom4.setText("Zoom Factor 4");
    jMenuItem_zoom4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(52, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_zoom4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_zoom_actionPerformed(e);
      }
    });
    jMenuItem_zoom8.setText("Zoom Factor 8");
    jMenuItem_zoom8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(56, java.awt.event.KeyEvent.CTRL_MASK, false));
    jMenuItem_zoom8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem_zoom_actionPerformed(e);
      }
    });
    jMenuItem_zoom1.setSelected(true);
    zoomButtonGroup.add(jMenuItem_zoom1);
    zoomButtonGroup.add(jMenuItem_zoom2);
    zoomButtonGroup.add(jMenuItem_zoom4);
    zoomButtonGroup.add(jMenuItem_zoom8);


    jMenuBar1.add(jMenu_file);
    jMenuBar1.add(jMenu1);
    jMenuBar1.add(jMenu_zoom);
    jMenuBar1.add(jMenu_help);
    this.setJMenuBar(jMenuBar1);

 //Create the popup menu.
    popup = new JPopupMenu();
    menuItem_remobject = new JMenuItem("Remove Shape");
    menuItem_remobject.addActionListener(this);
    popup.add(menuItem_remobject);
    menuItem_rempoint = new JMenuItem("Remove Point");
    menuItem_rempoint.addActionListener(this);
    popup.add(menuItem_rempoint);
    menuItem_edithref = new JMenuItem("Edit HREF");
    menuItem_edithref.addActionListener(this);
    popup.add(menuItem_edithref);
    menuItem_editalt = new JMenuItem("Edit ALT");
    menuItem_editalt.addActionListener(this);
    popup.add(menuItem_editalt);
    menuItem_convert = new JMenuItem("Convert to Polygon");
    menuItem_convert.addActionListener(this);
    popup.add(menuItem_convert);

    jScrollPane_image.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jScrollPane_image_componentShown(e);
      }
    });
    jButton_parsehtml.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_parsehtml_actionPerformed(e);
      }
    });
    jButton_openimage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_openimage_actionPerformed(e);
      }
    });
    jScrollPane_image.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    jScrollPane_image.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    hRule = new Rule(Rule.HORIZONTAL, imagemapPanel);
    vRule = new Rule(Rule.VERTICAL, imagemapPanel);
    hRule.setPreferredWidth(10);
    vRule.setPreferredHeight(10);
    jScrollPane_image.setColumnHeaderView(hRule);
    jScrollPane_image.setRowHeaderView(vRule);

    //Add listener to components that can bring up popup menus.
    imagemapPanel.addMouseListener(new PopupListener());


    jButton_rectangle.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_rectangle_actionPerformed(e);
      }
    });
    jPanel_html.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jPanel_html_componentShown(e);
      }
    });
    jButton_circle.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_circle_actionPerformed(e);
      }
    });
    jButton_polygon.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_polygon_actionPerformed(e);
      }
    });

    jPanel_html.add(jToolBar2, BorderLayout.NORTH);
    jPanel_html.add(jScrollPane_html, BorderLayout.CENTER);
    this.getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
    jTabbedPane1.add(jScrollPane_image, "Image");
    jTabbedPane1.add(jPanel_html, "HTML");
    jScrollPane_html.getViewport().add(jTextArea_html, null);
    jToolBar2.add(jButton_savehtml, null);
    jToolBar2.add(jButton_copyhtml, null);
    this.getContentPane().add(jToolBar1, BorderLayout.NORTH);
    jToolBar1.add(jButton_new, null);
    jToolBar1.add(jButton_openimage, null);
    jToolBar1.add(jButton_parsehtml, null);
    jToolBar1.add(component1, null);
    jToolBar1.add(jButton_rectangle, null);
    jToolBar1.add(jButton_circle, null);
    jToolBar1.add(jButton_polygon, null);
    jToolBar1.add(jButton_addpoint, null);
    jToolBar1.add(jButton_delpoint, null);
    jToolBar1.add(component2, null);
    jToolBar1.add(jButton_about, null);
    jToolBar1.add(jButton_help, null);
    this.getContentPane().add(jToolBar3, BorderLayout.SOUTH);
    jToolBar3.add(statusBar, null);
    jMenu_file.add(jMenuItem_new);
    jMenu_file.add(jMenuItem_openimage);
    jMenu_file.add(jMenuItem_parsehtml);
    jMenu_file.addSeparator();
    jMenu_file.add(jMenuItem_exit);
    jMenu_help.add(jMenuItem_about);
    jMenu_help.add(jMenuItem_help);
    jMenu1.add(jCheckBoxMenuItem_snap);
    jMenu1.add(jCheckBoxMenuItem_antialias);
    jMenu_zoom.add(jMenuItem_zoom1);
    jMenu_zoom.add(jMenuItem_zoom2);
    jMenu_zoom.add(jMenuItem_zoom4);
    jMenu_zoom.add(jMenuItem_zoom8);


  }

  /**Main method*/
  public static void main(String[] args) {

    Imagemap frame = new Imagemap();

    //EXIT_ON_CLOSE == 3
    frame.setDefaultCloseOperation(3);

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
  }

  //static initializer for setting look & feel
  static {
    try {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch(Exception e) {
    }
  }

  void jButton_rectangle_actionPerformed(ActionEvent e) {
    imagemapPanel.set_action(ImagemapPanel.ACTION_RECT);
  }
  void jButton_circle_actionPerformed(ActionEvent e) {
    imagemapPanel.set_action(ImagemapPanel.ACTION_CIRCLE);
  }
  void jButton_polygon_actionPerformed(ActionEvent e) {
    imagemapPanel.set_action(ImagemapPanel.ACTION_POLY);
  }
  void jButton_addpoint_actionPerformed(ActionEvent e) {
    imagemapPanel.set_action(ImagemapPanel.ACTION_ADDPOINT);
  }
  void jButton_delpoint_actionPerformed(ActionEvent e) {
    imagemapPanel.set_action(ImagemapPanel.ACTION_DELPOINT);
  }
  void jButton_openimage_actionPerformed(ActionEvent e) {
    jMenuItem_openimage_actionPerformed(e);
  }
  void jMenuItem_about_actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(this, "ImageMap v1.0\nCopyright (c) 2001-2003 Andreas Tetzl\nE-Mail: andreas@tetzl.de\nwww.tetzl.de\n\nThis program is Freeware.", "About", JOptionPane.PLAIN_MESSAGE);
  }
  void jButton_about_actionPerformed(ActionEvent e) {
    jMenuItem_about_actionPerformed(e);
  }
  void jButton_new_actionPerformed(ActionEvent e) {
    jMenuItem_new_actionPerformed(e);
  }
  void jMenuItem_exit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }
  void jButton_help_actionPerformed(ActionEvent e) {
    jMenuItem_help_actionPerformed(e);
  }
  void jMenuItem_help_actionPerformed(ActionEvent e) {
    HelpFrame hf = new HelpFrame();
  }


  public void actionPerformed(ActionEvent e) {   // popup menu
    JMenuItem source = (JMenuItem)(e.getSource());
    if (source==menuItem_remobject) {
      shapeList.removeFoundShape();
      imagemapPanel.repaint();
    } else if (source==menuItem_rempoint) {
      shapeList.getFoundShape().remove_polypoint(shapeList.getFoundKeyPoint());
      imagemapPanel.repaint();
    } else if (source==menuItem_convert) {
      Shape s=shapeList.getFoundShape();
      if (s.get_type()==Shape.TYPE_CIRCLE) {
        String str=JOptionPane.showInputDialog(this, "Enter number of circle segments in new polygon");
        int n=new Integer(str).intValue();
        s.convert(n);
      } else
        s.convert();
      imagemapPanel.repaint();
    } else if (source==menuItem_edithref) {
/*      jTextArea_html.setText(shapeList.get_html(imageSrc, image.getWidth(null), image.getHeight(null)));
      String str=jTextArea_html.getText();
      int j=0;
      for (int i=0; i<shapeList.getFoundShapeIndex(); i++) {
        j=str.indexOf("href=", j);
      }
      j+=6;
      System.out.println(j);
      jTabbedPane1.setSelectedIndex(1);
      jTextArea_html.grabFocus();

      jTextArea_html.setCaretPosition(30);
      jTextArea_html.moveCaretPosition(31);
*/
      Shape s=shapeList.getFoundShape();
      s.set_href((String)JOptionPane.showInputDialog(this, "Enter HREF argument for this " + s.get_typeString(), "ImageMap",
                                                     JOptionPane.QUESTION_MESSAGE, null, null, s.get_href()));
    } else if (source==menuItem_editalt) {
      Shape s=shapeList.getFoundShape();
      s.set_alt((String)JOptionPane.showInputDialog(this, "Enter ALT argument for this " + s.get_typeString(), "ImageMap",
                                                    JOptionPane.QUESTION_MESSAGE, null, null, s.get_alt()));
    }
  }


  class PopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
    public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
    private void maybeShowPopup(MouseEvent e) {
      int x=e.getX() / imagemapPanel.zoomFactor;
      int y=e.getY() / imagemapPanel.zoomFactor;
      if (e.isPopupTrigger() && (shapeList.isPointInsideShape(x, y) || shapeList.isShapePointAt(x, y))) {
        menuItem_rempoint.setEnabled(shapeList.getFoundKeyPoint()>0);  // poly point
        menuItem_convert.setEnabled(shapeList.getFoundShape().get_type()==Shape.TYPE_CIRCLE ||
                                    shapeList.getFoundShape().get_type()==Shape.TYPE_RECT);

        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }


  public boolean loadImage(String fname) {
    if (fname.length()<2) return false;
    imageSrc=fname;
    if (imageSrc.startsWith("http://"))
        try {
            image = getToolkit().getImage(new URL(imageSrc));
        } catch (Exception ex) {}
    else
        image = getToolkit().getImage(imageSrc);
    MediaTracker tracker = new MediaTracker(this);
    tracker.addImage(image, 0);
    try {
      tracker.waitForID(0);
    } catch (InterruptedException ie) {
      return false;
    }
    if (tracker.isErrorAny()) return false;
    return true;
  }

  void jScrollPane_image_componentShown(ComponentEvent e) {
    if (jTextArea_html.getText().length()<10) return;
    HTMLParser hp = new HTMLParser(jTextArea_html.getText());
    hp.createShapeList(shapeList);

    shapeList.mapName=hp.getMapName();

    if (!imageSrc.equals(hp.getImageSrc())) {
      imageSrc=hp.getImageSrc();
      if (!loadImage(imageSrc))
        image=emptyImage;
      imagemapPanel.setImage(image);
      jMenuItem_zoom1.setSelected(true);
      imagemapPanel.repaint();
    }
    statusBar.setText(" ");
  }

  void jPanel_html_componentShown(ComponentEvent e) {
    jTextArea_html.setText(shapeList.get_html(imageSrc, image.getWidth(null), image.getHeight(null)));
    statusBar.setText(" ");
  }

    void jMenuItem_parsehtml_actionPerformed(ActionEvent e) {
        File file = null;

        JFileChooser chooser = new JFileChooser(".");
        chooser.setDialogTitle("Parse HTML file for imagemap");
        //chooser.setLocale(currentLocale);
        if (chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (f.exists()) {
                file=f;
            } else {
                statusBar.setText("file does not exist");
                return;
            }
        } else return;  // FileChooser Cancel

        parseHtml(file);
    }
    void jButton_parsehtml_actionPerformed(ActionEvent e) {
        jMenuItem_parsehtml_actionPerformed(e);
    }


    void parseHtml(File file) {
        try {
            String path=file.getParent();
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str=new String();
            int i;
            try {
                while ((i=br.read())!=-1)
                    str+=new Character((char)i).toString();
                br.close();
            } catch (IOException ex) {
                statusBar.setText("could not read from file");
                return;
            }

            HTMLParser hp = new HTMLParser(str);
            hp.createShapeList(shapeList);
            if (shapeList.size()==0) statusBar.setText("no imagemap found");
            imageSrc=path + "/" + hp.getImageSrc();
            if (!loadImage(imageSrc))
                image=emptyImage;

            imagemapPanel.setImage(image);
            jMenuItem_zoom1.setSelected(true);
            jTextArea_html.setText(shapeList.get_html(imageSrc, image.getWidth(null), image.getHeight(null)));
        } catch(Exception ex) {
            statusBar.setText("could not open file");
        }
    }



  void jMenuItem_openimage_actionPerformed(ActionEvent e) {
    String fname=null;
    JFileChooser chooser = new JFileChooser(".");
    chooser.setDialogTitle("Open Image");
    if (chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      if (f.exists()) {
        fname=f.toString();
      } else
        statusBar.setText("file does not exist");
    }

    if (fname!=null) {
        openImage(fname);
    }
  }

  void openImage(String fname) {
      imageSrc=fname;
      if (!loadImage(imageSrc)) {
        image=emptyImage;
        statusBar.setText("could not load image");
      }
      imagemapPanel.setImage(image);
      jTextArea_html.setText(shapeList.get_html(imageSrc, image.getWidth(null), image.getHeight(null)));
  }

  void jMenuItem_new_actionPerformed(ActionEvent e) {
    imageSrc="";
    image=emptyImage;
    shapeList.clear();
    imagemapPanel.setImage(image);
    jTextArea_html.setText(shapeList.get_html(imageSrc, image.getWidth(null), image.getHeight(null)));
  }

  void jMenuItem_zoom_actionPerformed(ActionEvent e) {
      int zoomFactor=0;
      if (e.getSource()==jMenuItem_zoom1)
          zoomFactor=1;
      else if (e.getSource()==jMenuItem_zoom2)
          zoomFactor=2;
      else if (e.getSource()==jMenuItem_zoom4)
          zoomFactor=4;
      else if (e.getSource()==jMenuItem_zoom8)
          zoomFactor=8;
      if (imagemapPanel.zoom(zoomFactor)==false)
          jMenuItem_zoom1.setSelected(true);
      vRule.repaint();
      hRule.repaint();
  }

  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void jButton_savehtml_actionPerformed(ActionEvent e) {
    File f;
    JFileChooser chooser = new JFileChooser(".");
    chooser.setDialogTitle("Save HTML file");
    //chooser.setLocale(currentLocale);
    if (chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
      f = chooser.getSelectedFile();
    } else return;  // FileChooser Cancel

    try {
      OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f));
      out.write(jTextArea_html.getText());
      out.close();
    } catch (FileNotFoundException ex) {
    } catch (IOException ex) {
      statusBar.setText("HTML code saved to file " + f.getName());
    }
  }

  void jButton_copyhtml_actionPerformed(ActionEvent e) {
    jTextArea_html.selectAll();
    jTextArea_html.copy();
    statusBar.setText("HTML code copied to clipboard");
  }





}







