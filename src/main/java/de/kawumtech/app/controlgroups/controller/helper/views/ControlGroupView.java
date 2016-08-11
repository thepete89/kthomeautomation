package de.kawumtech.app.controlgroups.controller.helper.views;

import java.util.ArrayList;
import java.util.List;

import de.kawumtech.app.controlgroups.model.sensor.Sensor;

public class ControlGroupView
{
	private String controlGroupName;
	
	private String selectedSensor;
	
	private String selectedActionEvaluation;
	
	private String controlGroupId;
	
	private List<Sensor> addedSensors = new ArrayList<Sensor>();
	
	private List<Sensor> availableSensors = new ArrayList<Sensor>();
	
	private List<ActionView> addedActionEvaluations = new ArrayList<ActionView>();
	
	private List<ActionView> availableActionEvaluations = new ArrayList<ActionView>();
	
	public String getControlGroupName()
	{
		return controlGroupName;
	}

	public void setControlGroupName(String controlGroupName)
	{
		this.controlGroupName = controlGroupName;
	}

	public String getSelectedSensor()
	{
		return selectedSensor;
	}

	public void setSelectedSensor(String selectedSensor)
	{
		this.selectedSensor = selectedSensor;
	}

	public String getSelectedActionEvaluation()
	{
		return selectedActionEvaluation;
	}

	public void setSelectedActionEvaluation(String selectedActionEvaluation)
	{
		this.selectedActionEvaluation = selectedActionEvaluation;
	}

	public List<Sensor> getAddedSensors()
	{
		return addedSensors;
	}

	public void setAddedSensors(List<Sensor> addedSensors)
	{
		this.addedSensors = addedSensors;
	}

	public List<ActionView> getAddedActionEvaluations()
	{
		return addedActionEvaluations;
	}

	public void setAddedActionEvaluations(List<ActionView> addedActionEvaluations)
	{
		this.addedActionEvaluations = addedActionEvaluations;
	}

	public String getControlGroupId()
	{
		return controlGroupId;
	}

	public void setControlGroupId(String controlGroupId)
	{
		this.controlGroupId = controlGroupId;
	}

	public List<Sensor> getAvailableSensors()
	{
		return availableSensors;
	}

	public void setAvailableSensors(List<Sensor> availableSensors)
	{
		this.availableSensors = availableSensors;
	}

	public List<ActionView> getAvailableActionEvaluations()
	{
		return availableActionEvaluations;
	}

	public void setAvailableActionEvaluations(List<ActionView> availableActionEvaluations)
	{
		this.availableActionEvaluations = availableActionEvaluations;
	}
}
