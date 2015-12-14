package org.openhab.binding.mysensors.internal;

import java.awt.Color;

import org.openhab.binding.mysensors.internal.type.InternalType;
import org.openhab.binding.mysensors.internal.type.MessageType;
import org.openhab.binding.mysensors.internal.type.PresentationType;
import org.openhab.binding.mysensors.internal.type.ValueType;


public class MessageUtil {
    public static Message getResponse(Message msg, Object payload) {
        MessageType type = msg.getType();
        int subType = msg.getSubType();
        if(msg.isInternal() && msg.getSubType(InternalType.class) == InternalType.I_ID_REQUEST) {
            subType = InternalType.I_ID_RESPONSE.ordinal();
        }
        if(msg.isRep()) {
            type = MessageType.set;
        }
        return new Message(msg.getNodeId(), msg.getSensorId(), type, false, subType, payload.toString());
    }

    public static String toPresentation(Message msg) {
        if(!msg.isPresentation()) return null;
        StringBuilder sb = new StringBuilder();
        if(msg.getSubType(PresentationType.class) == PresentationType.S_ARDUINO_NODE || msg.getSubType(PresentationType.class) == PresentationType.S_ARDUINO_REPEATER_NODE) {
            sb.append("New MySensor node found: node-id=");
            sb.append(msg.getNodeId());
            if(msg.getSubType(PresentationType.class) == PresentationType.S_ARDUINO_REPEATER_NODE) {
                sb.append(" ( REPEATER )");
            }
            nodeExample(sb, msg, InternalType.I_BATTERY_LEVEL);
            nodeExample(sb, msg, InternalType.I_SKETCH_NAME);
            nodeExample(sb, msg, InternalType.I_SKETCH_VERSION);
            nodeExample(sb, msg.getNodeId(), 0, InternalType.I_INCLUSION_MODE);
        } else {
            sb.append("New MySensor sensor found (");
            if(msg.getPayload().isEmpty()) {
                sb.append(msg.getSubType(PresentationType.class).getDescription());
            } else {
                sb.append(msg.getPayload());
            }
            sb.append("): ");
            sb.append("node-id=").append(msg.getNodeId());
            sb.append(", sensor-id=").append(msg.getSensorId());
            sb.append(" with type ").append(msg.getSubTypeAsString());
            for (ValueType vType : msg.getSubType(PresentationType.class).getValueTypes()) {
                itemExample(sb, msg, vType.name(), vType.getDescription());
            }
        }

        return sb.toString();
    }

    public static String colorToPayload(Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    private static void nodeExample(StringBuilder sb, int nodeId, int sensorId, InternalType type) {
        itemExample(sb, nodeId, sensorId, type.name(), type.getDescription());
    }

    private static void nodeExample(StringBuilder sb, Message msg, InternalType type) {
        itemExample(sb, msg, type.name(), type.getDescription());
    }

    private static void itemExample(StringBuilder sb, Message msg, String type, String desc) {
    	itemExample(sb, msg.getNodeId(), msg.getSensorId(), type, desc);
    }
    
    private static void itemExample(StringBuilder sb, int nodeId, int sensorId, String type, String desc) {
        sb.append(String.format("\n * Example item: %s;%s;%-20s - %s", nodeId, sensorId, type, desc));
    }

}