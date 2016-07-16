package de.kawumtech.app.controlgroups.model.sensor;

public class SensorValue<T>
{
	private T value;
	
	private Long timestamp;

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}
}
