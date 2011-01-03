/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
class PhysicalConnection implements Connection {
    private final Reception reception;
    private final Transmission transmission;
    private final DataListener dataListener;

    public PhysicalConnection(
            final StreamConnection connection,
            final DataListener dataListener) {

        this.dataListener = dataListener;
        reception = new Reception(connection, this);
        reception.start();
        transmission = new Transmission(connection);
        transmission.start();
    }

    public void send(byte[] data) {
        transmission.send(data);
    }

    public void receive(byte[] data) {
        dataListener.receive(data);
    }
}
