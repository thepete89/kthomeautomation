package de.kawumtech.app.controlgroups.controller.helper;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.kawumtech.app.controlgroups.controller.helper.views.ControlGroupView;
import de.kawumtech.app.controlgroups.model.ControlGroup;
import de.kawumtech.app.controlgroups.model.actuator.Actuator;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.service.ActuatorService;
import de.kawumtech.app.controlgroups.service.SensorService;

@Component
public class ControlGroupsControllerHelper
{
	@Autowired
	private SensorService sensorService;

	@Autowired
	private ActuatorService actuatorService;
	
	public List<ControlGroupView> createControlGroupViews(final List<ControlGroup> controlGroups)
	{
		List<ControlGroupView> controlGroupViews = controlGroups.stream().map(cg -> {
			return this.convertControlGroupToControlGroupView(cg);
		}).collect(Collectors.toList());
		return controlGroupViews;
	}
	
	public List<Sensor> filterSelectableSensors(final List<Sensor> selectedSensors)
	{
		List<Sensor> availableSensors = this.sensorService.loadAllSensors();
		Iterator<Sensor> sensorIterator = availableSensors.iterator();
		while (sensorIterator.hasNext())
		{
			Sensor currentSensor = sensorIterator.next();
			for (Sensor selectedSensor : selectedSensors)
			{
				if (currentSensor.getId().equals(selectedSensor.getId()))
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
		controlGroup.setControlGroupScript(controlGroupView.getControlGroupScript());
		return controlGroup;
	}

	public void assignSensors(ControlGroup controlGroup, List<Sensor> addedSensors)
	{
		for (Sensor sensor : addedSensors)
		{
			Sensor savedSensor = this.sensorService.findSensorById(sensor.getId());
			savedSensor.getControlGroupIds().add(controlGroup.getId());
			this.sensorService.saveSensor(savedSensor);
		}
	}

	public ControlGroupView convertControlGroupToControlGroupView(ControlGroup controlGroup)
	{
		ControlGroupView controlGroupView = new ControlGroupView();
		if (controlGroup != null)
		{
			controlGroupView.setControlGroupId(controlGroup.getId());
			controlGroupView.setControlGroupName(controlGroup.getControlGroupName());
			controlGroupView.setControlGroupScript(controlGroup.getControlGroupScript());
			controlGroupView.setAddedSensors(this.sensorService
					.findSensorsByControlGroupIdsContaining(controlGroup.getId()));
			controlGroupView.setAddedActuators(this.actuatorService.findByControlGroupIdsContains(controlGroup.getId()));
			controlGroupView.setAvailableSensors(this.filterSelectableSensors(controlGroupView.getAddedSensors()));
			controlGroupView.setAvailableActuators(this.filterSelectableActuators(controlGroupView.getAddedActuators()));
		}
		return controlGroupView;
	}

	public void removeSensor(ControlGroupView controlGroupView, String sensorId)
	{
		Iterator<Sensor> sensorIterator = controlGroupView.getAddedSensors().iterator();
		while (sensorIterator.hasNext())
		{
			Sensor sensor = sensorIterator.next();
			if (sensor.getId().equals(sensorId))
			{
				sensorIterator.remove();
				Sensor savedSensor = this.sensorService.findSensorById(sensorId);
				savedSensor.getControlGroupIds().remove(controlGroupView.getControlGroupId());
				this.sensorService.saveSensor(savedSensor);
				break;
			}
		}
	}

	public void assignActuators(ControlGroup controlGroup, List<Actuator> addedActuators)
	{
		for (Actuator actuator : addedActuators)
		{
			Actuator savedActuator = this.actuatorService.findById(actuator.getId());
			savedActuator.getControlGroupIds().add(controlGroup.getId());
			this.actuatorService.saveActuator(savedActuator);
		}
	}

	public List<Actuator> filterSelectableActuators(List<Actuator> addedActuators)
	{
		List<Actuator> availableActuators = this.actuatorService.loadAllActuators();
		Iterator<Actuator> actuatorIterator = availableActuators.iterator();
		while (actuatorIterator.hasNext())
		{
			Actuator currentActuator = actuatorIterator.next();
			for (Actuator actuator : addedActuators)
			{
				if (currentActuator.getId().equals(actuator.getId()))
				{
					actuatorIterator.remove();
					break;
				}
			}
		}
		return availableActuators;
	}

	public void removeActuator(ControlGroupView controlGroupView, String actuatorId)
	{
		Iterator<Actuator> actuatorIterator = controlGroupView.getAddedActuators().iterator();
		while (actuatorIterator.hasNext())
		{
			Actuator actuator = actuatorIterator.next();
			if (actuator.getId().equals(actuatorId))
			{
				actuatorIterator.remove();
				Actuator savedActuator = this.actuatorService.findById(actuatorId);
				savedActuator.getControlGroupIds().remove(controlGroupView.getControlGroupId());
				this.actuatorService.saveActuator(savedActuator);
				break;
			}
		}
	}
}
