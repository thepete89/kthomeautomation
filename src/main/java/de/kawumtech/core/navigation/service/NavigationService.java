package de.kawumtech.core.navigation.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import de.kawumtech.core.navigation.model.NavigationElement;

@Service
public class NavigationService
{
	private List<NavigationElement> navigationElements;
	
	@PostConstruct
	private void init()
	{
		this.navigationElements = new ArrayList<NavigationElement>();
		this.navigationElements.add(new NavigationElement("Home", "home", "/"));
		NavigationElement controlGroups = new NavigationElement("Control Groups", "controlgroups", "#");
		NavigationElement divider = new NavigationElement("", "", "");
		divider.setDivider(Boolean.TRUE);
		controlGroups.getChildren().add(new NavigationElement("Overview", "controlgroups.overview", "/controlgroups"));
		controlGroups.getChildren().add(divider);
		controlGroups.getChildren().add(new NavigationElement("Manage Control Groups", "controlgroups.manage", "/controlgroups/manage"));
		controlGroups.getChildren().add(new NavigationElement("Sensoric Modules", "controlgroups.sensors", "/controlgroups/sensors"));
		controlGroups.getChildren().add(new NavigationElement("Control Actions", "controlgroups.actions", "/controlgroups/actions"));
		this.navigationElements.add(controlGroups);
		this.navigationElements.add(new NavigationElement("System Status", "systemstatus", "/systemstatus"));
		this.navigationElements.add(new NavigationElement("About", "about", "/about"));
		this.navigationElements.add(new NavigationElement("Widgets", "widgets", "/widgets"));
	}
	
	public void setActiveNavigationElement(final String elementTag)
	{
		this.resetActiveNavigation();
		for (NavigationElement navigationElement : this.navigationElements)
		{
			if(navigationElement.getElementTag().equals(elementTag))
			{
				navigationElement.setActive(Boolean.TRUE);
				break;
			}
		}
	}
	
	public List<NavigationElement> getNavigationElements()
	{
		return this.navigationElements;
	}
	
	private void resetActiveNavigation()
	{
		for (NavigationElement navigationElement : this.navigationElements)
		{
			if(navigationElement.getActive())
			{
				navigationElement.setActive(Boolean.FALSE);
				break;
			}
		}
	}
}
