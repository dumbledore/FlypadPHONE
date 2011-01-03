/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.IOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import org.flypad.util.log.Logger;

/**
 *
 * @author albus
 */
public class Server extends ManagedConnection {
    /**
     * Define the server connection URL
     */
    private final String connURL =
            "btspp://localhost:" + serviceUUID.toString() + ";"
            + "name=" + serviceName;

    private final StreamConnectionNotifier server;
    private final ClientManager discoverer = new ClientManager(this);

    public Server(
            final DataListener dataListener,
            final Logger logger)
            throws IOException {

        super(dataListener, logger);

        logger.log("Creating server...");
        /*
         * Retrieve the local device to get the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Servers set the discoverable mode to GIAC
         */
        logger.log("Setting GIAC...");
        localDevice.setDiscoverable(DiscoveryAgent.GIAC);

        /*
         * Create a server connection (a notifier)
         */
        server = (StreamConnectionNotifier) Connector.open(connURL);
        logger.log("Server is running...");
        discoverer.start();
    }

    class ClientManager extends Thread {
        private final DataListener dataListener;
        private volatile boolean alive = true;

        ClientManager(final DataListener dataListener) {
            this.dataListener = dataListener;
        }

        public void run() {
            while (alive) {
                try {
                    /*
                     * Accept a new client connection
                     */
                    logger.log("Awaiting client connection...");
                    StreamConnection client = server.acceptAndOpen();

                    /*
                     * Get a handle on the connection
                     */
                    RemoteDevice remote = RemoteDevice.getRemoteDevice(client);
                    logger.log("New client connection to "
                            + remote.getFriendlyName(false));

                    connection = new PhysicalConnection(client, dataListener);
                } catch (IOException e) {
                    logger.log(e.toString());
                }
            }
        }
    }
}
