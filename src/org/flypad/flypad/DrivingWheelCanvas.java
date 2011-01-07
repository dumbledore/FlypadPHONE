/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import org.flypad.command.CommandDispatcher;

/**
 *
 * @author albus
 */
public class DrivingWheelCanvas extends Canvas {

    private static final int TYPE_DIGITAL_1 = 0;
    private static final int TYPE_DIGITAL_2 = 1;
    private static final int TYPE_DIGITAL_3 = 2;
    private static final int TYPE_DIGITAL_4 = 3;
    private static final int TYPE_NONE   = 4;
    private static final int TYPE_ANALOG_1  = 5;
    private static final int TYPE_ANALOG_2  = 6;

    private int wx, hx, hx2, hx3, w, h, a1, a2, a3, a4;
    private int touchedType = TYPE_NONE;

    private boolean[] digital   = new boolean[4];
    private int       analog1   = 0;
    private int       analog2   = 0;

    private final FlypadMIDlet app;
    private final CommandDispatcher dispatcher;

    public DrivingWheelCanvas(final FlypadMIDlet app) {
        this.app = app;
        this.dispatcher = app.dispatcher;
        recalculatePositions(getWidth(), getHeight());
    }

    protected void paint(Graphics g) {
        final int tt = touchedType;

        /*
         * Draw ANALOG 1 area
         */
        if (tt == TYPE_ANALOG_1) {
            g.setColor(0x00FF00);
        } else {
            g.setColor(0x009900);
        }
        g.fillRect(0, hx3, w, hx);

        /*
         * draw ANALOG 2 area
         */
        if (tt == TYPE_ANALOG_2) {
            g.setColor(0x00FF00);
        } else {
            g.setColor(0x009900);
        }
        g.fillRect(0, 0, w, hx);

        /*
         * Draw the ANALOG 1 & 2 lines
         */
        g.setColor(0);
        g.drawLine(analog1, h, analog1, hx3);
        g.drawLine(analog2, 0, analog2, hx);

        g.setColor(0x000099);
        g.fillRect(0, hx, w, hx2);

        g.setColor(0x0000FF);
        if (tt == TYPE_DIGITAL_1) {
            g.fillRect(0, hx2, wx, hx);
        } else if (tt == TYPE_DIGITAL_3) {
            g.fillRect(wx, hx2, wx, hx);
        } else if (tt == TYPE_DIGITAL_2) {
            g.fillRect(0, hx, wx, hx);
        } else if (tt == TYPE_DIGITAL_4) {
            g.fillRect(wx, hx, wx, hx);
        }

        /*
         * Draw the lines between the buttons
         */
        g.setColor(0x000077);
        g.drawLine(0, hx2, w, hx2);
        g.drawLine(wx, hx, wx, hx3);
    }

    private void recalculatePositions(final int width, final int height) {
        w = width;
        h = height;
        
        wx = w / 2;
        hx = h / 4;
        hx2 = 2 * hx;
        hx3 = 3 * hx;

        final int prc = w / 5;
        final int prc2 = prc / 2;
        
        a1 = prc;
        a2 = w - prc;
        a3 = wx - prc2;
        a4 = wx + prc2;
        
        resetButtons();
    }

    protected final void sizeChanged(final int width, final int height) {
        recalculatePositions(width, height);
    }

    private void resetButtons() {
        digital[0] = digital[1] = digital[2] = digital[3] = false;
        analog1 = analog2 = wx;
        dispatcher.wheelReset();
    }

    private int findType(final int x, final int y) {
        if (y < hx) {
            return TYPE_ANALOG_2;
        } else if (y < hx2) {
            if (x < wx) {
                return TYPE_DIGITAL_2;
            } else {
                return TYPE_DIGITAL_4;
            }
        } else if (y < hx3) {
            if (x < wx) {
                return TYPE_DIGITAL_1;
            } else {
                return TYPE_DIGITAL_3;
            }
        } else {
            return TYPE_ANALOG_1;
        }
    }

    private void processAnalog(final int x, final boolean isAnalog1) {
        int value = 0;

        if (x < a1) {
            value = 0;
        } else if (x > a2) {
            value = w;
//        } else if (a3 < x && x < a4) {
//            value = wx;
        } else {
            value = x;
        }
        
        setAnalog(value, isAnalog1);
        dispatcher.wheelAnalog((isAnalog1 ? (byte) 1 : (byte) 2), value, w);
    }

    private void setAnalog(final int x, final boolean isAnalog1) {
        if (isAnalog1) {
            analog1 = x;
        } else {
            analog2 = x;
        }
    }

    protected final void pointerPressed(final int x, final int y) {
        touchedType = findType(x, y);
        resetButtons();
        if (touchedType == TYPE_ANALOG_1) {
            processAnalog(x, true);
        } else if (touchedType == TYPE_ANALOG_2) {
            processAnalog(x, false);
        } else {
            digital[touchedType] = true;
            dispatcher.wheelDigital((byte) touchedType);
        }
        repaint();
    }

    protected final void pointerDragged(final int x, final int y) {
        final int tt = findType(x, y);
        if (tt == touchedType) {
            if (tt == TYPE_ANALOG_1) {
                processAnalog(x, true);
            } else if (tt == TYPE_ANALOG_2) {
                processAnalog(x, false);
            }
        } else {
            resetButtons();
        }
        repaint();
    }

    protected final void pointerReleased(final int x, final int y) {
        resetButtons();
        touchedType = TYPE_NONE;
        repaint();
    }
}
