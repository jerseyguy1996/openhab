/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mysensors.internal;
import java.util.Arrays;
import java.util.List;

import org.openhab.binding.mysensors.internal.type.InternalType;
import org.openhab.binding.mysensors.internal.type.MessageType;
import org.openhab.binding.mysensors.internal.type.PresentationType;
import org.openhab.binding.mysensors.internal.type.StreamType;
import org.openhab.binding.mysensors.internal.type.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
node-id;child-sensor-id;message-type;ack;sub-type;payload
 */
public class Message {
    public enum SubType {}
    private static Logger logger = LoggerFactory.getLogger(Message.class);
    List<String> data;

    public Message(String content) {
        data = Arrays.asList(content.split(";", 6));
    }

    public Message(int nodeId, int sensorId, MessageType type, boolean ack, PresentationType subtype, String payload) {
        this(nodeId, sensorId, type, ack, subtype.ordinal(), payload);
    }

    public Message(int nodeId, int sensorId, MessageType type, boolean ack, ValueType subtype, String payload) {
        this(nodeId, sensorId, type, ack, subtype.ordinal(), payload);
    }

    public Message(int nodeId, int sensorId, MessageType type, boolean ack, InternalType subtype, String payload) {
        this(nodeId, sensorId, type, ack, subtype.ordinal(), payload);
    }

    public Message(int nodeId, int sensorId, MessageType type, boolean ack, int subtype, String payload) {
        data = Arrays.asList(String.valueOf(nodeId), String.valueOf(sensorId), String.valueOf(type.ordinal()), (ack ? "1" : "0"), String.valueOf(subtype), payload);
    }

    // Present data
    public int getNodeId() { return Integer.parseInt(data.get(0)); }
    public int getSensorId() { return Integer.parseInt(data.get(1)); }
    public MessageType getType() { return MessageType.values()[Integer.parseInt(data.get(2))]; }
    public boolean getAck() { return "1".equals(data.get(3)); }
    public int getSubType() { return Integer.parseInt(data.get(4)); }
    public String getPayload() { return data.get(5); }

    public <E extends Enum<E>> E getSubType(Class<E> enumClass) {
        return enumClass.getEnumConstants()[getSubType()];
    }

    public String getSubTypeAsString() {
        switch(getType()) {
            case presentation:  return getSubType(PresentationType.class).name();
            case set:           return getSubType(ValueType.class).name();
            case req:           return getSubType(ValueType.class).name();
            case internal:      return getSubType(InternalType.class).name();
            case stream:        return getSubType(StreamType.class).name();
        }
        return "Unknown";
    }

    public boolean isPresentation() { return getType() == MessageType.presentation; }
    public boolean isSet() { return getType() == MessageType.set; }
    public boolean isRep() { return getType() == MessageType.req; }
    public boolean isInternal() { return getType() == MessageType.internal; }
    public boolean isStream() { return getType() == MessageType.stream; }

    public boolean isPresentation(PresentationType type) {
        return getType() == MessageType.presentation && getSubType(PresentationType.class) == type;
    }

    public boolean isSet(ValueType type) {
        return getType() == MessageType.set && getSubType(ValueType.class) == type;
    }

    public boolean isRep(ValueType type) {
        return getType() == MessageType.req && getSubType(ValueType.class) == type;
    }

    public boolean isInternal(InternalType type) {
        return getType() == MessageType.internal && getSubType(InternalType.class) == type;
    }

    public boolean isStream(StreamType type) {
        return getType() == MessageType.stream && getSubType(StreamType.class) == type;
    }

    public String toMessage() {
        return String.format("%s;%s;%s;%s;%s;%s\n", getNodeId(), getSensorId(), getType().ordinal(), getAck() ? 1 : 0, getSubType(), getPayload());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("node-id=").append(getNodeId());
        sb.append(", child-sensor-id=").append(getSensorId());
        sb.append(", message-type=").append(getType());
        sb.append(", ack=").append(getAck());
        sb.append(", sub-type=").append(getSubTypeAsString());
        sb.append(", payload=").append(getPayload());
        return sb.toString();
    }
}