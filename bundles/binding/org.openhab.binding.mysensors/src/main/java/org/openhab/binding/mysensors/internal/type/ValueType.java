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

import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

@SuppressWarnings("unchecked")
public enum ValueType {
    V_TEMP("Temperature", DecimalType.class, StringType.class),
    V_HUM("Humidity", DecimalType.class, StringType.class),
    V_STATUS("Binary status. (on/off)", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_PERCENTAGE("Percentage value. 0-100 (%)", PercentType.class, DecimalType.class, StringType.class),
    V_PRESSURE("Atmospheric Pressure", DecimalType.class, StringType.class),
    V_FORECAST("Weather forecast. One of \"stable\", \"sunny\", \"cloudy\", \"unstable\", \"thunderstorm\" or \"unknown\"", StringType.class),
    V_RAIN("Amount of rain", DecimalType.class, StringType.class),
    V_RAINRATE("Rate of rain", DecimalType.class, StringType.class),
    V_WIND("Windspeed", DecimalType.class, StringType.class),
    V_GUST("Gust", DecimalType.class, StringType.class),
    V_DIRECTION("Wind direction", DecimalType.class, StringType.class),
    V_UV("UV light level", DecimalType.class, StringType.class),
    V_WEIGHT("Weight", DecimalType.class, StringType.class),
    V_DISTANCE("Distance", DecimalType.class, StringType.class),
    V_IMPEDANCE("Impedance value", DecimalType.class, StringType.class),
    V_ARMED("Armed status of a security sensor. (Armed/Bypassed)", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_TRIPPED("Tripped status of a security sensor. (Tripped/Untripped)", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_WATT("Watt value for power meters", DecimalType.class, StringType.class),
    V_KWH("Accumulated number of KWH for a power meter", DecimalType.class, StringType.class),
    V_SCENE_ON("Turn on a scene", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_SCENE_OFF("Turn of a scene", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_HVAC_FLOW_STATE("Mode of header. One of \"Off\", \"HeatOn\", \"CoolOn\", or \"AutoChangeOver\"", StringType.class),
    V_HVAC_SPEED("HVAC/Heater fan speed (\"Min\", \"Normal\", \"Max\", \"Auto\")", StringType.class),
    V_LIGHT_LEVEL("Uncalibrated light level. 0-100 (%)", PercentType.class, DecimalType.class, StringType.class),
    V_VAR1("Custom value", PointType.class, DateTimeType.class, IncreaseDecreaseType.class, PercentType.class, OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_VAR2("Custom value", PointType.class, DateTimeType.class, IncreaseDecreaseType.class, PercentType.class, OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_VAR3("Custom value", PointType.class, DateTimeType.class, IncreaseDecreaseType.class, PercentType.class, OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_VAR4("Custom value", PointType.class, DateTimeType.class, IncreaseDecreaseType.class, PercentType.class, OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_VAR5("Custom value", PointType.class, DateTimeType.class, IncreaseDecreaseType.class, PercentType.class, OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_UP("Window covering. Up.", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_DOWN("Window covering. Down.", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_STOP("Window covering. Stop.", OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_IR_SEND("Send out an IR-command", StringType.class),
    V_IR_RECEIVE("This message contains a received IR-command", StringType.class),
    V_FLOW("Flow of water (in meter)", DecimalType.class, StringType.class),
    V_VOLUME("Water volume", DecimalType.class, StringType.class),
    V_LOCK_STATUS("Set or get lock status. (Locked/Unlocked)", OnOffType.class, DecimalType.class, StringType.class),
    V_LEVEL("Used for sending level-value", PercentType.class, DecimalType.class, StringType.class),
    V_VOLTAGE("Voltage level", PercentType.class, DecimalType.class, StringType.class),
    V_CURRENT("Current level", PercentType.class, DecimalType.class, StringType.class),
    V_RGB("RGB value transmitted as ASCII hex string (I.e \"ff0000\" for red)", OnOffType.class, HSBType.class, StringType.class),
    V_RGBW("RGBW value transmitted as ASCII hex string (I.e \"ff0000ff\" for red + full white)", OnOffType.class, PercentType.class, HSBType.class, StringType.class),
    V_ID("Optional unique sensor id (e.g. OneWire DS1820b ids)", DecimalType.class, StringType.class),
    V_UNIT_PREFIX("Allows sensors to send in a string representing the unit prefix to be displayed in GUI. This is not parsed by controller! E.g. cm, m, km, inch.", StringType.class),
    V_HVAC_SETPOINT_COOL("HVAC cold setpoint"),
    V_HVAC_SETPOINT_HEAT("HVAC/Heater setpoint"),
    V_HVAC_FLOW_MODE("Flow mode for HVAC (\"Auto\", \"ContinuousOn\", \"PeriodicOn\")", StringType.class),
    V_TEXT("Text message to display on LCD or controller device", StringType.class),
    V_CUSTOM("Custom messages used for controller/inter node specific commands", PointType.class, DateTimeType.class, IncreaseDecreaseType.class, PercentType.class, OpenClosedType.class, OnOffType.class, DecimalType.class, StringType.class),
    V_POSITION("GPS position and altitude. Payload: latitude;longitude;altitude(m). E.g. \"55.722526;13.017972;18\"", PointType.class, StringType.class),
    V_IR_RECORD("Record IR codes");

    private String desc;
    private Class<? extends Command>[] commands;

    ValueType(String desc, Class<? extends Command>...commands) {
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
