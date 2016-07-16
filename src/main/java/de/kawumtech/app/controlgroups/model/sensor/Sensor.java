package de.kawumtech.app.controlgroups.model.sensor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Sensor
{
	@Id
	private String id;
	private String sensorName;
	private String sensorDescriptor;
	private SensorValue<?> sensorValue;
	private Boolean linked;
	
	public Boolean isLinked()
	{
		return linked;
	}
	public void setLinked(Boolean linked)
	{
		this.linked = linked;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getSensorName()
	{
		return sensorName;
	}
	public void setSensorName(String sensorName)
	{
		this.sensorName = sensorName;
	}
	public String getSensorDescriptor()
	{
		return sensorDescriptor;
	}
	public void setSensorDescriptor(String sensorDescriptor)
	{
		this.sensorDescriptor = sensorDescriptor;
	}
	public SensorValue<?> getSensorValue()
	{
		return sensorValue;
	}
	public void setSensorValue(SensorValue<?> sensorValue)
	{
		this.sensorValue = sensorValue;
	}
}
