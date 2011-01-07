/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.sensor;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.sensor.Data;
import javax.microedition.sensor.SensorConnection;
import javax.microedition.sensor.SensorInfo;
import javax.microedition.sensor.SensorManager;
import org.flypad.util.SimpleThread;

/**
 *
 * @author albus
 */
public class DataCollector extends SimpleThread{
    private SensorConnection sensor;
    private final AccelerometerListener listener;
    private final int sleepingTime;

    public DataCollector(
            final AccelerometerListener listener,
            final int sleepingTime)
            throws IOException {

        this.sleepingTime = sleepingTime;
        this.listener = listener;

        findSensor();
    }

    private void findSensor() throws IOException {
        SensorInfo[] sensors = SensorManager.findSensors("acceleration", null);
        if (sensors.length > 0) {
            this.sensor =
                    (SensorConnection) Connector.open(sensors[0].getUrl());
        }
    }

    public void run() {
        try {
            Data[] data;
            double x, y, z;

            try {
                while (isWorking()) {
                    try {
                        data = sensor.getData(1, 0, true, false, false);
                        x = (data[0].getDoubleValues()[0]);
                        y = (data[1].getDoubleValues()[0]);
                        z = (data[2].getDoubleValues()[0]);

                        listener.receive(x, y, z);
                     } catch (Exception e) {
                     }
                    sleep(sleepingTime);
                }
            } finally {
                try {
                    sensor.close();
                } catch (IOException e) {}
            }
        } catch (InterruptedException e) {}
    }
}
