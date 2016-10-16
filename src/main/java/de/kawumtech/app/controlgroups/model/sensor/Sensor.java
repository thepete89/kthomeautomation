package de.kawumtech.app.controlgroups.model.sensor;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Sensor
{
	@Id
	private String id;
	private String sensorName;
	private String sensorDescriptor;
	private SensorValue<?> sensorValue;
	private List<String> controlGroupIds;
	
}
