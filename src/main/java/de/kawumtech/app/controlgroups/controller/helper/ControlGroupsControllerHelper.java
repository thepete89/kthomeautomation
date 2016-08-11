package de.kawumtech.app.controlgroups.controller.helper;

import java.util.ArrayList;
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
	
	public List<ActionView> filterSelectableActionEvaluations(List<ActionView> selectedActionEvaluations)
	{
		List<ActionView> availableActionEvaluations = this.loadAvailableActionEvaluations();
		Iterator<ActionView> actionIterator = availableActionEvaluations.iterator();
		while(actionIterator.hasNext())
		{
			ActionView currentActionEvaluation = actionIterator.next();
			for (ActionView selectedActionEvaluation : selectedActionEvaluations)
			{
				if(currentActionEvaluation.getActionId().equals(selectedActionEvaluation.getActionId()))
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
			actionView.setAvailableActionExecutors(this.actionEvaluationService.getAvailableActionExecutors());
		}
		return actionView;
	}
	
	public void updateActionExecutorDataMap(ActionView actionView)
	{
		Map<String, Object> actionDataMap = new HashMap<String, Object>();
		IActionExecutor actionExecutor = this.actionEvaluationService.getActionExecutorByRegistrationName(actionView.getActionExecutor());
		if(actionView.getActionId() != null)
		{
			actionDataMap = this.checkForExistingExecutorData(actionView, actionExecutor);
		}
		else
		{
			this.fillActionDataMap(actionDataMap, actionExecutor);
		}
		actionView.setActionDataMap(actionDataMap);
	}

	private void fillActionDataMap(Map<String, Object> actionDataMap, IActionExecutor actionExecutor)
	{
		for (String key : actionExecutor.getValidExecutorDataMapKeys())
		{
			actionDataMap.put(key, "");
		}
	}
	
	private Map<String, Object> checkForExistingExecutorData(ActionView actionView, IActionExecutor actionExecutor)
	{
		Map<String, Object> actionDataMap = new HashMap<String, Object>();
		ActionEvaluation savedActionEvaluation = this.actionEvaluationService.findActionEvaluationById(actionView.getActionId());
		if(savedActionEvaluation.getActionExecutor().getClass().equals(actionExecutor.getClass()))
		{
			actionDataMap = savedActionEvaluation.getActionExecutor().getExecutorDataMap();
		}
		else
		{
			this.fillActionDataMap(actionDataMap, actionExecutor);
		}
		return actionDataMap;
	}
	
	private void assignActions(ControlGroup controlGroup, List<ActionView> addedActionEvaluations)
	{
		for (ActionView actionEvaluation : addedActionEvaluations)
		{
			controlGroup.getActionEvaluationIds().add(actionEvaluation.getActionId());
			ActionEvaluation savedActionEvaluation = this.actionEvaluationService.findActionEvaluationById(actionEvaluation.getActionId());
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
		List<ActionView> selectedActionEvaluations = new ArrayList<ActionView>();
		for (ActionEvaluation actionEvaluation : this.actionEvaluationService.findActionEvaluationsByIds(actionEvaluationIds))
		{
			selectedActionEvaluations.add(this.convertActionEvaluationToActionView(actionEvaluation));
		}
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
		Iterator<ActionView> actionIterator = controlGroupView.getAddedActionEvaluations().iterator();
		while(actionIterator.hasNext())
		{
			ActionView actionEvaluation = actionIterator.next();
			if(actionEvaluation.getActionId().equals(actionId))
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

	public List<ActionView> loadAvailableActionEvaluations()
	{
		List<ActionView> availableActions = new ArrayList<ActionView>();
		for (ActionEvaluation actionEvaluation : this.actionEvaluationService.findActionEvaluationsByLinked(Boolean.FALSE))
		{
			availableActions.add(this.convertActionEvaluationToActionView(actionEvaluation));
		}
		return availableActions;
	}
	
	public List<ActionView> loadAllActionEvaluations()
	{
		List<ActionView> actionViews = new ArrayList<ActionView>();
		for (ActionEvaluation actionEvaluation : this.actionEvaluationService.findAllActionEvaluations())
		{
			actionViews.add(this.convertActionEvaluationToActionView(actionEvaluation));
		}
		return actionViews;
	}
}
