/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mysensors.internal.gateway;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.openhab.binding.mysensors.internal.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Serial extends Gateway implements SerialPortEventListener {
    private static Logger logger = LoggerFactory.getLogger(Serial.class);

    private CommPortIdentifier portId;
    private SerialPort serialPort;

    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean failing = false;

    public Serial(String port, int baud) {
        Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (id.getName().equals(port)) {
                    logger.debug("Serial port '{}' has been found.", port);
                    portId = id;
                }
            }
        }
        if (portId != null) {
            try {
                serialPort = (SerialPort) portId.open("openHAB", 2000);
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
                serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            } catch (PortInUseException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("Unable to find Serial port '{}'", port);
        }
    }

    public boolean isFailing() {
        return failing;
    }

    @Override
    public void write(Message message) {
    	logger.trace("Writing: " + message.toMessage());
        try {
            outputStream.write(message.toMessage().getBytes());
            outputStream.flush();
            failing = false;
        } catch (IOException e) {
            failing = true;
            logger.debug("Error writing data on serial port {}: {}", serialPort.getName(), e.getMessage());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            StringBuilder sb = new StringBuilder();
            byte[] readBuffer = new byte[20];
            try {
                do {
                    // read data from serial device
                    while (inputStream.available() > 0) {
                        int bytes = inputStream.read(readBuffer);
                        sb.append(new String(readBuffer, 0, bytes));
                    }
                    try {
                        // add wait states around reading the stream, so that interrupted transmissions are merged
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // ignore interruption
                    }
                } while (inputStream.available() > 0);

                for (String event : sb.toString().split("\n")) {
                	logger.trace("Recieved: " + event);
                    handle(new Message(event));
                }
                failing = false;
            } catch (IOException e) {
                failing = true;
                logger.debug("Error receiving data on serial port {}: {}", serialPort.getName(), e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        serialPort.removeEventListener();
        try {
            inputStream.close();
        } catch (IOException e) {}
        try {
            outputStream.close();
        } catch (IOException e) {}
        serialPort.close();
    }
}