/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mysensors.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mysensors.MySensorsBindingProvider;
import org.openhab.binding.mysensors.internal.type.InternalType;
import org.openhab.binding.mysensors.internal.type.ValueType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Bjarne Loft
 * @since 1.8.0
 */
public class MySensorsGenericBindingProvider extends AbstractGenericBindingProvider implements MySensorsBindingProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(MySensorsGenericBindingProvider.class);
	
	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern CONFIG_PATTERN = Pattern.compile("([0-9]{1,3});([0-9]{1,3});([VI]_[A-Z1-9_]+)");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "mysensors";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

        if( !matcher.matches()) {
        	throw new BindingConfigParseException("Unable to parse \"" + bindingConfig + "\" need to be in the format <number>;<number>;<type>");
        }

        if(getPossibleCommands(matcher.group(3), item).isEmpty()) {
            throw new BindingConfigParseException("Unsupported ItemType: " + item.getClass().getSimpleName().replace("Item", ""));
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		MySensorsBindingConfig config = new MySensorsBindingConfig();
		config.itemType = item.getClass();
		
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);
		
		if (!matcher.matches()) {
			throw new BindingConfigParseException("Unable to parse \"" + bindingConfig + "\" need to be in the format <number>;<number>;<type>");
		} else {
			config.nodeId = Integer.parseInt(matcher.group(1));
			config.sensorId = Integer.parseInt(matcher.group(2));
			config.typeName = matcher.group(3);
			config.commands = getPossibleCommands(matcher.group(3), item);
		}
		
		logger.debug("New Item \"" + item + "\" based on configuration \"" + bindingConfig + "\"");
		
		addBindingConfig(item, config);		
	}
	
	private List<Class<? extends Command>> getPossibleCommands(String type, Item item) throws BindingConfigParseException {
        List<Class<? extends Command>> commands = new ArrayList<Class<? extends Command>>(item.getAcceptedCommandTypes());
        try {
            if (type.startsWith("V_")) {
                commands.retainAll(ValueType.valueOf(type).getCommands());
            } else if (type.startsWith("I_")) {
                commands.retainAll(InternalType.valueOf(type).getCommands());
            } else {
            	throw new BindingConfigParseException("Invalid Type: " + type);
            }
        } catch(IllegalArgumentException e) {
        	throw new BindingConfigParseException("Invalid Type: " + type);
        }

        return commands;
    }
	
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		MySensorsBindingConfig config = (MySensorsBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}
	
	@Override
	public List<Class<? extends Command>> getCommands(String itemName) {
		MySensorsBindingConfig config = (MySensorsBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.commands : new ArrayList<Class<? extends Command>>();
	}
	
	@Override
	public int getNodeId(String itemName) {
		MySensorsBindingConfig config = (MySensorsBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.nodeId : -1;
	}
	
	@Override
	public int getSensorId(String itemName) {
		MySensorsBindingConfig config = (MySensorsBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.sensorId : -1;
	}
	
	@Override
	public boolean isValueType(String itemName) {
		return getType(itemName).startsWith("V_");
	}
	
	@Override
	public String getType(String itemName) {
		MySensorsBindingConfig config = (MySensorsBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.typeName : "";
	}
	
	@Override
	public String getItemName(int nodeId, int sensorId, String type) {
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			if(entry.getValue() instanceof MySensorsBindingConfig) {
				MySensorsBindingConfig cfg = (MySensorsBindingConfig) entry.getValue();
				if(cfg.nodeId == nodeId && cfg.sensorId == sensorId && cfg.typeName.equalsIgnoreCase(type)) {
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Bjarne Loft
	 * @since 1.8.0
	 */
	class MySensorsBindingConfig implements BindingConfig {
		Class<? extends Item> itemType;
		List<Class<? extends Command>> commands;
		int nodeId;
		int sensorId;
		String typeName;
	}
}
