package org.openhab.binding.mysensors.internal;

public interface EventHandler {
    void incommingMessage(Message message);
}
