package de.kawumtech.restapi.sensor.controller;

import java.text.DecimalFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.model.sensor.SensorValue;
import de.kawumtech.app.controlgroups.service.ControlGroupService;
import de.kawumtech.app.controlgroups.service.SensorService;
import de.kawumtech.ktha.restlib.sensor.pojo.SensorReading;

@RestController
public class SensorRestController
{

	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ControlGroupService controlGroupService;
	
	private static DecimalFormat FORMAT = new DecimalFormat(".##");
	
	@RequestMapping(value="/api/accept/numeric", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.OK)
	public void acceptNumericReading(@RequestBody final SensorReading<Double> sensorReading)
	{
		Date now = new Date();
		Sensor sensor = this.sensorService.findSensorBySensorName(sensorReading.getSensorName());
		if(sensor != null)
		{
			SensorValue<Double> sensorValue = new SensorValue<Double>();
			if(!sensorReading.getValue().isNaN())
			{				
				sensorValue.setValue(Double.valueOf(FORMAT.format(sensorReading.getValue()).replace(",", ".")));
			}
			else
			{
				sensorValue.setValue(sensorReading.getValue());
			}
			sensorValue.setTimestamp(now.getTime());
			sensor.setSensorValue(sensorValue);
			this.sensorService.saveSensor(sensor);
			this.controlGroupService.triggerControlGroup(sensor.getSensorName());
		}
	}
	
	@RequestMapping(value="/api/accept/boolean", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.OK)
	public void acceptBooleanReading(@RequestBody final SensorReading<Boolean> sensorReading)
	{
		Date now = new Date();
		Sensor sensor = this.sensorService.findSensorBySensorName(sensorReading.getSensorName());
		if(sensor != null)
		{
			SensorValue<Boolean> sensorValue = new SensorValue<Boolean>();
			sensorValue.setValue(sensorReading.getValue());
			sensorValue.setTimestamp(now.getTime());
			sensor.setSensorValue(sensorValue);
			this.sensorService.saveSensor(sensor);
			this.controlGroupService.triggerControlGroup(sensor.getSensorName());
		}
	}
	
}
