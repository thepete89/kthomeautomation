package de.kawumtech.app.controlgroups.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.kawumtech.app.controlgroups.model.actuator.Actuator;
import de.kawumtech.app.controlgroups.repository.ActuatorRepository;

@Service
public class ActuatorService
{
	@Autowired
	private ActuatorRepository actuatorRepository;
	
	public void createAndSaveActuator(final String description, final String endpoint, final String name)
	{
		Actuator actuator = new Actuator();
		actuator.setControlGroupIds(new ArrayList<String>());
		actuator.setDescription(description);
		actuator.setEndpoint(endpoint);
		actuator.setName(name);
		this.actuatorRepository.save(actuator);
	}
	
	public Actuator findById(final String id)
	{
		return this.actuatorRepository.findOne(id);
	}
	
	public List<Actuator> findByControlGroupIdsContains(final String controlGroupId)
	{
		return this.actuatorRepository.findByControlGroupIdsContaining(controlGroupId);
	}
	
	public List<Actuator> loadAllActuators()
	{
		return this.actuatorRepository.findAll();
	}
	
	public boolean isActuatorExistingByName(final String name)
	{
		return this.actuatorRepository.findByName(name) != null;
	}
	
	public Actuator saveActuator(final Actuator actuator)
	{
		Actuator actuatorToSave = actuator;
		if(!StringUtils.isEmpty(actuatorToSave.getId()))
		{
			Actuator savedActuator = this.actuatorRepository.findOne(actuatorToSave.getId());
			this.setEditedFields(savedActuator, actuatorToSave);
			actuatorToSave = savedActuator;
		}
		return this.actuatorRepository.save(actuatorToSave);
	}

	private void setEditedFields(Actuator savedActuator, Actuator actuatorToSave)
	{
		savedActuator.setControlGroupIds(actuatorToSave.getControlGroupIds());
		savedActuator.setDescription(actuatorToSave.getDescription());
		savedActuator.setEndpoint(actuatorToSave.getEndpoint());
		savedActuator.setName(actuatorToSave.getName());
	}
	
	public void deleteActuator(final String id)
	{
		this.actuatorRepository.delete(id);
	}
}
