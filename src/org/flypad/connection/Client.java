/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.IOException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.flypad.util.log.Logger;

/**
 *
 * @author albus
 */
public class Client extends ManagedConnection {
    
    public Client(
            final DataListener dataListener,
            final Logger logger)
            throws IOException {

        super(dataListener, logger);
        /*
         * Retrieve the local device to get to the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Clients retrieve the discovery agent
         */
        discoveryAgent = localDevice.getDiscoveryAgent();

        logger.log("Client created.");

        connect();
    }

    private final void connect() throws IOException {
        logger.log("Searching for host...");

        final String url = discoveryAgent.selectService(
                serviceUUID,
                ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                false);

        if (url == null) {
            throw new IOException("Couldn't find host");
        }

        logger.log("Host found. Connecting...");
        logger.log(url);

        StreamConnection connection = (StreamConnection) Connector.open(url);
        logger.log("Connected!");
        
        this.connection = new PhysicalConnection(connection, dataListener);
    }

    public void send(byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void receive(byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    class ServerDiscoverer implements DiscoveryListener {
//        private volatile boolean searching = false;
//        private RemoteDevice remote = null;
//
//        public void search() {
//            if (searching == false) {
//                try {
//                    searching = true;
//                    logger.log("Searching for host...");
//                    discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
//                } catch (Exception e) {
//                    logger.log(e.toString());
//                }
//            }
//        }
//
//        public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
//            logger.log("Discovered " + rd.getBluetoothAddress());
//            remote = rd;
//            try {
//                logger.log(rd.getFriendlyName(false));
//            } catch (IOException e) {
//                logger.log(e.toString());
//            }
//        }
//
//        public void inquiryCompleted(int i) {
//            logger.log("inquiery completed!");
//            try {
//                discoveryAgent.searchServices(null, uuids, remote, this);
//            } catch (IOException e) {
//                logger.log(e.toString());
//            }
//        }
//
//        public void serviceSearchCompleted(int i, int i1) {
//
//        }
//
//        public void servicesDiscovered(int i, ServiceRecord[] srs) {
//
//        }
//    }
}
