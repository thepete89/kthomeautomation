package de.kawumtech.restapi.sensor.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.kawumtech.app.controlgroups.model.ControlGroup;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.model.sensor.SensorValue;
import de.kawumtech.app.controlgroups.service.ControlGroupService;
import de.kawumtech.app.controlgroups.service.SensorService;
import de.kawumtech.ktha.restlib.sensor.pojo.SensorReading;

@RestController
@RequestMapping("/api/accept")
public class SensorRestController
{

	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ControlGroupService controlGroupService;
	
	private static DecimalFormat FORMAT = new DecimalFormat(".##");
	
	@RequestMapping(value="/numeric", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.OK)
	public void acceptNumericReading(@RequestBody final SensorReading<Double> sensorReading)
	{
		Date now = new Date();
		Sensor sensor = this.sensorService.findSensorBySensorName(sensorReading.getSensorName());
		if(sensor != null)
		{
			List<ControlGroup> controlGroupsToTrigger = new ArrayList<ControlGroup>();
			this.processDoubleReading(sensorReading, now, sensor);
			for (String controlGroupId : sensor.getControlGroupIds())
			{
				ControlGroup controlGroup = this.controlGroupService.findControlGroupById(controlGroupId);
				controlGroupsToTrigger.add(controlGroup);
			}
			this.controlGroupService.triggerControlGroups(controlGroupsToTrigger);
		}
	}

	private void processDoubleReading(final SensorReading<?> sensorReading, Date now, Sensor sensor)
	{
		SensorValue<Double> sensorValue = new SensorValue<Double>();
		if(!((Double) sensorReading.getValue()).isNaN())
		{				
			sensorValue.setValue(Double.valueOf(FORMAT.format(sensorReading.getValue()).replace(",", ".")));
		}
		else
		{
			sensorValue.setValue((Double) sensorReading.getValue());
		}
		sensorValue.setTimestamp(now.getTime());
		sensor.setSensorValue(sensorValue);
		this.sensorService.saveSensor(sensor);
	}
	
	@RequestMapping(value="/boolean", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.OK)
	public void acceptBooleanReading(@RequestBody final SensorReading<Boolean> sensorReading)
	{
		Date now = new Date();
		Sensor sensor = this.sensorService.findSensorBySensorName(sensorReading.getSensorName());
		if(sensor != null)
		{
			List<ControlGroup> controlGroupsToTrigger = new ArrayList<ControlGroup>();
			this.processBooleanReading(sensorReading, now, sensor);
			for (String controlGroupId : sensor.getControlGroupIds())
			{
				ControlGroup controlGroup = this.controlGroupService.findControlGroupById(controlGroupId);
				controlGroupsToTrigger.add(controlGroup);
			}
			this.controlGroupService.triggerControlGroups(controlGroupsToTrigger);
		}
	}

	private void processBooleanReading(final SensorReading<?> sensorReading, Date now, Sensor sensor)
	{
		SensorValue<Boolean> sensorValue = new SensorValue<Boolean>();
		sensorValue.setValue((Boolean) sensorReading.getValue());
		sensorValue.setTimestamp(now.getTime());
		sensor.setSensorValue(sensorValue);
		this.sensorService.saveSensor(sensor);
	}
	
	@RequestMapping(value="/multiple", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.OK)
	public void acceptMultipleReadings(@RequestBody final List<SensorReading<?>> sensorReadings)
	{
		Date now = new Date();
		List<ControlGroup> controlGroupsToTrigger = new ArrayList<ControlGroup>();
		for (SensorReading<?> sensorReading : sensorReadings)
		{
			Sensor sensor = this.sensorService.findSensorBySensorName(sensorReading.getSensorName());
			if(sensor != null)
			{
				if(sensorReading.getValue() instanceof Double)
				{
					this.processDoubleReading(sensorReading, now, sensor);
				}
				if(sensorReading.getValue() instanceof Boolean)
				{
					this.processBooleanReading(sensorReading, now, sensor);
				}
				for (String controlGroupId : sensor.getControlGroupIds())
				{
					ControlGroup controlGroup = this.controlGroupService.findControlGroupById(controlGroupId);
					if(!controlGroupsToTrigger.contains(controlGroup))
					{
						controlGroupsToTrigger.add(controlGroup);
					}
				}
			}
		}
		this.controlGroupService.triggerControlGroups(controlGroupsToTrigger);
	}
	
}
