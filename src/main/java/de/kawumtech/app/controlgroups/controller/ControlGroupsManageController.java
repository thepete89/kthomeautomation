package de.kawumtech.app.controlgroups.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.kawumtech.app.controlgroups.controller.helper.ControlGroupsControllerHelper;
import de.kawumtech.app.controlgroups.controller.helper.views.ControlGroupView;
import de.kawumtech.app.controlgroups.model.ControlGroup;
import de.kawumtech.app.controlgroups.service.ActuatorService;
import de.kawumtech.app.controlgroups.service.ControlGroupService;
import de.kawumtech.app.controlgroups.service.SensorService;

@RestController
public class ControlGroupsManageController
{	
	@Autowired
	private ControlGroupService controlGroupService;
		
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ActuatorService actuatorService;
	
	@Autowired
	private ControlGroupsControllerHelper controlGroupsControllerHelper;
	
	
	@RequestMapping(value="/controlgroups/manage")
	public List<ControlGroupView> getView()
	{
		return this.controlGroupsControllerHelper.createControlGroupViews(this.controlGroupService.loadControlGroups());
	}
	
	@RequestMapping(value="/controlgroups/manage/delete", params = {"id"})
	public List<ControlGroupView> deleteControlGroup(@RequestParam final String id)
	{
		this.controlGroupService.deleteControlGroup(id);
		return this.controlGroupsControllerHelper.createControlGroupViews(this.controlGroupService.loadControlGroups());
	}

	@RequestMapping(value="/controlgroups/manage/edit", params = {"id"})
	public ControlGroupView editControlGroup(@RequestParam final String id)
	{
		ControlGroupView view = this.controlGroupsControllerHelper.convertControlGroupToControlGroupView(this.controlGroupService.findControlGroupById(id));
		return view;
	}
	
	@RequestMapping(value="/controlgroups/manage/add")
	public ControlGroupView addControlGroup()
	{
		ControlGroupView view = new ControlGroupView();
		view.setAvailableSensors(this.sensorService.loadAllSensors());
		view.setAvailableActuators(this.actuatorService.loadAllActuators());
		return view;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"addSensor"})
	public ControlGroupView addSensor(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		controlGroupView.getAddedSensors().add(this.sensorService.findSensorById(controlGroupView.getSelectedSensor()));
		controlGroupView.setAvailableSensors(this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return controlGroupView;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"removeSensor"})
	public ControlGroupView removeSensor(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult, final HttpServletRequest httpServletRequest)
	{
		String sensorId = String.valueOf(httpServletRequest.getParameter("removeSensor"));
		this.controlGroupsControllerHelper.removeSensor(controlGroupView, sensorId);
		controlGroupView.setAvailableSensors(this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return controlGroupView;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"addActuator"})
	public ControlGroupView addActuator(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		controlGroupView.getAddedActuators().add(this.actuatorService.findById(controlGroupView.getSelectedActuator()));
		controlGroupView.setAvailableActuators(this.controlGroupsControllerHelper.filterSelectableActuators(controlGroupView.getAddedActuators()));
		return controlGroupView;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"removeActuator"})
	public ControlGroupView removeActuator(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult, final HttpServletRequest httpServletRequest)
	{
		String actuatorId = String.valueOf(httpServletRequest.getParameter("removeActuator"));
		this.controlGroupsControllerHelper.removeActuator(controlGroupView, actuatorId);
		controlGroupView.setAvailableActuators(this.controlGroupsControllerHelper.filterSelectableActuators(controlGroupView.getAddedActuators()));
		return controlGroupView;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"save"})
	public ControlGroupView saveControlGroup(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		ControlGroup controlGroup = this.controlGroupsControllerHelper.convertControlGroupViewToControlGroup(controlGroupView);
		controlGroup = this.controlGroupService.saveControlGroup(controlGroup);
		this.controlGroupsControllerHelper.assignSensors(controlGroup, controlGroupView.getAddedSensors());
		this.controlGroupsControllerHelper.assignActuators(controlGroup, controlGroupView.getAddedActuators());
		return controlGroupView;
	}
}
