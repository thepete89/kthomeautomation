package de.kawumtech.app.controlgroups.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.kawumtech.app.controlgroups.model.actuator.Actuator;

public interface ActuatorRepository extends MongoRepository<Actuator, String>
{
	List<Actuator> findByControlGroupIdsContaining(String controlGroupId);

	Actuator findByName(String name);
}
