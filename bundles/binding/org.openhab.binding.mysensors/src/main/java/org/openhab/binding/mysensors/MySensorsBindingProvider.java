/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mysensors;

import java.util.List;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * @author Bjarne Loft
 * @since 1.8.0
 */
public interface MySensorsBindingProvider extends BindingProvider {
	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
	
	List<Class<? extends Command>> getCommands(String itemName);
	
	int getNodeId(String itemName);
	
	int getSensorId(String itemName);
	
	boolean isValueType(String itemName);
	
	String getType(String itemName);
	
	String getItemName(int nodeId, int sensorId, String type);
}
