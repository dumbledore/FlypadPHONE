/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import javax.microedition.midlet.*;
import org.flypad.connection.FlypadClient;
import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * @author albus
 */
public class FlypadMIDlet extends MIDlet {

    private final FlypadClient client;
    private final MenuCanvas menu = new MenuCanvas(this);

    public FlypadMIDlet() {
        try {
            client = new FlypadClient();
        } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException();
    }

    }
    public final FlypadClient getManager() {
        return client;
    }

    public void startApp() {
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switchDisplayable(null, menu);
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
