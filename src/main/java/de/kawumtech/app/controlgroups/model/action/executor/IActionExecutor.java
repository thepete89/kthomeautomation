package de.kawumtech.app.controlgroups.model.action.executor;

import java.util.Map;


public interface IActionExecutor
{
	void execute();
	
	void setExecutorDataMap(Map<String, Object> executorDataMap);
	
	String[] getValidExecutorDataMapKeys();
	
	Map<String, Object> getExecutorDataMap();
}
