package org.flypad.command;

import org.flypad.io.connection.Connection;
import org.flypad.io.connection.DataListener;
import org.flypad.io.connection.Server;
import org.flypad.io.sensor.AccelerometerListener;
import org.flypad.io.sensor.DataCollector;
import org.flypad.util.log.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author albus
 */
public class CommandDispatcher
        implements DataListener, AccelerometerListener {

    private Connection connection;
    private DataCollector collector;

    private final Logger logger;

    public CommandDispatcher(final Logger logger) {
        this.logger = logger;
    }

    public final void initialize() {
        try {
            connection = new Server(this, logger);
        } catch (Throwable t) {
            logger.log(t.getMessage());
        }

        try {
            collector = new DataCollector(this, 150);
            collector.start();
        } catch (Throwable t) {
            logger.log(t.getMessage());
        }
    }

    public final void close() {
        connection.close();
        collector.kill();
    }

    public void receive(final byte[] data) {}

    public void receive(
            final double x,
            final double y,
            final double z) {

        try {
            byte[] data = new byte[33];
            data[0] = Commands.DRIVING_WHEEL;
            writeDouble(x, data, 1);
            writeDouble(y, data, 10);
            writeDouble(z, data, 19);
            
            if (connection != null) {
                connection.send(data);
            }
        } catch (Exception e) {
            logger.log(e);
        }
    }

    private static void writeDouble(
            final double value,
            final byte[] data,
            final int offset) {
        writeLong(Double.doubleToLongBits(value), data, offset);
    }

    private static void writeLong(
            final long value,
            final byte[] data,
            final int offset) {
	data[offset    ] = (byte)((int)(value >>> 56) & 0xFF);
	data[offset + 1] = (byte)((int)(value >>> 48) & 0xFF);
	data[offset + 2] = (byte)((int)(value >>> 40) & 0xFF);
	data[offset + 3] = (byte)((int)(value >>> 32) & 0xFF);
	data[offset + 4] = (byte)((int)(value >>> 24) & 0xFF);
	data[offset + 5] = (byte)((int)(value >>> 16) & 0xFF);
	data[offset + 6] = (byte)((int)(value >>>  8) & 0xFF);
	data[offset + 7] = (byte)((int)(value       ) & 0xFF);
    }
}
