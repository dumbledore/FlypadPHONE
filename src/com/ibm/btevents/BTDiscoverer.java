package com.ibm.btevents;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * @author bygravec
 * Thread tasked with discovering remote devices
 */
public class BTDiscoverer extends BTThread implements DiscoveryListener {

    private boolean searchRequested;
    
    /**
     * Create a new BTDiscoverer
     */
    public BTDiscoverer() {
        searchRequested = true;
    }
    
    /**
     * carry out searches when they are requested
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            if (searchRequested) {
                searchRequested = false;
                try {
                    localDevice.getDiscoveryAgent().startInquiry(
                            DiscoveryAgent.GIAC, this);
                } catch (BluetoothStateException e) {
                    fireErrorEvent(e.getMessage());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    
    /**
     * Start a new search for devices
     */
    public void searchForDevices() {
        searchRequested = true;
    }
    
    /**
     * @see javax.bluetooth.DiscoveryListener#deviceDiscovered(javax.bluetooth.RemoteDevice, javax.bluetooth.DeviceClass)
     */
    public void deviceDiscovered(
            final RemoteDevice device,
            final DeviceClass dc) {

        try {
//            System.out.println(device.getFriendlyName(false));
            fireDeviceDiscoveredEvent(
                    "Device discovered: " + device.getFriendlyName(false));
        } catch (IOException e) {
            fireErrorEvent(e.getMessage());
        }
    }
    
    /**
     * @see javax.bluetooth.DiscoveryListener#servicesDiscovered(int, javax.bluetooth.ServiceRecord[])
     */
    public void servicesDiscovered(int arg0, ServiceRecord[] records) {
    }
    
    /**
     * @see javax.bluetooth.DiscoveryListener#serviceSearchCompleted(int, int)
     */
    public void serviceSearchCompleted(int arg0, int arg1) {
    }

    /**
     * @see javax.bluetooth.DiscoveryListener#inquiryCompleted(int)
     */
    public void inquiryCompleted(int arg0) {
        fireDiagnosticEvent("Completed search");
    }
}