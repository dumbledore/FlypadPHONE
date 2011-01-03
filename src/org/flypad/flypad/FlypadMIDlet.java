/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import javax.microedition.midlet.*;
import org.flypad.connection.Client;
import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * @author albus
 */
public class FlypadMIDlet extends MIDlet {

    private final MenuCanvas menu;

    public FlypadMIDlet() {
        menu = new MenuCanvas();
    }

    public void startApp() {
        switchDisplayable(null, menu);
        menu.createConnection();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = Display.getDisplay(this);
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }
}
