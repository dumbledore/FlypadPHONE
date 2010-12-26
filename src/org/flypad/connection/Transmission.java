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

    public Transmission(
            final DiscoveryAgent discoveryAgent,
            final UUID uuid
            ) {
        this.discoveryAgent = discoveryAgent;
        this.uuid = uuid;
    }

    private void connect() throws IOException {
        System.out.println("Searching for host...");
        final String url = discoveryAgent.selectService(
                uuid,
                ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                false);

        if (url == null) {
            throw new IOException("Couldn't find host");
        }

        System.out.println("Host found. Connecting...");

        server = (StreamConnection) Connector.open(url);
        System.out.println("Connected!");
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

            System.out.println("Start sending data...");

            try {
                while(alive) {
                    if (!queue.isEmpty()) {
                        byte[] data = queue.dequeue();
                        if (data != null) {
                            System.out.println("[SENDING]: "
                                    + new String(data));
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
        System.out.println("[ENQUEUE]: "
                + new String(data));

        queue.enqueue(data);
    }

    public final void kill() {
        alive = false;
    }
}
