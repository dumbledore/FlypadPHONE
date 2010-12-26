package com.ibm.btevents;

/**
 * @author bygravec
 * A bluetooth event
 */
public class BTEvent {
    
    private final String message;
    private final byte[] data;
    
    /**
     * Create a new BTEvent
     * @param s
     */
    public BTEvent(final String message) {
        this.message = message;
        this.data = null;
    }

    public BTEvent(final byte[] data) {
        this.message = "No message";
        this.data = data;
    }

    public BTEvent(final String message, final byte[] data) {
        this.message = message;
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }

    public byte[] getData() {
        return data;
    }
}
