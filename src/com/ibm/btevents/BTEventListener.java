package com.ibm.btevents;

/**
 * @author bygravec
 * Definition of the methods required to listen for BTEvents
 */
public interface BTEventListener {

    /**
     * Fired when a message is received
     * @param event
     */
    public void messageReceived(BTEvent event);
    
    /**
     * Fired when a message is successfully sent
     * @param event
     */
    public void messageSent(BTEvent event);
    
    /**
     * Fired when new devices are discovered
     * @param event
     */
    public void devicesDiscovered(BTEvent event);
    
    /**
     * Fired when a diagnostic event is received
     * @param event
     */
    public void diagnosticMessage(BTEvent event);
    
    /**
     * Fired when an error occurs
     * @param event
     */
    public void errorMessage(BTEvent event);
    
}
