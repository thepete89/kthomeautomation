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
import de.kawumtech.app.controlgroups.model.action.ActionEvaluation;
import de.kawumtech.app.controlgroups.service.ActionEvaluationService;
import de.kawumtech.app.controlgroups.service.ControlGroupService;
import de.kawumtech.app.controlgroups.service.SensorService;

@RestController
public class ControlGroupsManageController
{	
	@Autowired
	private ControlGroupService controlGroupService;
	
	@Autowired
	private ActionEvaluationService actionEvaluationService;
	
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ControlGroupsControllerHelper controlGroupsControllerHelper;
	
	
	@RequestMapping(value="/controlgroups/manage")
	public List<ControlGroup> getView()
	{
		return this.controlGroupService.loadControlGroups();
	}
	
	@RequestMapping(value="/controlgroups/manage/delete", params = {"id"})
	public List<ControlGroup> deleteControlGroup(@RequestParam final String id)
	{
		this.controlGroupService.deleteControlGroup(id);
		return this.controlGroupService.loadControlGroups();
	}

	@RequestMapping(value="/controlgroups/manage/edit", params = {"id"})
	public ControlGroupView editControlGroup(@RequestParam final String id)
	{
		ControlGroupView view = this.controlGroupsControllerHelper.convertControlGroupToControlGroupView(this.controlGroupService.findControlGroupById(id));
		view.setAvailableActionEvaluations(this.controlGroupsControllerHelper.loadAvailableActionEvaluations());
		view.setAvailableSensors(this.sensorService.findSensorsByLinked(Boolean.FALSE));
		return view;
	}
	
	@RequestMapping(value="/controlgroups/manage/add")
	public ControlGroupView addControlGroup()
	{
		ControlGroupView view = new ControlGroupView();
		view.setAvailableActionEvaluations(this.controlGroupsControllerHelper.loadAvailableActionEvaluations());
		view.setAvailableSensors(this.sensorService.findSensorsByLinked(Boolean.FALSE));
		return view;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"addSensor"})
	public ControlGroupView addSensor(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		controlGroupView.getAddedSensors().add(this.sensorService.findSensorById(controlGroupView.getSelectedSensor()));
		controlGroupView.setAvailableActionEvaluations(this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		controlGroupView.setAvailableSensors(this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return controlGroupView;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"removeSensor"})
	public ControlGroupView removeSensor(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult, final HttpServletRequest httpServletRequest)
	{
		String sensorId = String.valueOf(httpServletRequest.getParameter("removeSensor"));
		this.controlGroupsControllerHelper.removeSensor(controlGroupView, sensorId);
		controlGroupView.setAvailableActionEvaluations(this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		controlGroupView.setAvailableSensors(this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return controlGroupView;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"addAction"})
	public ControlGroupView addAction(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		ActionEvaluation selectedAction = this.actionEvaluationService.findActionEvaluationById(controlGroupView.getSelectedActionEvaluation());
		controlGroupView.getAddedActionEvaluations().add(this.controlGroupsControllerHelper.convertActionEvaluationToActionView(selectedAction));
		controlGroupView.setAvailableActionEvaluations(this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		controlGroupView.setAvailableSensors(this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return controlGroupView;
	}

	@RequestMapping(value="/controlgroups/manage/modify", params = {"removeAction"})
	public ControlGroupView removeAction(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult, final HttpServletRequest httpServletRequest)
	{
		String actionId = String.valueOf(httpServletRequest.getParameter("removeAction"));
		this.controlGroupsControllerHelper.removeAction(controlGroupView, actionId);
		controlGroupView.setAvailableActionEvaluations(this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		controlGroupView.setAvailableSensors(this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return controlGroupView;
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"save"})
	public ControlGroupView saveControlGroup(@RequestBody final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		ControlGroup controlGroup = this.controlGroupsControllerHelper.convertControlGroupViewToControlGroup(controlGroupView);
		this.controlGroupService.saveControlGroup(controlGroup);
		return controlGroupView;
	}
}
