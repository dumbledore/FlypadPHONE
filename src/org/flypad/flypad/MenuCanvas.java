/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import org.flypad.connection.Client;
import org.flypad.util.Logger;

/**
 *
 * @author albus
 */
public class MenuCanvas
        extends Canvas
        implements Logger {
    
    private final FlypadMIDlet midlet;
    private final Vector messages = new Vector(30);

    public MenuCanvas(final FlypadMIDlet midlet) {
        this.midlet = midlet;
    }

    protected void paint(Graphics g) {
        final int w = getWidth();
        final int h = getHeight();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, w, h);
        g.setColor(0);
        int y = 10;
        final int hh = g.getFont().getHeight() + 4;
        for (int i = 0; i < messages.size(); i++) {
            final String message = (String) messages.elementAt(i);
            g.drawString(message, 10, y, Graphics.LEFT | Graphics.TOP);
            y += hh;
        }
    }

    protected void keyPressed(final int key) {
        final Client client = midlet.getClient();
        if (client != null) {
            final String s = "Sample data: " + key;
            log(s);
            client.send(s.getBytes());
        } else {
            log("Client is null");
        }
    }

    public void log(final String message) {
        if (message != null) {
            System.out.println(message);
            messages.addElement(message);
        } else {
            messages.addElement("message was null");
        }
        repaint();
    }
}
