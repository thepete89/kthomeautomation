package de.kawumtech.app.controlgroups.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.kawumtech.app.controlgroups.model.sensor.Sensor;

public interface SensorRepository extends MongoRepository<Sensor, String>
{
	Sensor findBySensorName(String sensorName);
	List<Sensor> findByLinked(Boolean linked);
}
