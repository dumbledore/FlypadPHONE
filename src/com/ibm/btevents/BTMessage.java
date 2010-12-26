package com.ibm.btevents;

import javax.bluetooth.RemoteDevice;

/**
 * @author bygravec
 * Encapsulation of a bluetooth message
 */
public class BTMessage {

    private RemoteDevice remoteDevice;
    private byte[] message;
    
    private int retries;
    
    /**
     * Create a new BTMessage with the provided parameters.
     */
    public BTMessage(
            final RemoteDevice remoteDevice,
            final byte[] message,
            int retries) {
        this.remoteDevice = remoteDevice;
        this.message = message;
        this.retries = retries;
    }

    /**
     * @return Returns the message.
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * @return Returns the remote.
     */
    public RemoteDevice getRemote() {
        return remoteDevice;
    }

    /**
     * Remove one from the retries count.
     */
    public void decrementRetries() {
        if (retries > 0) retries--;
    }

    /**
     * @return true, if the object still has a positive retry count.
     */
    public boolean isLive() {
        return (retries > 0);
    }
}
