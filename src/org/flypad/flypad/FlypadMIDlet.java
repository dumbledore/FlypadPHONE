/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import org.flypad.command.CommandDispatcher;

/**
 * @author albus
 */
public class FlypadMIDlet extends MIDlet {

    public final LoggingCanvas logger = new LoggingCanvas(this);
    public final CommandDispatcher dispatcher = new CommandDispatcher(logger);
    public final DrivingWheelCanvas dwcanvas = new DrivingWheelCanvas(this);

    {
        logger.setFullScreenMode(true);
        dwcanvas.setFullScreenMode(true);
    }

    public void startApp() {
        switchDisplayable(null, logger);
        dispatcher.initializeConnection();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        dispatcher.close();
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
