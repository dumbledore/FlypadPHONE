package org.flypad.command;

import org.flypad.flypad.LoggingCanvas;
import org.flypad.io.bluetooth.BluetoothListener;
import org.flypad.io.bluetooth.Connection;
import org.flypad.io.bluetooth.Server;
import org.flypad.io.sensor.AccelerometerListener;
import org.flypad.io.sensor.DataCollector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author albus
 */
public class CommandDispatcher
        implements BluetoothListener, AccelerometerListener {

    private Connection connection;
    private DataCollector collector;
    private final LoggingCanvas logger;

    public CommandDispatcher(final LoggingCanvas logger) {
        this.logger = logger;
    }

    public final synchronized void initializeConnection() {
        if (connection == null) {
            try {
                connection = new Server(this);
            } catch (Throwable t) {
                logger.logError(t.getMessage());
            }
        }
    }

    public final synchronized void initializeCollector() {
        if (collector == null) {
            try {
                collector = new DataCollector(this, 150);
                collector.start();
            } catch (Throwable t) {
                logger.log(t.getMessage());
            }
        }
    }

    public final synchronized void close() {
        closeConnection();
        closeAccelerometer();
    }

    public final synchronized void closeConnection() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public final synchronized void closeAccelerometer() {
        if (collector != null) {
            collector.kill();
            collector = null;
        }
    }

    public final void send(final byte[] data) {
        connection.send(data);
    }

    public void receive(final byte[] data) {}

    public void connected() {
        logger.connected();
    }

    public void lostConnection() {
        logger.lostConnection();
    }

    public void infoMessage(String message) {
        logger.log(message);
    }

    public void errorMessage(String message) {
        logger.logError(message);
    }

     private void sendData(final byte[] data) {
        if (connection != null) {
            connection.send(data);
        }
    }

    public void receive(
            final double x,
            final double y,
            final double z) {

        try {
            byte[] data = new byte[33];
            data[0] = Commands.DRIVING_WHEEL_XYZ_DATA;
            writeDouble(x, data, 1);
            writeDouble(y, data, 10);
            writeDouble(z, data, 19);

            sendData(data);
        } catch (Exception e) {
            logger.logError(e.getMessage());
        }
    }

    public void wheelReset() {
        sendData(new byte[] {Commands.DRIVING_WHEEL_RESET});
    }

    public void wheelAnalog(
            final byte analogNumber,
            final int value,
            final int maxValue) {

        byte[] data = new byte[10];
        data[0] = Commands.DRIVING_WHEEL_ANALOG;
        data[1] = analogNumber;
        writeInt(value, data, 2);
        writeInt(maxValue, data, 6);
        sendData(data);
    }

    public void wheelDigital(final byte digitalNumber) {
        sendData(new byte[] {Commands.DRIVING_WHEEL_DIGITAL, digitalNumber});
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

    private static void writeInt(
            final int value,
            final byte[] data,
            final int offset) {
        data[offset    ] = (byte)(value >>> 24);
        data[offset + 1] = (byte)(value >>> 16);
        data[offset + 2] = (byte)(value >>> 8);
        data[offset + 3] = (byte) value;
    }
}
