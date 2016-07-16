package de.kawumtech.app.controlgroups.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.kawumtech.app.controlgroups.controller.helper.ControlGroupsControllerHelper;
import de.kawumtech.app.controlgroups.controller.helper.views.ActionView;
import de.kawumtech.app.controlgroups.model.action.ActionEvaluation;
import de.kawumtech.app.controlgroups.service.ActionEvaluationService;
import de.kawumtech.core.navigation.model.NavigationElement;
import de.kawumtech.core.navigation.service.NavigationService;

@Controller
public class ControlGroupsActionsController
{
	@Autowired
	private NavigationService navigationService;
	
	@Autowired
	private ActionEvaluationService actionEvaluationService;
	
	@Autowired
	private ControlGroupsControllerHelper controlGroupsControllerHelper;
	
	@ModelAttribute("navigationElements")
	public List<NavigationElement> loadNavigation()
	{
		this.navigationService.setActiveNavigationElement("controlgroups");
		return this.navigationService.getNavigationElements();
	}
	
	@ModelAttribute("availableActionExecutors")
	public List<String> loadAvailableActionExecutors()
	{
		return this.actionEvaluationService.getAvailableActionExecutors();
	}
	
	@RequestMapping(value="/controlgroups/actions")
	public String getView(final Model model)
	{
		model.addAttribute("actionEvaluations", this.actionEvaluationService.findAllActionEvaluations());
		return "controlgroups/actions/actions";
	}
	
	@RequestMapping(value="/controlgroups/actions/add")
	public String addAction(final Model model)
	{
		ActionView actionView = new ActionView();
		actionView.setActionDataMap(new HashMap<String, Object>());
		model.addAttribute("actionView", actionView);
		return "controlgroups/actions/addEditAction";
	}
	
	@RequestMapping(value="/controlgroups/actions/edit", params = {"id"})
	public String editAction(final Model model, @RequestParam final String id)
	{
		ActionEvaluation actionEvaluation = this.actionEvaluationService.findActionEvaluationById(id);
		ActionView actionView = this.controlGroupsControllerHelper.convertActionEvaluationToActionView(actionEvaluation);
		model.addAttribute("actionView", actionView);
		return "controlgroups/actions/addEditAction";
	}
	
	@RequestMapping(value="/controlgroups/actions/delete", params = {"id"})
	public String deleteAction(final Model model, @RequestParam final String id)
	{
		this.actionEvaluationService.deleteAction(id);
		model.addAttribute("actionEvaluations", this.actionEvaluationService.findAllActionEvaluations());
		return "controlgroups/actions/actions";
	}
	
	@RequestMapping(value="/controlgroups/actions/modify", params = {"selectExecutor"})
	public String selectExecutor(final Model model, final ActionView actionView, final BindingResult bindingResult)
	{
		this.controlGroupsControllerHelper.updateActionExecutorDataMap(actionView);
		model.addAttribute("actionView", actionView);
		return "controlgroups/actions/addEditAction";
	}
	
	@RequestMapping(value="/controlgroups/actions/modify", params = {"save"})
	public String saveAction(final Model model, final ActionView actionView, final BindingResult bindingResult)
	{
		ActionEvaluation actionEvaluation = this.controlGroupsControllerHelper.convertActionViewToActionEvaluation(actionView);
		this.actionEvaluationService.saveActionEvaluation(actionEvaluation);
		model.addAttribute("actionEvaluations", this.actionEvaluationService.findAllActionEvaluations());
		return "controlgroups/actions/actions";
	}
}
