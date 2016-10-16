package de.kawumtech.app.controlgroups.service;

import java.util.List;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.kawumtech.app.controlgroups.model.actuator.Actuator;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;

@Service
public class ScriptService
{
	
	private final ScriptEngineManager engineManager = new ScriptEngineManager();
	
	public void runScript(final String script, final List<Actuator> actuators, final List<Sensor> sensors)
	{
		StringBuilder evaledScript = new StringBuilder();
		evaledScript.append("var ScriptEngineHelper = Java.type('de.kawumtech.app.controlgroups.service.helper.ScriptEngineHelper');");
		evaledScript.append(script);
		ScriptEngine engine = this.engineManager.getEngineByName("nashorn");
		ScriptContext context = engine.getContext();
		for (Sensor sensor : sensors)
		{
			context.setAttribute(sensor.getSensorName(), sensor.getSensorValue(), ScriptContext.ENGINE_SCOPE);
		}
		for (Actuator actuator : actuators)
		{
			context.setAttribute(actuator.getName(), actuator, ScriptContext.ENGINE_SCOPE);
		}
		engine.setContext(context);
		try
		{
			engine.eval(evaledScript.toString());
		} catch (ScriptException e)
		{
			LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
		}
	}
}
