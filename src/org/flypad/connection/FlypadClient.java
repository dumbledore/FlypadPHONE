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

/**
 *
 * @author albus
 */
public class FlypadClient
        extends SimpleConnection
        implements DiscoveryListener {

    private StreamConnection client;
    private RemoteDevice remote;
    private ServiceRecord service;
    
    public FlypadClient() throws IOException {
        /*
         * Retrieve the local device to get to the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Clients retrieve the discovery agent
         */
        discoveryAgent = localDevice.getDiscoveryAgent();
    }

    public void connect() throws IOException {
        /*
         * Given a service of interest, get its service record
         */
        discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
    }

    public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
        try {
            System.out.println("device found: "
                        + rd.getFriendlyName(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        remote = rd;
    }

    public void inquiryCompleted(int i) {
        if (remote != null) {
            try {
                discoveryAgent.searchServices(null, uuids, remote, this);
                System.out.println("Searching for services...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No devices found");
        }
    }

    public void servicesDiscovered(int code, ServiceRecord[] srs) {
        if (srs.length > 0) {
            System.out.println("Found services");
            for (int i = 0; i < srs.length; i++) {
                System.out.println(srs[i].getConnectionURL(
                        ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
            }
            service = srs[0];
        }
    }

    public void serviceSearchCompleted(int i, int i1) {
        if (remote != null && service != null) {
            try {
                final String url = service.getConnectionURL(
                        ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                System.out.println(
                        "Connecting to " + url
                        + " (" + remote.getFriendlyName(false) + ")");

                client = (StreamConnection) Connector.open(url);
                System.out.println("Connected!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No service found");
        }
    }
}
