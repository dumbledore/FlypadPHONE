/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.flypad;

import org.flypad.connection.Connection;
import org.flypad.connection.DataListener;
import org.flypad.connection.Server;
import org.flypad.util.log.CanvasLogger;

/**
 *
 * @author albus
 */
public class MenuCanvas
        extends CanvasLogger
        implements DataListener {
    
    private Connection connection;

    public void createConnection() {
        try {
            connection = new Server(this, this);
        } catch (Throwable t) {
            log(t.getMessage());
        }
    }

    protected void keyPressed(final int key) {
        connection.send(("X: " + key).getBytes());
    }

    public void receive(final byte[] data) {
        log("{R} " + new String(data));
    }
}
