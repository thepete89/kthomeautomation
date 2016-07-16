package de.kawumtech.app.controlgroups.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.kawumtech.app.controlgroups.model.ControlGroup;
import de.kawumtech.app.controlgroups.model.action.ActionEvaluation;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.repository.ControlGroupRepository;

@Service
public class ControlGroupService
{	
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ActionEvaluationService actionEvaluationService;
	
	@Autowired
	private ControlGroupRepository controlGroupRepository;
	
	public ControlGroup saveControlGroup(final ControlGroup controlGroup)
	{
		ControlGroup controlGroupToSave = controlGroup;
		if(!StringUtils.isEmpty(controlGroupToSave.getId()))
		{
			ControlGroup savedControlGroup = this.controlGroupRepository.findOne(controlGroup.getId());
			this.setEditedFields(savedControlGroup, controlGroup);
			controlGroupToSave = savedControlGroup;
		}
		return this.controlGroupRepository.save(controlGroupToSave);
	}
	
	private void setEditedFields(final ControlGroup savedControlGroup, final ControlGroup controlGroup)
	{
		savedControlGroup.setActionEvaluationIds(controlGroup.getActionEvaluationIds());
		savedControlGroup.setControlGroupName(controlGroup.getControlGroupName());
		savedControlGroup.setSensorIds(controlGroup.getSensorIds());
	}

	public void triggerControlGroup(final String sensorName)
	{
		Sensor sensor = this.sensorService.findSensorBySensorName(sensorName);
		ControlGroup controlGroup = this.controlGroupRepository.findBySensorIdsContains(sensor.getId());
		if(controlGroup != null)
		{
			List<Sensor> controlGroupSensors = this.sensorService.findSensorsByIds(controlGroup.getSensorIds());
			List<ActionEvaluation> controlGroupActionEvaluations = this.actionEvaluationService.findActionEvaluationsByIds(controlGroup.getActionEvaluationIds());
			for (ActionEvaluation actionEvaluation : controlGroupActionEvaluations)
			{
				actionEvaluation.evaluate(controlGroupSensors);
			}
		}
	}
	
	public void deleteControlGroup(String controlGroupId)
	{
		ControlGroup controlGroup = this.controlGroupRepository.findOne(controlGroupId);
		this.unlinkSensors(controlGroup.getSensorIds());
		this.unlinkActionEvaluations(controlGroup.getActionEvaluationIds());
		this.controlGroupRepository.delete(controlGroupId);
	}
	
	private void unlinkSensors(List<String> sensorIds)
	{
		List<Sensor> linkedSensors = this.sensorService.findSensorsByIds(sensorIds);
		for (Sensor sensor : linkedSensors)
		{
			sensor.setLinked(Boolean.FALSE);
			this.sensorService.saveSensor(sensor);
		}
	}
	
	private void unlinkActionEvaluations(List<String> actionEvaluationIds)
	{
		List<ActionEvaluation> linkedActionEvaluations = this.actionEvaluationService.findActionEvaluationsByIds(actionEvaluationIds);
		for (ActionEvaluation actionEvaluation : linkedActionEvaluations)
		{
			actionEvaluation.setLinked(Boolean.FALSE);
			this.actionEvaluationService.saveActionEvaluation(actionEvaluation);
		}
	}


	public ControlGroup getControlGroupByControlGroupName(final String controlGroupName)
	{
		return this.controlGroupRepository.findByControlGroupName(controlGroupName);
	}
	
	public List<ControlGroup> loadControlGroups()
	{
		return this.controlGroupRepository.findAll();
	}

	public ControlGroup findControlGroupById(String id)
	{
		return this.controlGroupRepository.findOne(id);
	}
}
