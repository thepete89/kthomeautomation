package de.kawumtech.restapi.registration.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.kawumtech.app.controlgroups.service.SensorService;
import de.kawumtech.ktha.restlib.registration.RegistrationState;

@RestController
public class RegistrationController
{
	@Autowired
	private SensorService sensorService;
	
	@RequestMapping(value="/api/register/{sensorName}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.OK)
	public RegistrationState registerSensor(@PathVariable final String sensorName)
	{
		RegistrationState registrationState = RegistrationState.UNDEFINED;
		if(!this.sensorService.isSensorExistingByName(sensorName))
		{
			this.sensorService.createAndSaveSensor(sensorName, "REST registered sensor " + sensorName);
			registrationState = RegistrationState.REGISTERED;
			LoggerFactory.getLogger(this.getClass()).info("Sensor " + sensorName + " registered via REST-API");
		}
		else
		{
			registrationState = RegistrationState.KNOWN;
			LoggerFactory.getLogger(this.getClass()).info("Sensor " + sensorName + " already known.");
		}
		return registrationState;
	}
}
