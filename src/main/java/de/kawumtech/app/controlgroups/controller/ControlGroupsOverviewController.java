package de.kawumtech.app.controlgroups.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.kawumtech.core.navigation.model.NavigationElement;
import de.kawumtech.core.navigation.service.NavigationService;

@Controller
public class ControlGroupsOverviewController
{
	@Autowired
	private NavigationService navigationService;
	
	@ModelAttribute("navigationElements")
	public List<NavigationElement> loadNavigation()
	{
		this.navigationService.setActiveNavigationElement("controlgroups");
		return this.navigationService.getNavigationElements();
	}
	
	@RequestMapping(value="/controlgroups")
	public String getView(final Model model)
	{
		return "controlgroups/overview";
	}
}
