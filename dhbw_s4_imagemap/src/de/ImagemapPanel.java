package de;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;

/**
 * Title:        ImageMap
 * Description:  client side imagemap creator
 * Copyright:    Copyright (c) 2001
 * Company:      webdesign-pirna.de
 * @author Andreas Tetzl
 * @version 1.0
 */


public class ImagemapPanel extends JPanel implements DropTargetListener, DragSourceListener, DragGestureListener {

  DropTarget dropTarget = new DropTarget (this, this);
  DragSource dragSource = DragSource.getDefaultDragSource();    
    
  public static int ACTION_RECT = 1;
  public static int ACTION_CIRCLE = 2;
  public static int ACTION_POLY = 3;
  public static int ACTION_ADDPOINT = 4;
  public static int ACTION_DELPOINT = 5;

  Imagemap main;
  Image img = null;
  Image imgUnscaled = null;
  ShapeList shapeList;
  int currentAction=0;
  Date prevClick = null;
  JLabel statusBar;
  JCheckBoxMenuItem cb_snap;
  JCheckBoxMenuItem cb_antialias;
  int zoomFactor=1;
  int offset=0;

  Vector hGuides=null;
  Vector vGuides=null;
  int drawingHGuide=-1;
  int drawingVGuide=-1;
  
  public ImagemapPanel(Image img, ShapeList s, JLabel sb, JCheckBoxMenuItem cb_snap, JCheckBoxMenuItem cb_antialias, Imagemap main) {
    this.img=img;
    this.imgUnscaled=img;
    this.setSize(img.getWidth(null), img.getHeight(null));
    this.shapeList=s;
    this.statusBar=sb;
    this.cb_antialias=cb_antialias;
    this.cb_snap=cb_snap;
    this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    this.enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        myKeyPressed(e);
      }
    });
    setAutoscrolls(true);
    this.main=main;
    hGuides=new Vector();
    vGuides=new Vector();
    dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
    //setModel(new DefaultListModel());
  }

  public void setImage(Image img) {
    this.img=img;
    this.imgUnscaled=img;
    zoomFactor=1;
    this.setSize(img.getWidth(null), img.getHeight(null));
    revalidate();
    repaint();
  }

  /* overwrite getXXX() methods */
  public int getWidth() {
    return img.getWidth(null);
  }
  public int getHeight() {
    return img.getHeight(null);
  }
  public Dimension getSize() {
    return new Dimension(getWidth(), getHeight());
  }
  public Dimension getPreferredSize() {
    return new Dimension(getWidth(), getHeight());
  }
  public Dimension getMinimumSize() {
    return new Dimension(getWidth(), getHeight());
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2=(Graphics2D)g;
    if (cb_antialias.isSelected()) {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    if (img!=null) {
      //setSize(img.getWidth(null), img.getHeight(null));
      g2.drawImage(img, 0, 0, this);
    }

    if (drawingHGuide>=0) {
        g2.drawLine(0, drawingHGuide*zoomFactor, getWidth(), drawingHGuide*zoomFactor);
    } else if (drawingVGuide>=0) {
        g2.drawLine(drawingVGuide*zoomFactor, 0, drawingVGuide*zoomFactor, getHeight());
    }
    
    if (hGuides!=null) 
        for (int i=0; i<hGuides.size(); i++) { 
            Integer iy=(Integer)hGuides.get(i);
            int y=iy.intValue()*zoomFactor;
            g2.drawLine(0, y, getWidth(), y);  
        }

    if (vGuides!=null) 
        for (int i=0; i<vGuides.size(); i++) { 
            Integer ix=(Integer)vGuides.get(i);
            int x=ix.intValue()*zoomFactor;
            g2.drawLine(x, 0, x, getHeight());  
        }
    
    for (int i=0; i<shapeList.size(); i++)
      shapeList.get_shape(i).draw(g2, zoomFactor);
    
    
  }

  public void addHGuide(int y) {
    hGuides.add(new Integer(y));
    main.vRule.repaint();
  }

  public void addVGuide(int x) {
    vGuides.add(new Integer(x));
    main.hRule.repaint();
  }
  
  public void set_action(int act) {
    currentAction=act;
    if (act==ACTION_RECT)
      statusBar.setText("hold down mouse button and draw rectangle, ESC to abort");
    else if (act==ACTION_CIRCLE)
      statusBar.setText("hold down mouse button and move right to set radius, ESC to abort");
    else if (act==ACTION_POLY)
      statusBar.setText("click to set points, double click sets last point, ESC to abort");
    else if (act==ACTION_ADDPOINT)
      statusBar.setText("click on polygon line to add point");
    else if (act==ACTION_DELPOINT)
      statusBar.setText("click on polygon point to remove it");
  }

  boolean buttonDown=false;
  boolean drawingPoly=false;
  Shape activeShape=null;
  int movingPoint=0;
  int ox,oy;

  protected void processMouseMotionEvent(MouseEvent e) {
    super.processMouseMotionEvent(e);
    int id = e.getID();
    Graphics g = this.getGraphics();
    
    if (id==MouseEvent.MOUSE_DRAGGED || id==MouseEvent.MOUSE_MOVED) {
      int x=(int)e.getPoint().getX() / zoomFactor;
      int y=(int)e.getPoint().getY() / zoomFactor;

      if (x<0) x=0;  if (y<0) y=0;
      if (x>getWidth()) x=getWidth();
      if (y>getHeight()) y=getHeight();
     
      if (drawingHGuide>=0) {
          drawingHGuide=y;
          if (cb_snap.isSelected()) {
                /* von allen Shape Instanzen Punkt mit geringstem Abstand zum Mauspointer abfragen.
                wenn Punkt mit geringstem Abstand weniger als 5 Pixel entfernt -> "anschnappen"  */
            int p=10000;
            for (int i=0; i<shapeList.size(); i++) {
                int np=shapeList.get_shape(i).minYDistance(y);
                if (Math.abs(y-np) < Math.abs(y-p))
                    p=np;
            }
            if (Math.abs(y-p) < 5) {
                drawingHGuide=p;
            }
          }
          statusBar.setText(""+drawingHGuide);
          repaint();
      } else if (drawingVGuide>=0) {
          drawingVGuide=x;
          if (cb_snap.isSelected()) {
                /* von allen Shape Instanzen Punkt mit geringstem Abstand zum Mauspointer abfragen.
                wenn Punkt mit geringstem Abstand weniger als 5 Pixel entfernt -> "anschnappen"  */
            int p=10000;
            for (int i=0; i<shapeList.size(); i++) {
                int np=shapeList.get_shape(i).minXDistance(x);
                if (Math.abs(x-np) < Math.abs(x-p))
                    p=np;
            }
            if (Math.abs(x-p) < 5) {
                drawingVGuide=p;
            }
          }
          statusBar.setText(""+drawingVGuide);
          repaint(); 
      }
      
      // An horizontale/vertikale Hilfslinien "anschnappen"
      if (cb_snap.isSelected() && vGuides!=null) 
        for (int i=0; i<vGuides.size(); i++) { 
            Integer ix=(Integer)vGuides.get(i);
            int gx=ix.intValue();
            if (Math.abs(gx-x)<5) {
                x=gx;
                break;
            }
        }
      if (cb_snap.isSelected() && hGuides!=null) 
        for (int i=0; i<hGuides.size(); i++) { 
            Integer iy=(Integer)hGuides.get(i);
            int gy=iy.intValue();
            if (Math.abs(gy-y)<5) {
                y=gy;
                break;
            }
        }

      if (movingPoint!=0 && activeShape!=null) {
          statusBar.setText(activeShape.getCoordString(x,y));
        if (cb_snap.isSelected() && movingPoint!=Shape.POINT_CIRCLE_INSIDE &&
            movingPoint!=Shape.POINT_POLY_INSIDE && movingPoint!=Shape.POINT_RECT_INSIDE) {
          /* von allen Shape Instanzen Punkt mit geringstem Abstand zum Mauspointer abfragen.
             wenn Punkt mit geringstem Abstand weniger als 5 Pixel entfernt -> "anschnappen"  */
          Point p=new Point(10000, 10000);
          for (int i=0; i<shapeList.size(); i++) {
            Point np=shapeList.get_shape(i).minDistance(x,y,activeShape,movingPoint);
            if (np!=null && np.distance((double)x, (double)y) < p.distance((double)x, (double)y))
              p=np;
          }
          if (p.distance((double)x, (double)y)<10.0f) {
            x=p.x;
            y=p.y;
          }
        }
        activeShape.moveKeyPoint(movingPoint,x,y);
        this.repaint();
      }
    }
  }

  protected void processMouseEvent(MouseEvent e) {
    super.processMouseEvent(e);
    int id = e.getID();
    int mod=e.getModifiers();
    Calendar cal = new GregorianCalendar().getInstance();

    this.requestFocus();

    int mouseX=(int)e.getPoint().getX() / zoomFactor;
    int mouseY=(int)e.getPoint().getY() / zoomFactor;
    
    if (id==MouseEvent.MOUSE_PRESSED && mod==MouseEvent.BUTTON1_MASK && drawingVGuide>=0) {
        addVGuide(drawingVGuide);
        drawingVGuide=-1;
    } else if (id==MouseEvent.MOUSE_PRESSED && mod==MouseEvent.BUTTON1_MASK && drawingHGuide>=0) {
        addHGuide(drawingHGuide);
        drawingHGuide=-1;
    } else if (id==MouseEvent.MOUSE_PRESSED && mod==MouseEvent.BUTTON1_MASK && !drawingPoly) {
      buttonDown=true;
      if (currentAction==ACTION_RECT) {
        ox=mouseX;
        oy=mouseY;
        activeShape=new Shape(ox,oy,ox,oy);
        shapeList.add_shape(activeShape);
        movingPoint=Shape.POINT_RECT_LOWERRIGHT;
      } else if (currentAction==ACTION_CIRCLE) {
        activeShape=new Shape(new Point(mouseX, mouseY), 0);
        shapeList.add_shape(activeShape);
        movingPoint=Shape.POINT_CIRCLE_RADIUS;
      } else if (currentAction==ACTION_POLY && !drawingPoly) {
        activeShape=new Shape(new Point(mouseX, mouseY));
        shapeList.add_shape(activeShape);
        movingPoint=1;
        drawingPoly=true;
      } else if (currentAction==ACTION_ADDPOINT) {
        shapeList.addPoint(mouseX, mouseY);
      } else if (currentAction==ACTION_DELPOINT) {
        shapeList.removePointAt(mouseX, mouseY);
      } else {
        for (int i=shapeList.size()-1; i>=0; i--) {
          movingPoint=shapeList.get_shape(i).isKeyPoint(mouseX, mouseY);
          if (movingPoint!=0) {
            activeShape=shapeList.get_shape(i);
            break;
          }
        }
      }
      currentAction=0;
    } else if (id==MouseEvent.MOUSE_RELEASED && mod==MouseEvent.BUTTON1_MASK && !drawingPoly) {
      int x=mouseX;
      int y=mouseY;

      if (buttonDown && movingPoint!=0 && !drawingPoly) {
        //if (activeShape!=null) activeShape.moveKeyPoint(movingPoint,x,y);
        activeShape=null;
      }
      buttonDown=false;
      this.repaint();
      statusBar.setText(" ");
    } else if (id==MouseEvent.MOUSE_CLICKED && mod==MouseEvent.BUTTON1_MASK && drawingPoly &&
               prevClick!=null && cal.getTime().getTime()-prevClick.getTime()<400) {  // double click
      drawingPoly=false;
      activeShape.remove_lastPolyPoint();
      activeShape=null;
      movingPoint=0;
      statusBar.setText(" ");
    } else if (id==MouseEvent.MOUSE_CLICKED && mod==MouseEvent.BUTTON1_MASK && drawingPoly) {
      movingPoint=activeShape.add_polypoint(mouseX, mouseY);
      prevClick=cal.getTime();
    }
  }

  
  protected void myKeyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    int modifiers = e.getModifiers();
    String tmpString = KeyEvent.getKeyModifiersText(modifiers);

//    System.out.println(keyCode);
//    System.out.println(modifiers);
//    System.out.println(tmpString);

    if (keyCode==27) { //Escape
      currentAction=0;
      buttonDown=false;
      drawingPoly=false;
      shapeList.remove_shape(activeShape);
      activeShape=null;
      movingPoint=0;
      this.repaint();
      statusBar.setText(" ");
    } 

  }


  public boolean zoom(int zoomFactor) {
      if (img==null) return false;
      if (zoomFactor>1) {
        img=imgUnscaled.getScaledInstance(imgUnscaled.getWidth(null)*zoomFactor, imgUnscaled.getHeight(null)*zoomFactor, 0);
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException ie) {
            return false;
        }
      } else
        img=imgUnscaled;
      this.zoomFactor=zoomFactor;
      this.setSize(img.getWidth(null), img.getHeight(null));
      this.setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
      revalidate();
      repaint();
      return true;
  }
  
        public void dragDropEnd(DragSourceDropEvent DragSourceDropEvent){}
        public void dragEnter(DragSourceDragEvent DragSourceDragEvent){}
        public void dragExit(DragSourceEvent DragSourceEvent){}
        public void dragOver(DragSourceDragEvent DragSourceDragEvent){}
        public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent){}

        public void dragEnter (DropTargetDragEvent dropTargetDragEvent)
        {
           dropTargetDragEvent.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
        }

        public void dragExit (DropTargetEvent dropTargetEvent) {}
        public void dragOver (DropTargetDragEvent dropTargetDragEvent) {}
        public void dropActionChanged (DropTargetDragEvent dropTargetDragEvent){}

        public synchronized void drop (DropTargetDropEvent dropTargetDropEvent)
        {
            try
            { 
                Transferable tr = dropTargetDropEvent.getTransferable();
                if (tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor)) {
                    dropTargetDropEvent.acceptDrop (DnDConstants.ACTION_COPY_OR_MOVE);
                    java.util.List fileList = (java.util.List)
                        tr.getTransferData(DataFlavor.javaFileListFlavor);
                    Iterator iterator = fileList.iterator();
                    while (iterator.hasNext()) {
                        File file = (File)iterator.next();
                        System.out.println(file.getName()+"\n"+file.toURL().toString()+"\n"+file.getAbsolutePath());
                        String fname=file.getName().toLowerCase();
                        if (fname.endsWith(".html") || fname.endsWith(".htm")) {
                            main.parseHtml(file);
                        } else {  // Alles andere versuchen als Bild zu laden
                            main.openImage(file.getAbsolutePath());
                        }
                        break; // nur die erste Datei
                    }
                    dropTargetDropEvent.getDropTargetContext().dropComplete(true);
              } else {
                System.err.println ("Rejected");
                dropTargetDropEvent.rejectDrop();
              }
            } catch (IOException io) {
                io.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
                dropTargetDropEvent.rejectDrop();
            }
        }

        public void dragGestureRecognized(DragGestureEvent dragGestureEvent)
        {/*
            //if (getSelectedIndex() == -1)
            //    return;
            //Object obj = getSelectedValue();
            //if (obj == null) {
                // Nothing selected, nothing to drag
                System.out.println ("Nothing selected - beep");
                getToolkit().beep();
            } else {
                Hashtable table = (Hashtable)obj;
                FileSelection transferable =
                  new FileSelection(new File((String)table.get("path")));
                dragGestureEvent.startDrag(
                  DragSource.DefaultCopyDrop,
                  transferable,
                  this);
            }*/
        }
  
}