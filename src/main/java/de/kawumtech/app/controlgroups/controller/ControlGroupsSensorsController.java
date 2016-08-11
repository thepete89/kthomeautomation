package de.kawumtech.app.controlgroups.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.service.SensorService;

@RestController
public class ControlGroupsSensorsController
{	
	@Autowired
	private SensorService sensorService;
	
	
	@RequestMapping(value="/controlgroups/sensors")
	public List<Sensor> getView(final Model model)
	{
		return this.sensorService.loadAllSensors();
	}
	
	@RequestMapping(value="/controlgroups/sensors/delete", params = {"id"})
	public List<Sensor> deleteSensor(@RequestParam final String id)
	{
		this.sensorService.deleteSensor(id);
		return this.sensorService.loadAllSensors();
	}
}
