package de.kawumtech.app.controlgroups.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.kawumtech.app.controlgroups.model.ControlGroup;

public interface ControlGroupRepository extends MongoRepository<ControlGroup, String>
{
	ControlGroup findByControlGroupName(String controlGroupName);
	ControlGroup findBySensorIdsContains(String sensorId);
}
