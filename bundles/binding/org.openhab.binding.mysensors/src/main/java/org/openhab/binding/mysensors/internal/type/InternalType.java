package org.openhab.binding.mysensors.internal.type;

import java.util.Arrays;
import java.util.List;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

@SuppressWarnings("unchecked")
public enum InternalType {
	I_BATTERY_LEVEL("Use this to report the battery level 0-100 (%)", PercentType.class, DecimalType.class, StringType.class),
    I_TIME("Sensors can request the current time from the Controller using this message. The time will be reported as the seconds since 1970"),
    I_VERSION("Used to request gateway version from controller."),
    I_ID_REQUEST("Use this to request a unique node id from the controller."),
    I_ID_RESPONSE("Id response back to sensor. Payload contains sensor id"),
    I_INCLUSION_MODE("Start/stop inclusion mode of the Controller (1=start, 0=stop)", OnOffType.class, StringType.class, DecimalType.class),
    I_CONFIG("Config request from node. Reply with (M)etric or (I)mperal back to sensor"),
    I_FIND_PARENT("When a sensor starts up, it broadcast a search request to all neighbor nodes. They reply with a I_FIND_PARENT_RESPONSE"),
    I_FIND_PARENT_RESPONSE("Reply message type to I_FIND_PARENT request"),
    I_LOG_MESSAGE("Sent by the gateway to the Controller to trace-log a message", StringType.class),
    I_CHILDREN("A message that can be used to transfer child sensors (from EEPROM routing table) of a repeating node."),
    I_SKETCH_NAME("Sketch name that can be used to identify sensor", StringType.class),
    I_SKETCH_VERSION("Sketch version that can be reported to keep track of the version of sensor", StringType.class),
    I_REBOOT("Used by OTA firmware updates. Request for node to reboot."),
    I_GATEWAY_READY("Send by gateway to controller when startup is complete.", StringType.class),
    I_REQUEST_SIGNING("Used between sensors when initialting signing."),
    I_GET_NONCE("Used between sensors when requesting nonce."),
    I_GET_NONCE_RESPONSE("Used between sensors for nonce response."),
    I_HEARTBEAT(""),
    I_PRESENTATION(""),
    I_DISCOVER(""),
    I_DISCOVER_RESPONSE("");

    private String desc;
    private Class<? extends Command>[] commands;

    InternalType(String desc, Class<? extends Command>...commands) {
        this.desc = desc;
        this.commands = commands;
    }

    public String getDescription() {
        return desc;
    }

    public List<Class<? extends Command>> getCommands() {
        return Arrays.asList(commands);
    }
}