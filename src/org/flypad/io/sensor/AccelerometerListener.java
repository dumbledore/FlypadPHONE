/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.sensor;

/**
 *
 * @author albus
 */
public interface AccelerometerListener {
    public void receive(double x, double y, double z);
}
