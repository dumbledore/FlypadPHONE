package com.ibm.btevents;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;

/**
 * @author bygravec
 * BTThread tasked with transmitting messages to arbitrary devices
 */
public class BTTransmitter extends BTThread implements DiscoveryListener {

    /**
     * Constant value for message retry.
     */
    public static final int MESSAGE_RETRY_COUNT = 5;
    
    private final Vector messageQueue;
    private ServiceRecord record;
    private boolean searchCompleted;
    
    /**
     * Create a new BTTransmitter
     */
    public BTTransmitter() {
        messageQueue = new Vector();
    }
    
    /**
     * Monitor the message queue and transmit any messages found
     * @see java.lang.Runnable#run()
     */
    public void run() {
        boolean messagesRemaining;
        BTMessage message = null;
        while (true) {
            messagesRemaining = (messageQueue.size() > 0);
            while (messagesRemaining) {
                synchronized (messageQueue) {
                    if (messageQueue.size() > 0) {
                        message = (BTMessage) messageQueue.firstElement();
                    }
                }
                if (message != null) message.decrementRetries();
                if (message != null && message.isLive()) {
                    UUID[] filter = { new UUID(BT_ID, false) };
                    try {
                        searchCompleted = false;
                        localDevice.getDiscoveryAgent().searchServices(null, filter, message.getRemote(), this);
                    } catch (BluetoothStateException e) {
                        fireErrorEvent(e.getMessage());
                    }
                    while (!searchCompleted) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                    OutputStream out = null;
                    if (record != null) {
                        try {
                            out = Connector.openOutputStream(
                                    record.getConnectionURL(
                                    ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                                    false));
                            out.write(message.getMessage());
                            fireMessageSentEvent(message.getMessage());
                            messageQueue.removeElement(message);
                        } catch (IOException e) {
                            fireErrorEvent(e.getMessage());
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e) {
                                    fireErrorEvent(e.getMessage());
                                }
                            }
                        }
                    }
                    record = null;
                    message = null;
                } else {
                    // remove dead messages
                    if (message != null) messageQueue.removeElement(message);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    
    /**
     * Send the given message to the named device
     * @param device the device to send to
     * @param message the message to send
     * @throws DeviceNotFoundException if the device is not found
     */
    public void sendMessage(
            final RemoteDevice device,
            final byte[] message)
            throws DeviceNotFoundException {

        RemoteDevice[] devices = localDevice.getDiscoveryAgent().retrieveDevices(DiscoveryAgent.CACHED);
        boolean found = false;
        if (devices != null) {
            int x = 0;
            while (x < devices.length && !found) {
                if (devices[x].equals(device)) found = true;
                x++;
            }
        }
        if (!found) throw new DeviceNotFoundException();
        synchronized (messageQueue) {
            messageQueue.addElement(new BTMessage(device, message, MESSAGE_RETRY_COUNT));
        }
    }
    
    /**
     * @see javax.bluetooth.DiscoveryListener#deviceDiscovered(javax.bluetooth.RemoteDevice, javax.bluetooth.DeviceClass)
     */
    public void deviceDiscovered(RemoteDevice device, DeviceClass dc) {
        try {
            fireDeviceDiscoveredEvent("Device discovered : "+device.getFriendlyName(false));
        } catch (IOException e) {
            fireErrorEvent(e.getMessage());
        }
    }
    
    /**
     * @see javax.bluetooth.DiscoveryListener#servicesDiscovered(int, javax.bluetooth.ServiceRecord[])
     */
    public void servicesDiscovered(int arg0, ServiceRecord[] records) {
        for (int i = 0; i < records.length; i++)
        {
          String conURL =
            records[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
          if (conURL.startsWith(BT_PROTOCOL)) {
              record = records[i];
          }
        }
        
    }
    
    /**
     * @see javax.bluetooth.DiscoveryListener#serviceSearchCompleted(int, int)
     */
    public void serviceSearchCompleted(int arg0, int arg1) {
        searchCompleted = true;
    }

    /**
     * @see javax.bluetooth.DiscoveryListener#inquiryCompleted(int)
     */
    public void inquiryCompleted(int arg0) {
        fireDiagnosticEvent("Completed search");
    }
}