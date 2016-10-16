package de.kawumtech.app.controlgroups.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.kawumtech.app.controlgroups.model.actuator.Actuator;
import de.kawumtech.app.controlgroups.service.ActuatorService;

@RestController
public class ControlGroupsActuatorsController
{
	
	@Autowired
	private ActuatorService actuatorService;
	
	@RequestMapping(value="/controlgroups/actuators")
	public List<Actuator> getView()
	{
		return this.actuatorService.loadAllActuators();
	}
	
	@RequestMapping(value="/controlgroups/actuators/delete", params = {"id"})
	public List<Actuator> deleteActuator(@RequestParam final String id)
	{
		this.actuatorService.deleteActuator(id);
		return this.actuatorService.loadAllActuators();
	}
}
