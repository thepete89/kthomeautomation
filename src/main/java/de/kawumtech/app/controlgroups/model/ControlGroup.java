package de.kawumtech.app.controlgroups.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ControlGroup
{
	@Id
	private String id;
	
	private List<String> sensorIds = new ArrayList<String>();
	
	private List<String> actionEvaluationIds = new ArrayList<String>();
		
	private String controlGroupName;
	
	public String getControlGroupName()
	{
		return controlGroupName;
	}

	public void setControlGroupName(String controlGroupName)
	{
		this.controlGroupName = controlGroupName;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public List<String> getSensorIds()
	{
		return sensorIds;
	}

	public void setSensorIds(List<String> sensorIds)
	{
		this.sensorIds = sensorIds;
	}

	public List<String> getActionEvaluationIds()
	{
		return actionEvaluationIds;
	}

	public void setActionEvaluationIds(List<String> actionEvaluationIds)
	{
		this.actionEvaluationIds = actionEvaluationIds;
	}
}
