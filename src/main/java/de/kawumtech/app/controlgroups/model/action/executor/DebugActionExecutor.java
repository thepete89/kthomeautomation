package de.kawumtech.app.controlgroups.model.action.executor;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;

public class DebugActionExecutor implements IActionExecutor
{
	
	private Map<String, Object> debugDataMap;
	
	private String[] validKeys = {"test1", "test2", "numeric"};
	
	@Override
	public void execute()
	{
		LoggerFactory.getLogger(this.getClass()).info("--- DEBUG Executor called");
		for (Entry<String, Object> debugDataMapEntry : this.debugDataMap.entrySet())
		{
			LoggerFactory.getLogger(this.getClass()).info("--- DEBUG DATA MAP - KEY: " + debugDataMapEntry.getKey() + " VALUE: " + debugDataMapEntry.getValue().toString());
		}
	}

	@Override
	public void setExecutorDataMap(Map<String, Object> executorDataMap)
	{
		this.debugDataMap = executorDataMap;
	}

	@Override
	public String[] getValidExecutorDataMapKeys()
	{
		return this.validKeys;
	}

	@Override
	public Map<String, Object> getExecutorDataMap()
	{
		return this.debugDataMap;
	}

}
