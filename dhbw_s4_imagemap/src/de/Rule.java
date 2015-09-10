/*
 * Rule.java
 *
 * Created on 31. August 2003, 23:57
 */

package de;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

/* Rule.java is used by ScrollDemo.java. */

public class Rule extends JComponent {
    public static final int INCH = Toolkit.getDefaultToolkit().
            getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SIZE = 10;

    public int orientation;
    private int increment=10;
    private int units=1;
    private ImagemapPanel imagemapPanel;
    
    public Rule(int o, ImagemapPanel imagemapPanel) {
        orientation = o;
        this.imagemapPanel=imagemapPanel;
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }



    public int getIncrement() {
        return increment;
    }

    public void setPreferredHeight(int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    public void setPreferredWidth(int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }

    public void paintComponent(Graphics g) {
        Rectangle drawHere = g.getClipBounds();

        // Fill clipping area with dirty brown/orange.
        g.setColor(new Color(230, 163, 4));
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        Vector vGuides=imagemapPanel.vGuides;
        Vector hGuides=imagemapPanel.hGuides;
        
        g.setColor(Color.black);
        if (orientation==HORIZONTAL && vGuides!=null) {
            for (int i=0; i<vGuides.size(); i++) { 
                Integer ix=(Integer)vGuides.get(i);
                int x=ix.intValue()*imagemapPanel.zoomFactor;
                g.drawLine(x, 0, x, SIZE);
            }
        } else if (orientation==VERTICAL && hGuides!=null) {
            for (int i=0; i<hGuides.size(); i++) { 
                Integer iy=(Integer)hGuides.get(i);
                int y=iy.intValue()*imagemapPanel.zoomFactor;
                g.drawLine(0, y, SIZE, y);
            }
        }
        
        
        // Do the ruler labels in a small font that's black.
        //g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.black);

        // Some vars we need.
        int end = 0;
        int start = 0;
        int tickLength = 0;

        int increment=this.increment*imagemapPanel.zoomFactor;
        
        // Use clipping bounds to calculate first and last tick locations.
        if (orientation == HORIZONTAL) {
            start = (drawHere.x / increment) * increment;
            end = (((drawHere.x + drawHere.width) / increment) + 1)
                  * increment;
        } else {
            start = (drawHere.y / increment) * increment;
            end = (((drawHere.y + drawHere.height) / increment) + 1)
                  * increment;
        }

        // ticks 
        for (int i = start; i < end; i += increment) {
            tickLength = 3;
            if (tickLength != 0) {
                if (orientation == HORIZONTAL) {
                    g.drawLine(i, SIZE-1, i, SIZE-tickLength-1);
                } else {
                    g.drawLine(SIZE-1, i, SIZE-tickLength-1, i);
                }
            }
        }
    }
    
  protected void processMouseMotionEvent(MouseEvent e) {
    super.processMouseMotionEvent(e);
    int id = e.getID();
    if (id==MouseEvent.MOUSE_DRAGGED || id==MouseEvent.MOUSE_MOVED) {
      int x=(int)e.getPoint().getX();
      int y=(int)e.getPoint().getY();
      //System.out.println(x+" "+y);
    }
  }

  protected void processMouseEvent(MouseEvent e) {
    super.processMouseEvent(e);
    int id = e.getID();
    int mod=e.getModifiers();

    int mouseX=(int)e.getPoint().getX();
    int mouseY=(int)e.getPoint().getY();
    
    if (id==MouseEvent.MOUSE_PRESSED && mod==MouseEvent.BUTTON1_MASK) {
        System.out.println(mouseX+ " "+ mouseY);
        /*if (orientation==VERTICAL)
            imagemapPanel.addHGuide(mouseY);
        else
            imagemapPanel.addVGuide(mouseX);
        imagemapPanel.repaint();
        this.repaint();*/
        
        Vector vGuides=imagemapPanel.vGuides;
        Vector hGuides=imagemapPanel.hGuides;
        if (orientation==HORIZONTAL && vGuides!=null) {
            for (int i=0; i<vGuides.size(); i++) { 
                Integer ix=(Integer)vGuides.get(i);
                int x=ix.intValue()*imagemapPanel.zoomFactor;
                if (Math.abs(x-mouseX)<5) { 
                    imagemapPanel.drawingVGuide=x;  // bestehende Hilfslinie "anfassen"
                    vGuides.remove(ix);
                    break;
                }
            }
            if (imagemapPanel.drawingVGuide<0)
                imagemapPanel.drawingHGuide=0;  // neue Hilfslinie
        } else if (orientation==VERTICAL && hGuides!=null) {
            for (int i=0; i<hGuides.size(); i++) { 
                Integer iy=(Integer)hGuides.get(i);
                int y=iy.intValue()*imagemapPanel.zoomFactor;
                if (Math.abs(y-mouseY)<5) { 
                    imagemapPanel.drawingHGuide=y; // bestehende Hilfslinie "anfassen"
                    hGuides.remove(iy);
                    break;
                }
            }
            if (imagemapPanel.drawingHGuide<0)
                imagemapPanel.drawingVGuide=0;  // neue Hilfslinie
        }
        
        repaint();
        imagemapPanel.requestFocus();
    } else if (id==MouseEvent.MOUSE_PRESSED && mod==MouseEvent.BUTTON3_MASK) {
        Vector vGuides=imagemapPanel.vGuides;
        Vector hGuides=imagemapPanel.hGuides;
        if (orientation==HORIZONTAL && vGuides!=null) {
            for (int i=0; i<vGuides.size(); i++) { 
                Integer ix=(Integer)vGuides.get(i);
                int x=ix.intValue()*imagemapPanel.zoomFactor;
                if (Math.abs(x-mouseX)<10) { 
                    vGuides.remove(ix);
                    repaint();
                    imagemapPanel.repaint();
                    break;
                }
            }
        } else if (orientation==VERTICAL && hGuides!=null) {
            for (int i=0; i<hGuides.size(); i++) { 
                Integer iy=(Integer)hGuides.get(i);
                int y=iy.intValue()*imagemapPanel.zoomFactor;
                if (Math.abs(y-mouseY)<10) { 
                    hGuides.remove(iy);
                    repaint();
                    imagemapPanel.repaint();                    
                    break;
                }
            }   
        }
    }
  }

}
