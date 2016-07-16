package de.kawumtech.app.controlgroups.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.kawumtech.app.controlgroups.controller.helper.ControlGroupsControllerHelper;
import de.kawumtech.app.controlgroups.controller.helper.views.ControlGroupView;
import de.kawumtech.app.controlgroups.model.ControlGroup;
import de.kawumtech.app.controlgroups.service.ActionEvaluationService;
import de.kawumtech.app.controlgroups.service.ControlGroupService;
import de.kawumtech.app.controlgroups.service.SensorService;
import de.kawumtech.core.navigation.model.NavigationElement;
import de.kawumtech.core.navigation.service.NavigationService;

@Controller
public class ControlGroupsManageController
{
	@Autowired
	private NavigationService navigationService;
	
	@Autowired
	private ControlGroupService controlGroupService;
	
	@Autowired
	private ActionEvaluationService actionEvaluationService;
	
	@Autowired
	private SensorService sensorService;
	
	@Autowired
	private ControlGroupsControllerHelper controlGroupsControllerHelper;
	
	@ModelAttribute("navigationElements")
	public List<NavigationElement> loadNavigation()
	{
		this.navigationService.setActiveNavigationElement("controlgroups");
		return this.navigationService.getNavigationElements();
	}
	
	@RequestMapping(value="/controlgroups/manage")
	public String getView(final Model model)
	{
		model.addAttribute("controlGroups", this.controlGroupService.loadControlGroups());
		return "controlgroups/manage/manage";
	}
	
	@RequestMapping(value="/controlgroups/manage/delete", params = {"id"})
	public String deleteControlGroup(final Model model, @RequestParam final String id)
	{
		this.controlGroupService.deleteControlGroup(id);
		model.addAttribute("controlGroups", this.controlGroupService.loadControlGroups());
		return "controlgroups/manage/manage";
	}

	@RequestMapping(value="/controlgroups/manage/edit", params = {"id"})
	public String editControlGroup(final Model model, @RequestParam final String id)
	{
		model.addAttribute("controlGroupView", this.controlGroupsControllerHelper.convertControlGroupToControlGroupView(this.controlGroupService.findControlGroupById(id)));
		model.addAttribute("availableActionEvaluations", this.actionEvaluationService.findActionEvaluationsByLinked(Boolean.FALSE));
		model.addAttribute("availableSensors", this.sensorService.findSensorsByLinked(Boolean.FALSE));
		return "controlgroups/manage/addEditControlGroup";
	}
	
	@RequestMapping(value="/controlgroups/manage/add")
	public String addControlGroup(final Model model)
	{
		model.addAttribute("controlGroupView", new ControlGroupView());
		model.addAttribute("availableActionEvaluations", this.actionEvaluationService.findActionEvaluationsByLinked(Boolean.FALSE));
		model.addAttribute("availableSensors", this.sensorService.findSensorsByLinked(Boolean.FALSE));
		return "controlgroups/manage/addEditControlGroup";
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"addSensor"})
	public String addSensor(final Model model, final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		controlGroupView.getAddedSensors().add(this.sensorService.findSensorById(controlGroupView.getSelectedSensor()));
		model.addAttribute("controlGroupView", controlGroupView);
		model.addAttribute("availableActionEvaluations", this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		model.addAttribute("availableSensors", this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return "controlgroups/manage/addEditControlGroup";
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"removeSensor"})
	public String removeSensor(final Model model, final ControlGroupView controlGroupView, final BindingResult bindingResult, final HttpServletRequest httpServletRequest)
	{
		String sensorId = String.valueOf(httpServletRequest.getParameter("removeSensor"));
		this.controlGroupsControllerHelper.removeSensor(controlGroupView, sensorId);
		model.addAttribute("controlGroupView", controlGroupView);
		model.addAttribute("availableActionEvaluations", this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		model.addAttribute("availableSensors", this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return "controlgroups/manage/addEditControlGroup";
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"addAction"})
	public String addAction(final Model model, final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		controlGroupView.getAddedActionEvaluations().add(this.actionEvaluationService.findActionEvaluationById(controlGroupView.getSelectedActionEvaluation()));
		model.addAttribute("controlGroupView", controlGroupView);
		model.addAttribute("availableActionEvaluations", this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		model.addAttribute("availableSensors", this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return "controlgroups/manage/addEditControlGroup";
	}

	@RequestMapping(value="/controlgroups/manage/modify", params = {"removeAction"})
	public String removeAction(final Model model, final ControlGroupView controlGroupView, final BindingResult bindingResult, final HttpServletRequest httpServletRequest)
	{
		String actionId = String.valueOf(httpServletRequest.getParameter("removeAction"));
		this.controlGroupsControllerHelper.removeAction(controlGroupView, actionId);
		model.addAttribute("controlGroupView", controlGroupView);
		model.addAttribute("availableActionEvaluations", this.controlGroupsControllerHelper.filterSelectableActionEvaluations(controlGroupView.getAddedActionEvaluations()));
		model.addAttribute("availableSensors", this.controlGroupsControllerHelper.filterSelectableSensors(controlGroupView.getAddedSensors()));
		return "controlgroups/manage/addEditControlGroup";
	}
	
	@RequestMapping(value="/controlgroups/manage/modify", params = {"save"})
	public String saveControlGroup(final Model model, final ControlGroupView controlGroupView, final BindingResult bindingResult)
	{
		ControlGroup controlGroup = this.controlGroupsControllerHelper.convertControlGroupViewToControlGroup(controlGroupView);
		this.controlGroupService.saveControlGroup(controlGroup);
		model.addAttribute("controlGroups", this.controlGroupService.loadControlGroups());
		return "controlgroups/manage/manage";
	}
}
