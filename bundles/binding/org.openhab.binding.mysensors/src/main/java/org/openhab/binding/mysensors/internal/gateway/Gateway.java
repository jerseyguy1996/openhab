/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mysensors.internal.gateway;

import org.openhab.binding.mysensors.internal.EventHandler;
import org.openhab.binding.mysensors.internal.Message;

public abstract class Gateway {
    private EventHandler eventHandler;

    public abstract void write(Message message);

    public abstract void close();

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    protected void handle(Message message) {
        eventHandler.incommingMessage(message);
    }
}
