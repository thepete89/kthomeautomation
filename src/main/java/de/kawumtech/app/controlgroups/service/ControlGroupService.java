package de.kawumtech.app.controlgroups.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.kawumtech.app.controlgroups.model.ControlGroup;
import de.kawumtech.app.controlgroups.model.actuator.Actuator;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.repository.ControlGroupRepository;

@Service
public class ControlGroupService
{	
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ActuatorService actuatorService;
	
	@Autowired
	private ScriptService scriptService;
		
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
		savedControlGroup.setControlGroupName(controlGroup.getControlGroupName());
		savedControlGroup.setControlGroupScript(controlGroup.getControlGroupScript());
	}
	
	public void triggerControlGroups(final List<ControlGroup> controlGroupsToTrigger)
	{
		for (ControlGroup controlGroup : controlGroupsToTrigger)
		{
			List<Sensor> sensors = this.sensorService.findSensorsByControlGroupIdsContaining(controlGroup.getId());
			List<Actuator> actuators = this.actuatorService.findByControlGroupIdsContains(controlGroup.getId());
			this.scriptService.runScript(controlGroup.getControlGroupScript(), actuators, sensors);
		}
	}
	
	public void deleteControlGroup(String controlGroupId)
	{
		this.unlinkSensors(controlGroupId);
		this.unlinkActuators(controlGroupId);
		this.controlGroupRepository.delete(controlGroupId);
	}
	
	private void unlinkSensors(String controlGroupId)
	{
		List<Sensor> linkedSensors = this.sensorService.findSensorsByControlGroupIdsContaining(controlGroupId);
		for (Sensor sensor : linkedSensors)
		{
			sensor.getControlGroupIds().remove(controlGroupId);
			this.sensorService.saveSensor(sensor);
		}
	}
	
	private void unlinkActuators(String controlGroupId)
	{
		List<Actuator> linkedActuators = this.actuatorService.findByControlGroupIdsContains(controlGroupId);
		for (Actuator actuator : linkedActuators)
		{
			actuator.getControlGroupIds().remove(controlGroupId);
			this.actuatorService.saveActuator(actuator);
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
