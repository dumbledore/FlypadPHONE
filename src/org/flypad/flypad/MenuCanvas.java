/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import org.flypad.connection.FlypadClient;

/**
 *
 * @author albus
 */
public class MenuCanvas extends Canvas {
    private final FlypadMIDlet midlet;
    private final FlypadClient client;

    public MenuCanvas(final FlypadMIDlet midlet) {
        this.midlet = midlet;
        this.client = midlet.getManager();
    }

    protected void paint(Graphics g) {
        
    }

    protected void keyPressed(final int key) {
//        manager.send();
    }
}
