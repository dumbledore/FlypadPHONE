/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;
import org.flypad.util.DataQueue;

/**
 *
 * @author albus
 */
public class Transmission extends Thread {
    private StreamConnection connection;
    private volatile boolean alive = true;
    private DataQueue queue = new DataQueue(128);

    public Transmission(final StreamConnection connection) {
        this.connection = connection;
    }

    public final void run() {
        try {
            /*
             * Start sending data
             */
            DataOutputStream send = connection.openDataOutputStream();

//            logger.log("Start sending data...");

            try {
                while(alive) {
                    if (!queue.isEmpty()) {
                        byte[] data = queue.dequeue();
                        if (data != null) {
//                            logger.log("[SENDING]: "
//                                    + new String(data));
                            send.writeShort((short) data.length);
                            send.write(data);
                            send.flush();
                        }
                    } else {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {}
                    }
                }
            } finally {
                send.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void send(final byte[] data) {
//        logger.log("[ENQUEUE]: "
//                + new String(data));

        queue.enqueue(data);
    }

    public final void kill() {
        alive = false;
    }
}
