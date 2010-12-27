/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.flypad.util.DataQueue;
import org.flypad.util.Logger;

/**
 *
 * @author albus
 */
public class Transmission extends Thread {
    private final DiscoveryAgent discoveryAgent;
    private final UUID uuid;

    private StreamConnection server;
    private volatile boolean alive = true;

    private DataQueue queue = new DataQueue(128);

    private final Logger logger;

    public Transmission(
            final DiscoveryAgent discoveryAgent,
            final UUID uuid,
            final Logger logger
            ) {

        this.discoveryAgent = discoveryAgent;
        this.uuid = uuid;
        this.logger = logger;
    }

    private void connect() throws IOException {
        logger.log("Searching for host...");
        final String url = discoveryAgent.selectService(
                uuid,
                ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                false);

        if (url == null) {
            throw new IOException("Couldn't find host");
        }

        logger.log("Host found. Connecting...");
        logger.log(url);

        server = (StreamConnection) Connector.open(url);
        logger.log("Connected!");
    }

    public final void run() {
        try {
            /*
             * Connect to server
             */
            connect();
            
            /*
             * Start sending data
             */
            DataOutputStream send = server.openDataOutputStream();

            logger.log("Start sending data...");

            try {
                while(alive) {
                    if (!queue.isEmpty()) {
                        byte[] data = queue.dequeue();
                        if (data != null) {
                            logger.log("[SENDING]: "
                                    + new String(data));
                            send.writeShort((short) data.length);
                            send.write(data);
                            send.flush();
                        }
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
        logger.log("[ENQUEUE]: "
                + new String(data));

        queue.enqueue(data);
    }

    public final void kill() {
        alive = false;
    }
}
