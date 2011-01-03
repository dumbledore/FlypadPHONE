/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
public class Reception extends Thread {
    
    private final StreamConnection connection;
    private final DataListener receiver;
    
    private volatile boolean alive = true;

    public Reception(
            final StreamConnection connection,
            final DataListener receiver
            ) {
        this.connection = connection;
        this.receiver = receiver;
    }

    public final void run() {
        try {
            /*
             * Start receiving data
             */
            int size;
            byte[] buffer;
            long t;
            DataInputStream receive = connection.openDataInputStream();
            try {
                while (alive) {
                    size = receive.readShort();
//                    t = System.currentTimeMillis();
                    buffer = new byte[size];
                    receive.readFully(buffer);
//                    t = System.currentTimeMillis() - t;
//                    System.out.println("[Recieved]: "
//                            + new String(buffer)
//                            + " (" + t + ")");
                    receiver.receive(buffer);
                }
            } finally {
                System.out.println("Closing connection...");
                receive.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public final void kill() {
        alive = false;
    }
}
