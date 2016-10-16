package de.kawumtech.app.controlgroups.controller.helper.views;

import java.util.ArrayList;
import java.util.List;

import de.kawumtech.app.controlgroups.model.actuator.Actuator;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import lombok.Data;

@Data
public class ControlGroupView
{
	private String controlGroupName;
	
	private String selectedSensor;
	
	private String selectedActuator;
	
	private String controlGroupId;
	
	private String controlGroupScript;
	
	private List<Sensor> addedSensors = new ArrayList<Sensor>();
	
	private List<Sensor> availableSensors = new ArrayList<Sensor>();
	
	private List<Actuator> addedActuators = new ArrayList<Actuator>();
	
	private List<Actuator> availableActuators = new ArrayList<Actuator>();
}
