package com.ibm.btevents;

import javax.bluetooth.RemoteDevice;

/**
 * @author bygravec
 * Manager for the different threads necessary for two-way communication
 */
public class BTManager {

    private BTTransmitter transmitter;
    private BTReceiver receiver;
    private BTDiscoverer discoverer;
    
//    /**
//     * Create a new BTManager
//     */
//    public BTManager() {
//        transmitter = new BTTransmitter();
//        transmitter.start();
//        receiver = new BTReceiver();
//        receiver.start();
//        discoverer = new BTDiscoverer();
//        discoverer.start();
//    }
    
    /**
     * Create a new BTManager and register the the given listener
     * @param listener
     */
    public BTManager(BTEventListener listener) {
        transmitter = new BTTransmitter();
        transmitter.addBTEventListener(listener);
        transmitter.start();
        receiver = new BTReceiver();
        receiver.addBTEventListener(listener);
        receiver.start();
        discoverer = new BTDiscoverer();
        discoverer.addBTEventListener(listener);
        discoverer.start();
    }

    /**
     * Add a new listener for BTEvents
     * @param listener
     */
    public void addBTEventListener(BTEventListener listener) {
        transmitter.addBTEventListener(listener);
        receiver.addBTEventListener(listener);
        discoverer.addBTEventListener(listener);
    }

    /**
     * Carry out a new search for devices within range
     */
    public void searchForDevices() {
        discoverer.searchForDevices();
    }

    /**
     * Send a meesage to a remote device
     * @param device
     * @param message
     * @throws DeviceNotFoundException
     */
    public void sendMessage(RemoteDevice device, final byte[] message)
            throws DeviceNotFoundException {
        transmitter.sendMessage(device, message);
    }

    /**
     * Check if the necessary bluetooth classes are available
     * @return true if the API is available, false otherwise
     */
    public boolean bluetoothAPIAvailable() {
        try {
            Class.forName("javax.bluetooth.LocalDevice");
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    /**
     * Returns a cached copy of the discovered devices
     * @return an array of RemoteDevices
     */
    public RemoteDevice[] getDevices() {
      return discoverer.getDevices();
  }
}
