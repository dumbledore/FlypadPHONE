/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import org.flypad.connection.Client;

/**
 *
 * @author albus
 */
public class MenuCanvas extends Canvas {
    private final FlypadMIDlet midlet;
    private final Client client;

    public MenuCanvas(final FlypadMIDlet midlet) {
        this.midlet = midlet;
        this.client = midlet.getClient();
        System.out.println("Client: " + (client == null));
    }

    protected void paint(Graphics g) {
        
    }

    protected void keyPressed(final int key) {
        final String s = "Sample data: " + key;
        System.out.println(s);
        System.out.println(client == null);
        client.send(s.getBytes());
    }
}
