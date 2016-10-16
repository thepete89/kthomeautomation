package de.kawumtech.app.controlgroups.model.sensor;

import lombok.Data;

@Data
public class SensorValue<T>
{
	private T value;
	
	private Long timestamp;

}
