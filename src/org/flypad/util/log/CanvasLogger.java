/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.util.log;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author albus
 */
public class CanvasLogger
        extends Canvas
        implements Logger {

    private final BufferedLogger logger;

    public CanvasLogger() {
        logger = new BufferedLogger();
    }

    public CanvasLogger(final BufferedLogger logger) {
        this.logger = logger;
    }

    protected void paint(Graphics g) {
        final int w = getWidth();
        final int h = getHeight();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, w, h);
        g.setColor(0);
        int y = 10;
        final String[] messages = logger.getMessages(false, true);
        final int hh = g.getFont().getHeight() + 4;
        for (int i = 0; i < messages.length; i++) {
            final String message = messages[i];
            g.drawString(message, 10, y, Graphics.LEFT | Graphics.TOP);
            y += hh;
        }
    }

    public void log(String message) {
        logger.log(message);
        repaint();
    }

    public void logError(String message) {
        logger.logError(message);
        repaint();
    }

    public void log(int number) {
        logger.log(number);
        repaint();
    }

    public void log(double number) {
        logger.log(number);
        repaint();
    }

    public void log(boolean bool) {
        logger.log(bool);
        repaint();
    }

    public void log(Throwable t) {
        logger.log(t);
        repaint();
    }
}
