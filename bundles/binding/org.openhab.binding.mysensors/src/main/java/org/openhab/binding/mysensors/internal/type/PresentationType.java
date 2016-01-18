/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mysensors.internal.type;

import java.util.Arrays;
import java.util.List;

public enum PresentationType {
    S_DOOR("Door and window sensor", ValueType.V_TRIPPED, ValueType.V_ARMED),
    S_MOTION("Motion sensor", ValueType.V_TRIPPED, ValueType.V_ARMED),
    S_SMOKE("Smoke sensor", ValueType.V_TRIPPED, ValueType.V_ARMED),
    S_BINARY("Binary device (on/off)", ValueType.V_STATUS, ValueType.V_WATT),
    S_DIMMER("Dimmable device of some kind", ValueType.V_STATUS, ValueType.V_PERCENTAGE, ValueType.V_WATT),
    S_COVER("Window covers or shades", ValueType.V_UP, ValueType.V_DOWN, ValueType.V_STOP, ValueType.V_PERCENTAGE),
    S_TEMP("Temperature sensor", ValueType.V_TEMP, ValueType.V_ID),
    S_HUM("Humidity sensor", ValueType.V_HUM),
    S_BARO("Barometer sensor (Pressure)", ValueType.V_PRESSURE, ValueType.V_FORECAST),
    S_WIND("Wind sensor", ValueType.V_WIND, ValueType.V_DIRECTION, ValueType.V_GUST),
    S_RAIN("Rain sensor", ValueType.V_RAIN, ValueType.V_RAINRATE),
    S_UV("UV sensor", ValueType.V_UV),
    S_WEIGHT("Weight sensor for scales etc.", ValueType.V_WEIGHT, ValueType.V_IMPEDANCE),
    S_POWER("Power measuring device, like power meters", ValueType.V_WATT, ValueType.V_KWH),
    S_HEATER("Heater device", ValueType.V_HVAC_SETPOINT_HEAT, ValueType.V_HVAC_FLOW_STATE, ValueType.V_TEMP),
    S_DISTANCE("Distance sensor", ValueType.V_DISTANCE, ValueType.V_UNIT_PREFIX),
    S_LIGHT_LEVEL("Light sensor", ValueType.V_LIGHT_LEVEL, ValueType.V_LEVEL),
    S_ARDUINO_NODE("Arduino node device"),
    S_ARDUINO_REPEATER_NODE("Arduino repeating node device"),
    S_LOCK("Lock device", ValueType.V_LOCK_STATUS),
    S_IR("Ir sender/receiver device", ValueType.V_IR_SEND, ValueType.V_IR_RECEIVE, ValueType.V_IR_RECORD),
    S_WATER("Water meter", ValueType.V_FLOW, ValueType.V_VOLUME),
    S_AIR_QUALITY("Air quality sensor e.g. MQ-2", ValueType.V_LEVEL, ValueType.V_UNIT_PREFIX),
    S_CUSTOM("Unknown custom sensor", ValueType.V_VAR1, ValueType.V_VAR2, ValueType.V_VAR3, ValueType.V_VAR4, ValueType.V_VAR5),
    S_DUST("Dust level sensor", ValueType.V_LEVEL, ValueType.V_UNIT_PREFIX),
    S_SCENE_CONTROLLER("Scene controller device", ValueType.V_SCENE_ON, ValueType.V_SCENE_OFF),
    S_RGB_LIGHT("RGB light", ValueType.V_RGB, ValueType.V_WATT),
    S_RGBW_LIGHT("RGBW light (with separate white component)", ValueType.V_RGBW, ValueType.V_WATT),
    S_COLOR_SENSOR("Color sensor", ValueType.V_RGB),
    S_HVAC("Thermostat/HVAC device", ValueType.V_HVAC_SETPOINT_HEAT, ValueType.V_HVAC_SETPOINT_COOL, ValueType.V_HVAC_FLOW_STATE, ValueType.V_HVAC_FLOW_MODE, ValueType.V_HVAC_SPEED),
    S_MULTIMETER("Multimeter device", ValueType.V_VOLTAGE, ValueType.V_CURRENT, ValueType.V_IMPEDANCE),
    S_SPRINKLER("Sprinkler device", ValueType.V_STATUS, ValueType.V_TRIPPED),
    S_WATER_LEAK("Water leak sensor", ValueType.V_TRIPPED, ValueType.V_ARMED),
    S_SOUND("Sound sensor", ValueType.V_LEVEL, ValueType.V_TRIPPED, ValueType.V_ARMED),
    S_VIBRATION("Vibration sensor", ValueType.V_LEVEL, ValueType.V_TRIPPED, ValueType.V_ARMED),
    S_MOISTURE("Moisture sensor", ValueType.V_LEVEL, ValueType.V_TRIPPED, ValueType.V_ARMED),
    S_INFO("LCD text device / Simple information device on controller", ValueType.V_TEXT),
    S_GAS("Gas meter", ValueType.V_FLOW, ValueType.V_VOLUME),
    S_GPS("GPS Sensor", ValueType.V_POSITION);

    private String desc;
    private ValueType[] valueTypes;
    PresentationType(String desc, ValueType...valueTypes) {
        this.desc = desc;
        this.valueTypes = valueTypes;
    }

    public List<ValueType> getValueTypes() {
        return Arrays.asList(valueTypes);
    }

    public String getDescription() {
        return desc;
    }
}
