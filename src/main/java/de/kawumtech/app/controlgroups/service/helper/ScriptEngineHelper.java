package de.kawumtech.app.controlgroups.service.helper;

import org.slf4j.LoggerFactory;

import de.kawumtech.app.controlgroups.model.actuator.Actuator;
import de.kawumtech.ktha.restlib.actuator.ActuatorClient;
import de.kawumtech.ktha.restlib.api.client.RestConfiguration;

public class ScriptEngineHelper
{
	// TODO maybe make raspi endpoints and ports configurable?
	private static final String DEFAULT_PORT = "8080";
	
	public static void switchActuatorOn(final Actuator actuator)
	{
		LoggerFactory.getLogger(ScriptEngineHelper.class).info("SWITCH_ON: " + actuator.getName());
		RestConfiguration configuration = RestConfiguration.builder().connectTimeout(2000).readTimeout(2000).serviceEndpoint("http://" + actuator.getEndpoint() + ":" + DEFAULT_PORT).build();
		ActuatorClient client = ActuatorClient.getInstance();
		client.init(configuration);
		client.switchOn(actuator.getName());
	}
	
	public static void switchActuatorOff(final Actuator actuator)
	{
		LoggerFactory.getLogger(ScriptEngineHelper.class).info("SWITCH_OFF: " + actuator.getName());
		RestConfiguration configuration = RestConfiguration.builder().connectTimeout(2000).readTimeout(2000).serviceEndpoint("http://" + actuator.getEndpoint() + ":" + DEFAULT_PORT).build();
		ActuatorClient client = ActuatorClient.getInstance();
		client.init(configuration);
		client.switchOff(actuator.getName());
	}
	
	public static void setActuatorValue(final Actuator actuator, final Object value)
	{
		LoggerFactory.getLogger(ScriptEngineHelper.class).info("SET_VALUE: " + actuator.getName() + " : " + value.toString());
		RestConfiguration configuration = RestConfiguration.builder().connectTimeout(2000).readTimeout(2000).serviceEndpoint("http://" + actuator.getEndpoint() + ":" + DEFAULT_PORT).build();
		ActuatorClient client = ActuatorClient.getInstance();
		client.init(configuration);
		client.setValue(actuator.getName(), value);
	}
}
