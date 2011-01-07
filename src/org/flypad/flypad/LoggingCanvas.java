/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author albus
 */
public class LoggingCanvas extends Canvas {
    private final FlypadMIDlet app;

    public LoggingCanvas(final FlypadMIDlet app) {
        this.app = app;
    }

    String message = "Loading...";
    boolean error = false;

    public final void log(final String message) {
        this.message = message;
        error = false;
        repaint();
    }

    public final void logError(final String message) {
        this.message = message;
        error = true;
        repaint();
    }

    public final synchronized void connected() {
        message = "CONNECTED!";
        repaint();
        app.dispatcher.initializeCollector();
        app.switchDisplayable(null, app.dwcanvas);
    }

    public final synchronized void lostConnection() {
        app.dispatcher.closeAccelerometer();
        app.switchDisplayable(null, this);
    }

    protected void paint(Graphics g) {
        final int w = getWidth();
        final int h = getHeight();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, w, h);
        if (error) {
            g.setColor(0xFF0000);
        } else {
            g.setColor(0);
        }
        
        g.drawString(message, 5, h / 2, Graphics.LEFT | Graphics.TOP);
    }
}
