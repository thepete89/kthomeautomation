package de.kawumtech.app.controlgroups.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.kawumtech.app.controlgroups.service.SensorService;
import de.kawumtech.core.navigation.model.NavigationElement;
import de.kawumtech.core.navigation.service.NavigationService;

@Controller
public class ControlGroupsSensorsController
{
	@Autowired
	private NavigationService navigationService;
	
	@Autowired
	private SensorService sensorService;
	
	@ModelAttribute("navigationElements")
	public List<NavigationElement> loadNavigation()
	{
		this.navigationService.setActiveNavigationElement("controlgroups");
		return this.navigationService.getNavigationElements();
	}
	
	@RequestMapping(value="/controlgroups/sensors")
	public String getView(final Model model)
	{
		model.addAttribute("sensors", this.sensorService.loadAllSensors());
		return "controlgroups/sensors/sensors";
	}
	
	@RequestMapping(value="/controlgroups/sensors/delete", params = {"id"})
	public String deleteSensor(final Model model, @RequestParam final String id)
	{
		this.sensorService.deleteSensor(id);
		model.addAttribute("sensors", this.sensorService.loadAllSensors());
		return "controlgroups/sensors/sensors";
	}
}
