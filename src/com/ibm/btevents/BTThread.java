package com.ibm.btevents;

import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

/**
 * @author bygravec
 * Supertype for threads in the btevents package, providing common functionality.
 */
public class BTThread extends Thread {
    
    protected static final String BT_ID = "1987";
    protected static final String BT_PROTOCOL = "btspp";

    LocalDevice localDevice;
    private Vector eventListeners;
    
    /**
     * Create a new BTThread
     */
    public BTThread() {
        try {
            localDevice = LocalDevice.getLocalDevice();
//            System.out.println(localDevice);
            LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
        } catch (BluetoothStateException e) {
            fireErrorEvent(e.getMessage());
        }
        eventListeners = new Vector();
    }
    
    /**
     * Add a listener for BTEvents
     * @param listener
     */
    public final void addBTEventListener(final BTEventListener listener) {
        eventListeners.addElement(listener);
    }
    
    /**
     * Remove a listener for BTEvents
     * @param listener
     */
    public final void removeBTEventListener(final BTEventListener listener) {
        eventListeners.removeElement(listener);
    }
    
    protected final void fireDiagnosticEvent(final String source) {
        BTEvent event = new BTEvent(source);
        Enumeration elements = eventListeners.elements();
        while (elements.hasMoreElements()) {
            ((BTEventListener) elements.nextElement()).diagnosticMessage(event);
        }
    }
    
    protected final void fireErrorEvent(final String source) {
        BTEvent event = new BTEvent(source);
        Enumeration elements = eventListeners.elements();
        while (elements.hasMoreElements()) {
            ((BTEventListener) elements.nextElement()).errorMessage(event);
        }
    }
    
    protected final void fireDeviceDiscoveredEvent(final String source) {
        BTEvent event = new BTEvent(source);
        Enumeration elements = eventListeners.elements();
        while (elements.hasMoreElements()) {
//            System.out.println("Discovered "+source);
            ((BTEventListener) elements.nextElement()).devicesDiscovered(event);
        }
    }
    
    protected final void fireMessageSentEvent(final byte[] data) {
//        System.out.println(data.length + "byte(s) recieved");
        BTEvent event = new BTEvent(data);
        Enumeration elements = eventListeners.elements();
        while (elements.hasMoreElements()) {
            ((BTEventListener) elements.nextElement()).messageSent(event);
        }
    }
    
    protected final void fireMessageReceivedEvent(final byte[] data) {
//        System.out.println(data.length + "byte(s) send");
        BTEvent event = new BTEvent(data);
        Enumeration elements = eventListeners.elements();
        while (elements.hasMoreElements()) {
            ((BTEventListener) elements.nextElement()).messageReceived(event);
        }
    }

    /**
     * This method returns a cached copy of devices. If a slightly out of date
     * device list is of no use, make sure to call BTDiscoverer.searchForDevices()
     * @return the cached array of RemoteDevices
     */
    public final RemoteDevice[] getDevices() {
        return localDevice.getDiscoveryAgent().retrieveDevices(
                DiscoveryAgent.CACHED);
    }
}
