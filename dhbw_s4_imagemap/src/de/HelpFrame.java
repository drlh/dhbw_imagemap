package de;

/**
 * Title:        ImageMap
 * Description:  client side imagemap creator
 * Copyright:    Copyright (c) 2001
 * Company:      webdesign-pirna.de
 * @author Andreas Tetzl
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class HelpFrame extends JFrame  implements Runnable {
  Toolkit toolkit = Toolkit.getDefaultToolkit ();
  Thread thread;
  boolean running=true;
  JEditorPane jEditorPane_help = new JEditorPane();

  public HelpFrame ( ) {
    Dimension scrSize;

    scrSize = toolkit.getScreenSize();
    setLocation ( ( scrSize.width / 2 ) - ( 400 / 2 ),
                 ( scrSize.height / 2 ) - ( 400 / 2 ) );
    setSize ( 400, 400 );
    setTitle("ImageMap Help");

    jEditorPane_help.setEditable(false);
    try {
      jEditorPane_help.setPage(getClass().getResource("help.html"));
    } catch (IOException e) {
    }

    getContentPane().setLayout(new BorderLayout(0,0));
    this.getContentPane().add(new JScrollPane(jEditorPane_help), BorderLayout.CENTER);

    thread = new Thread(this);
    thread.setPriority(Thread.NORM_PRIORITY);
    thread.start();

    show ();
    toFront ();
  }


  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      running=false;
    }
  }


  public void run()
  {
    while(running) {

      try
      {
       thread.sleep(200);
      }
      catch(Exception e){}

    }
    hide();
  }


}

