/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import org.flypad.command.CommandDispatcher;
import org.flypad.util.log.CanvasLogger;

/**
 *
 * @author albus
 */
public class MenuCanvas extends CanvasLogger {

    final CommandDispatcher dispatcher = new CommandDispatcher(this);
    
    protected void keyPressed(final int key) {
//        connection.send(("X: " + key).getBytes());
    }
}
