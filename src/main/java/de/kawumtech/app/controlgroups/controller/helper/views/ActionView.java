package de.kawumtech.app.controlgroups.controller.helper.views;

import java.util.Map;

public class ActionView
{
	private String actionId;
	
	private String actionEvaluationName;
	
	private Map<String, Object> actionDataMap;
	
	private String actionExecutor;
	
	private Boolean linked = Boolean.FALSE;
	
	private String evaluationScript;

	public String getActionId()
	{
		return actionId;
	}

	public void setActionId(String actionId)
	{
		this.actionId = actionId;
	}

	public Map<String, Object> getActionDataMap()
	{
		return actionDataMap;
	}

	public void setActionDataMap(Map<String, Object> actionDataMap)
	{
		this.actionDataMap = actionDataMap;
	}

	public String getActionExecutor()
	{
		return actionExecutor;
	}

	public void setActionExecutor(String actionExecutor)
	{
		this.actionExecutor = actionExecutor;
	}

	public String getActionEvaluationName()
	{
		return actionEvaluationName;
	}

	public void setActionEvaluationName(String actionEvaluationName)
	{
		this.actionEvaluationName = actionEvaluationName;
	}

	public Boolean getLinked()
	{
		return linked;
	}

	public void setLinked(Boolean linked)
	{
		this.linked = linked;
	}

	public String getEvaluationScript()
	{
		return evaluationScript;
	}

	public void setEvaluationScript(String evaluationScript)
	{
		this.evaluationScript = evaluationScript;
	}
}
