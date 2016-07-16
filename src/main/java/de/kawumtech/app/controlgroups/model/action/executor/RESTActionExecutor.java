package de.kawumtech.app.controlgroups.model.action.executor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;


public class RESTActionExecutor implements IActionExecutor
{

	private String restEndpoint;
	
	private String[] validKeys = {"restEndpoint"};
		
	@Override
	public void execute()
	{
		// TODO
		LoggerFactory.getLogger(this.getClass()).info("Calling: " + this.restEndpoint);
	}

	@Override
	public void setExecutorDataMap(Map<String, Object> executorDataMap)
	{
		this.restEndpoint = "";
		if(executorDataMap.containsKey("restEndpoint"))
		{
			this.restEndpoint = (String) executorDataMap.get("restEndpoint");
		}
	}

	@Override
	public String[] getValidExecutorDataMapKeys()
	{
		return this.validKeys;
	}

	@Override
	public Map<String, Object> getExecutorDataMap()
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("restEndpoint", this.restEndpoint);
		return dataMap;
	}

}
