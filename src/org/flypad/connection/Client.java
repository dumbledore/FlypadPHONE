/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.IOException;
import javax.bluetooth.LocalDevice;
import org.flypad.util.Logger;

/**
 *
 * @author albus
 */
public class Client extends Base {
    
    private Transmission transmission;
    private final Logger logger;

    public Client(final Logger logger) throws IOException {
        /*
         * Retrieve the local device to get to the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Clients retrieve the discovery agent
         */
        discoveryAgent = localDevice.getDiscoveryAgent();

        this.logger = logger;

        logger.log("Client created.");
    }

    public final void connect() throws IOException {
        if (transmission == null) {
            logger.log("Attempting transmission...");
            transmission = new Transmission(discoveryAgent, serviceUUID, logger);
            transmission.start();
        }
    }

    public final void send(final byte[] data) {
        if (transmission != null) {
            transmission.send(data);
        }
    }
}
