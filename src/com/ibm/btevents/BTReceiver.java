package com.ibm.btevents;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * @author bygravec
 * Thread tasked with listening on a socket for incoming messages
 */
public class BTReceiver extends BTThread {
    
    /**
     * Listen for incoming messages and alert listeners when messages are received
     * @see java.lang.Runnable#run()
     */
    public void run() {
        StreamConnectionNotifier server = null;
        try {
            server = (StreamConnectionNotifier)Connector.open(
                    BT_PROTOCOL + "://localhost:" + BT_ID);
        } catch (IOException e) {
            fireErrorEvent(e.getMessage());
        }

        StreamConnection clientConnection;
        ByteArrayOutputStream container = new ByteArrayOutputStream(2048);
        byte[] buffer = new byte[512];
        byte[] recieved;
        int read;
        
        while (true) {
            DataInputStream in = null;
            try {
                clientConnection = server.acceptAndOpen();
                in = clientConnection.openDataInputStream();
            } catch (IOException e) {
                fireErrorEvent(e.getMessage());
            }
            try {
                container.reset();
                while ((read = in.read(buffer)) > 0) {
                    container.write(buffer, 0, read);
                }
                recieved = container.toByteArray();
                fireMessageReceivedEvent(recieved);
            } catch (IOException e) {
                fireErrorEvent(e.getMessage());
            }
        }
    }
}
