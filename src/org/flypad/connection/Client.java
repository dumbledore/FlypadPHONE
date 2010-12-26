/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.IOException;
import javax.bluetooth.LocalDevice;

/**
 *
 * @author albus
 */
public class Client extends Base {
    
    private Transmission transmission;

    public Client() throws IOException {
        /*
         * Retrieve the local device to get to the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Clients retrieve the discovery agent
         */
        discoveryAgent = localDevice.getDiscoveryAgent();

        System.out.println("Client created.");
    }

    public final void connect() throws IOException {
        if (transmission == null) {
            System.out.println("Attempting transmission...");
            transmission = new Transmission(discoveryAgent, serviceUUID);
            transmission.start();
        }
    }

    public final void send(final byte[] data) {
        if (transmission != null) {
            transmission.send(data);
        }
    }
}
