package de.kawumtech.app.controlgroups.controller.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.kawumtech.app.controlgroups.controller.helper.views.ActionView;
import de.kawumtech.app.controlgroups.controller.helper.views.ControlGroupView;
import de.kawumtech.app.controlgroups.model.ControlGroup;
import de.kawumtech.app.controlgroups.model.action.ActionEvaluation;
import de.kawumtech.app.controlgroups.model.action.executor.IActionExecutor;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.service.ActionEvaluationService;
import de.kawumtech.app.controlgroups.service.SensorService;

@Component
public class ControlGroupsControllerHelper
{
	@Autowired
	private ActionEvaluationService actionEvaluationService;
	
	@Autowired
	private SensorService sensorService;
	
	public List<ActionEvaluation> filterSelectableActionEvaluations(List<ActionEvaluation> selectedActionEvaluations)
	{
		List<ActionEvaluation> availableActionEvaluations = this.actionEvaluationService.findActionEvaluationsByLinked(Boolean.FALSE);
		Iterator<ActionEvaluation> actionIterator = availableActionEvaluations.iterator();
		while(actionIterator.hasNext())
		{
			ActionEvaluation currentActionEvaluation = actionIterator.next();
			for (ActionEvaluation selectedActionEvaluation : selectedActionEvaluations)
			{
				if(currentActionEvaluation.getId().equals(selectedActionEvaluation.getId()))
				{
					actionIterator.remove();
					break;
				}
			}
		}
		return availableActionEvaluations;
	}
	
	public List<Sensor> filterSelectableSensors(List<Sensor> selectedSensors)
	{
		List<Sensor> availableSensors = this.sensorService.findSensorsByLinked(Boolean.FALSE);
		Iterator<Sensor> sensorIterator = availableSensors.iterator();
		while(sensorIterator.hasNext())
		{
			Sensor currentSensor = sensorIterator.next();
			for (Sensor selectedSensor : selectedSensors)
			{
				if(currentSensor.getId().equals(selectedSensor.getId()))
				{
					sensorIterator.remove();
					break;
				}
			}
		}
		return availableSensors;
	}

	public ControlGroup convertControlGroupViewToControlGroup(ControlGroupView controlGroupView)
	{
		ControlGroup controlGroup = new ControlGroup();
		controlGroup.setId(controlGroupView.getControlGroupId());
		controlGroup.setControlGroupName(controlGroupView.getControlGroupName());
		this.assignSensors(controlGroup, controlGroupView.getAddedSensors());
		this.assignActions(controlGroup, controlGroupView.getAddedActionEvaluations());
		return controlGroup;
	}
	
	public ActionView convertActionEvaluationToActionView(ActionEvaluation actionEvaluation)
	{
		ActionView actionView = new ActionView();
		if(actionEvaluation != null)
		{
			actionView.setActionId(actionEvaluation.getId());
			actionView.setLinked(actionEvaluation.isLinked());
			actionView.setEvaluationScript(actionEvaluation.getEvaluationScript());
			actionView.setActionDataMap(actionEvaluation.getActionExecutor().getExecutorDataMap());
			actionView.setActionEvaluationName(actionEvaluation.getActionEvaluationName());
			actionView.setActionExecutor(actionEvaluation.getActionExecutor().getClass().getSimpleName());
		}
		return actionView;
	}
	
	public void updateActionExecutorDataMap(ActionView actionView)
	{
		IActionExecutor actionExecutor = this.actionEvaluationService.getActionExecutorByRegistrationName(actionView.getActionExecutor());
		Map<String, Object> actionDataMap = new HashMap<String, Object>();
		for (String key : actionExecutor.getValidExecutorDataMapKeys())
		{
			actionDataMap.put(key, "");
		}
		actionView.setActionDataMap(actionDataMap);
	}
	
	private void assignActions(ControlGroup controlGroup, List<ActionEvaluation> addedActionEvaluations)
	{
		for (ActionEvaluation actionEvaluation : addedActionEvaluations)
		{
			controlGroup.getActionEvaluationIds().add(actionEvaluation.getId());
			ActionEvaluation savedActionEvaluation = this.actionEvaluationService.findActionEvaluationById(actionEvaluation.getId());
			savedActionEvaluation.setEvaluationScript(actionEvaluation.getEvaluationScript());
			savedActionEvaluation.setLinked(Boolean.TRUE);
			this.actionEvaluationService.saveActionEvaluation(savedActionEvaluation);
		}
	}

	private void assignSensors(ControlGroup controlGroup, List<Sensor> addedSensors)
	{
		for (Sensor sensor : addedSensors)
		{
			controlGroup.getSensorIds().add(sensor.getId());
			Sensor savedSensor = this.sensorService.findSensorById(sensor.getId());
			savedSensor.setLinked(Boolean.TRUE);
			this.sensorService.saveSensor(savedSensor);
		}
	}

	public ControlGroupView convertControlGroupToControlGroupView(ControlGroup controlGroup)
	{
		ControlGroupView controlGroupView = new ControlGroupView();
		if(controlGroup != null)
		{
			controlGroupView.setControlGroupId(controlGroup.getId());
			controlGroupView.setControlGroupName(controlGroup.getControlGroupName());
			this.loadSensors(controlGroupView, controlGroup.getSensorIds());
			this.loadActions(controlGroupView, controlGroup.getActionEvaluationIds());
		}
		return controlGroupView;
	}

	private void loadActions(ControlGroupView controlGroupView, List<String> actionEvaluationIds)
	{
		List<ActionEvaluation> selectedActionEvaluations = this.actionEvaluationService.findActionEvaluationsByIds(actionEvaluationIds);
		controlGroupView.setAddedActionEvaluations(selectedActionEvaluations);
	}

	private void loadSensors(ControlGroupView controlGroupView, List<String> sensorIds)
	{
		List<Sensor> selectedSensors = this.sensorService.findSensorsByIds(sensorIds);
		controlGroupView.setAddedSensors(selectedSensors);
	}

	public void removeSensor(ControlGroupView controlGroupView, String sensorId)
	{
		Iterator<Sensor> sensorIterator = controlGroupView.getAddedSensors().iterator();
		while(sensorIterator.hasNext())
		{
			Sensor sensor = sensorIterator.next();
			if(sensor.getId().equals(sensorId))
			{
				sensorIterator.remove();
				Sensor savedSensor = this.sensorService.findSensorById(sensorId);
				savedSensor.setLinked(Boolean.FALSE);
				this.sensorService.saveSensor(savedSensor);
				break;
			}
		}
	}

	public void removeAction(ControlGroupView controlGroupView, String actionId)
	{
		Iterator<ActionEvaluation> actionIterator = controlGroupView.getAddedActionEvaluations().iterator();
		while(actionIterator.hasNext())
		{
			ActionEvaluation actionEvaluation = actionIterator.next();
			if(actionEvaluation.getId().equals(actionId))
			{
				actionIterator.remove();
				ActionEvaluation savedActionEvaluation = this.actionEvaluationService.findActionEvaluationById(actionId);
				savedActionEvaluation.setLinked(Boolean.FALSE);
				this.actionEvaluationService.saveActionEvaluation(savedActionEvaluation);
				break;
			}
		}
	}

	public ActionEvaluation convertActionViewToActionEvaluation(ActionView actionView)
	{
		ActionEvaluation actionEvaluation = new ActionEvaluation();
		actionEvaluation.setId(actionView.getActionId());
		actionEvaluation.setActionEvaluationName(actionView.getActionEvaluationName());
		actionEvaluation.setLinked(actionView.getLinked());
		actionEvaluation.setEvaluationScript(actionView.getEvaluationScript());
		IActionExecutor actionExecutor = this.actionEvaluationService.getActionExecutorByRegistrationName(actionView.getActionExecutor());
		if(actionExecutor != null)
		{
			actionExecutor.setExecutorDataMap(actionView.getActionDataMap());
		}
		actionEvaluation.setActionExecutor(actionExecutor);
		return actionEvaluation;
	}
}
