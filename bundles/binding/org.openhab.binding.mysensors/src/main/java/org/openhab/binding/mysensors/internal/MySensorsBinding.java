/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mysensors.internal;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.openhab.binding.mysensors.MySensorsBindingProvider;
import org.openhab.binding.mysensors.internal.gateway.Ethernet;
import org.openhab.binding.mysensors.internal.gateway.Gateway;
import org.openhab.binding.mysensors.internal.gateway.Serial;
import org.openhab.binding.mysensors.internal.type.InternalType;
import org.openhab.binding.mysensors.internal.type.MessageType;
import org.openhab.binding.mysensors.internal.type.ValueType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * 
 * 
 * @author Bjarne Loft
 * @since 1.8.0
 */
public class MySensorsBinding extends AbstractActiveBinding<MySensorsBindingProvider> implements EventHandler {
	private static final Logger logger = LoggerFactory.getLogger(MySensorsBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;
	
	private Properties prop = new Properties();

	private Gateway gateway;
	
	private boolean iMetric = true;
	
	/** 
	 * the refresh interval which is used to poll values from the MySensors
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private ItemUIRegistry itemUIRegistry;
	
	
	public MySensorsBinding() {
	}
	
	public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = itemUIRegistry;
	}
	
	public void unsetItemUIRegistry(ItemRegistry itemUIRegistry) {
		itemUIRegistry = null;
	}

	public ItemUIRegistry getItemUIRegistry() {
		return itemUIRegistry;
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		logger.debug("activate");
		this.bundleContext = bundleContext;
		
		prop.putAll(configuration);
		
		try {
			refreshInterval = Long.parseLong(prop.getProperty("refresh", "60000"));
			iMetric = Boolean.parseBoolean(prop.getProperty("metric", "true"));
			
			startGateway();
			setProperlyConfigured(true);
		} catch (Exception e) { 
			setProperlyConfigured(false);
		}
	}
	
	private void startGateway() throws NumberFormatException, IOException {
		String type = prop.getProperty("type", "serial");
		if("serial".equalsIgnoreCase(type)) {
			gateway = new Serial(prop.getProperty("port"), Integer.parseInt(prop.getProperty("baudrate", "115200")));
		} else if ("ethernet".equalsIgnoreCase(type)) {
			gateway = new Ethernet(prop.getProperty("host"), Integer.parseInt(prop.getProperty("port", "5003")));
		}
		gateway.setEventHandler(this);
	}
	
	private void stopGateway() {
		gateway.close();
		gateway.setEventHandler(null);
		gateway= null;
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		logger.debug("modified");
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		stopGateway();
		logger.debug("deactivate(" + reason + ")");
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "MySensors Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		if(gateway != null && !gateway.write(new Message(0, 0, MessageType.internal, false, InternalType.I_VERSION, ""))) {
			logger.debug("Ping gateway: Faild, unable to contact gawatay");
			stopGateway();
		} else {
			logger.trace("Ping gateway: All good");
		}
		if(gateway == null) {
			try {
				logger.debug("Starting new gateway");
				startGateway();
			} catch(Exception e) {
				logger.warn("Unable to recover gateway");
				stopGateway();
			}
		}
	}
	
	/**
	 * Lookup provider based on itemName
	 * @param itemName
	 * @return
	 */
	protected MySensorsBindingProvider getProvider(String itemName) {
		for (MySensorsBindingProvider provider : providers) {
			if(provider.providesBindingFor(itemName)) {
				return provider;
			}
		}
		return null;
	}
	
	/**
	 * Lookup provider based on nodeId, sensorId and typeName
	 * @param nodeId
	 * @param sensorId
	 * @param typeName
	 * @return
	 */
	protected MySensorsBindingProvider getProvider(int nodeId, int sensorId, String typeName) {
		for (MySensorsBindingProvider provider : providers) {
			if(provider.getItemName(nodeId, sensorId, typeName) != null) {
				return provider;
			}
		}
		return null;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		if(gateway != null) {
			MySensorsBindingProvider provider = getProvider(itemName);
			if(provider != null && provider.isValueType(itemName) && provider.getCommands(itemName).contains(command.getClass())) {
				String value = stateToValue(command, provider.getItemType(itemName));
				
				gateway.write(new Message(provider.getNodeId(itemName), provider.getSensorId(itemName), MessageType.set, false, ValueType.valueOf(provider.getType(itemName)), value));
			} else {
				logger.debug("Command \"" + command.getClass() + "\" is not a valid Command tyoe: " + provider.getCommands(itemName));
			}
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}


	@Override
	public void incommingMessage(Message message) {
		logger.trace("Incomming message: " + message);
		
		if(message.isPresentation()) {
			logger.info(MessageUtil.toPresentation(message));
		} else if (message.isInternal(InternalType.I_VERSION)) {
			logger.debug("Gateway Version: " + message.getPayload());
        } else if (message.isInternal(InternalType.I_GATEWAY_READY)) {
            logger.info("Gateway Ready");
        } else if (message.isInternal(InternalType.I_LOG_MESSAGE)) {
            logger.debug("I_LOG_MESSAGE: " + message.getPayload());
        } else if (message.isInternal(InternalType.I_TIME)) {
            gateway.write(MessageUtil.getResponse(message, System.currentTimeMillis() / 1000));
        } else if (message.isInternal(InternalType.I_CONFIG)) {
            gateway.write(MessageUtil.getResponse(message, iMetric ? "M" : "I"));
        } else if (message.isInternal(InternalType.I_ID_REQUEST)) {
            // ToDo: Auto assign ID's of nodes
            // int maxId = 0;
            // Lookup using I_VERSION on all nodes
            // gateway.write(MessageUtil.getResponse(msg, maxId));
        } else {
			MySensorsBindingProvider provider = getProvider(message.getNodeId(), message.getSensorId(), message.getSubTypeAsString());
			if(provider != null) {
				String itemName = provider.getItemName(message.getNodeId(), message.getSensorId(), message.getSubTypeAsString());
				if(message.isRep()) {
					logger.debug("Incomming request: " + message.toString());
					Item item = getItem(itemName);
					if(item != null) {
						gateway.write(MessageUtil.getResponse(message, stateToValue(item.getState(), provider.getItemType(itemName))));
					}
				} else {
					Type content = null;
					Class<? extends Command> cClass = provider.getCommands(itemName).get(0);
					if(cClass == OnOffType.class) {
						content = "1".equals(message.getPayload()) ? OnOffType.ON : OnOffType.OFF;
					} else if(cClass == OpenClosedType.class) {
						content = "1".equals(message.getPayload()) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
					} else if(cClass == HSBType.class) {
						content = new HSBType(Color.decode("#" + message.getPayload()));
					} else if (cClass == PointType.class) {
						content = new PointType(message.getPayload().replace(";", ","));
					} else {
						try {
							content = cClass.getConstructor(String.class).newInstance(message.getPayload());
						} catch (Exception e) {
							logger.warn("Unable to create new instace of " + cClass.getSimpleName());
						}
					}
					
					if(content != null) {
						eventPublisher.postUpdate(itemName, (State)content);
						//eventPublisher.postCommand(itemName, (Command)content);
					}
					
					logger.debug(itemName + " = " + content);
					
					if(message.getAck()) {
			            gateway.write(MessageUtil.getResponse(message, message.getPayload()));
			        }
					
					if(message.getAck()) {
						gateway.write(MessageUtil.getResponse(message, message.getPayload()));
					}
				}
			} else {
				logger.info(String.format("No item for \"%s;%s;%s\"", message.getNodeId(), message.getSensorId(), message.getSubTypeAsString()));
			}
        }
	}
	
	private String stateToValue(Type command, Class<? extends Item> itemtype) {
		String value = command.toString();
		if(command instanceof HSBType) {
			Color color = ((HSBType)command).toColor();
			value = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
		} else if(command instanceof PointType) {
            PointType point = (PointType) command;
            value = String.format("%s;%s;%s", point.getLatitude().toBigDecimal(), point.getLongitude().toBigDecimal(), point.getAltitude().toBigDecimal());
        } else if(command instanceof DateTimeType) {
            value = Long.toString(((DateTimeType) command).getCalendar().getTimeInMillis() / 1000);
		} else if (command instanceof OnOffType) {
			if(ColorItem.class.isAssignableFrom(itemtype)) {
				value = command == OnOffType.ON ? "ffffff" : "000000";
			} else {
				value = command == OnOffType.ON ? "1" : "0";
			}
		} else if (command instanceof OpenClosedType) {
			value = command == OpenClosedType.OPEN ? "1" : "0";
		}
		return value;
	}
	
	private Item getItem(String itemname) {
		if(itemUIRegistry != null) {
			try {
				return itemUIRegistry.getItem(itemname);
			} catch (ItemNotFoundException e) {
				logger.debug(e.getMessage());
			}
		}
		return null;
	}

}
