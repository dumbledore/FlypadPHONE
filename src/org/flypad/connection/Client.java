/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.IOException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.flypad.util.Logger;

/**
 *
 * @author albus
 */
public class Client extends Base implements DiscoveryListener {
    
    private Transmission transmission;
    private final Logger logger;
    private RemoteDevice remote;

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

            logger.log("Searching for host...");
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
//            final String url = discoveryAgent.selectService(
//                    serviceUUID,
//                    ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
//                    false);
//
//            if (url == null) {
//                throw new IOException("Couldn't find host");
//            }
//
//            logger.log("Host found. Connecting...");
//            logger.log(url);

//            server = (StreamConnection) Connector.open(url);
//            logger.log("Connected!");
        }
    }

    public final void send(final byte[] data) {
        if (transmission != null) {
            transmission.send(data);
        }
    }

    public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
        logger.log("Discovered " + rd.getBluetoothAddress());
        remote = rd;
        try {
            logger.log(rd.getFriendlyName(false));
        } catch (IOException e) {
            logger.log(e.toString());
        }
    }

    public void inquiryCompleted(int i) {
        logger.log("inquiery completed!");
        try {
            discoveryAgent.searchServices(null, uuids, remote, this);
        } catch (IOException e) {
            logger.log(e.toString());
        }
    }

    public void serviceSearchCompleted(int i, int i1) {
        
    }

    public void servicesDiscovered(int i, ServiceRecord[] srs) {
        
    }
}
