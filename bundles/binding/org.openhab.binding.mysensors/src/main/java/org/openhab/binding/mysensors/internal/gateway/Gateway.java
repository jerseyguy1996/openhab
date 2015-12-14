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
