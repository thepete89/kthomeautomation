package de.kawumtech.app.controlgroups.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.kawumtech.app.controlgroups.model.sensor.Sensor;
import de.kawumtech.app.controlgroups.repository.SensorRepository;

@Service
public class SensorService
{
	
	@Autowired
	private SensorRepository sensorRepository;
	
	public Sensor createAndSaveSensor(final String sensorName, final String sensorDescriptor)
	{
		Sensor sensor = new Sensor();
		sensor.setSensorName(sensorName);
		sensor.setSensorDescriptor(sensorDescriptor);
		sensor.setLinked(Boolean.FALSE);
		return this.sensorRepository.save(sensor);
	}
	
	public Sensor saveSensor(final Sensor sensor)
	{
		Sensor sensorToSave = sensor;
		if(!StringUtils.isEmpty(sensorToSave.getId()))
		{
			Sensor savedSensor = this.sensorRepository.findOne(sensorToSave.getId());
			this.setEditedFields(savedSensor, sensorToSave);
			sensorToSave = savedSensor;
		}
		return this.sensorRepository.save(sensorToSave);
	}
	

	public List<Sensor> findSensorsByIds(final List<String> ids)
	{
		List<Sensor> sensors = new ArrayList<Sensor>();
		for (String id : ids)
		{
			if(this.sensorRepository.exists(id))
			{
				sensors.add(this.sensorRepository.findOne(id));
			}
		}
		return sensors;
	}
	
	public Sensor findSensorBySensorName(final String sensorName)
	{
		return this.sensorRepository.findBySensorName(sensorName);
	}
	
	public Sensor findSensorById(final String sensorId)
	{
		return this.sensorRepository.findOne(sensorId);
	}
	
	public List<Sensor> findSensorsByLinked(final Boolean linked)
	{
		return this.sensorRepository.findByLinked(linked);
	}
	
	public List<Sensor> loadAllSensors()
	{
		return this.sensorRepository.findAll();
	}
	
	public boolean isSensorExistingByName(final String sensorName)
	{
		return this.sensorRepository.findBySensorName(sensorName) != null;
	}
	
	private void setEditedFields(Sensor savedSensor, Sensor sensorToSave)
	{
		savedSensor.setSensorDescriptor(sensorToSave.getSensorDescriptor());
		savedSensor.setSensorName(sensorToSave.getSensorName());
		savedSensor.setSensorValue(sensorToSave.getSensorValue());
		savedSensor.setLinked(sensorToSave.isLinked());
	}

	public void deleteSensor(String id)
	{
		this.sensorRepository.delete(id);
	}
}
