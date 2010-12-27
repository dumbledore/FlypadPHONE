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

    private final Client client;
    private final MenuCanvas menu;

    public FlypadMIDlet() {
        menu = new MenuCanvas(this);

        try {
            client = new Client(menu);
            menu.log("New client was created.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.toString());
        }
    }
    
    public final Client getClient() {
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
