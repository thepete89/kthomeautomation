package de.kawumtech.app.controlgroups.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.kawumtech.app.controlgroups.controller.helper.ControlGroupsControllerHelper;
import de.kawumtech.app.controlgroups.controller.helper.views.ActionView;
import de.kawumtech.app.controlgroups.model.action.ActionEvaluation;
import de.kawumtech.app.controlgroups.service.ActionEvaluationService;

@RestController
public class ControlGroupsActionsController
{
	@Autowired
	private ActionEvaluationService actionEvaluationService;
	
	@Autowired
	private ControlGroupsControllerHelper controlGroupsControllerHelper;
			
	@RequestMapping(value="/controlgroups/actions")
	public List<ActionView> getView(final Model model)
	{
		return this.controlGroupsControllerHelper.loadAllActionEvaluations();
	}
	
	@RequestMapping(value="/controlgroups/actions/add")
	public ActionView addAction()
	{
		ActionView actionView = new ActionView();
		actionView.setActionDataMap(new HashMap<String, Object>());
		actionView.setAvailableActionExecutors(this.actionEvaluationService.getAvailableActionExecutors());
		return actionView;
	}
	
	@RequestMapping(value="/controlgroups/actions/edit", params = {"id"})
	public ActionView editAction(@RequestParam final String id)
	{
		ActionEvaluation actionEvaluation = this.actionEvaluationService.findActionEvaluationById(id);
		ActionView actionView = this.controlGroupsControllerHelper.convertActionEvaluationToActionView(actionEvaluation);
		return actionView;
	}
	
	@RequestMapping(value="/controlgroups/actions/delete", params = {"id"})
	public List<ActionView> deleteAction(@RequestParam final String id)
	{
		this.actionEvaluationService.deleteAction(id);
		return this.controlGroupsControllerHelper.loadAllActionEvaluations();
	}
	
	@RequestMapping(value="/controlgroups/actions/modify", params = {"selectExecutor"})
	public ActionView selectExecutor(@RequestBody final ActionView actionView, final BindingResult bindingResult)
	{
		this.controlGroupsControllerHelper.updateActionExecutorDataMap(actionView);
		return actionView;
	}
	
	@RequestMapping(value="/controlgroups/actions/modify", params = {"save"})
	public ActionView saveAction(@RequestBody final ActionView actionView, final BindingResult bindingResult)
	{
		ActionEvaluation actionEvaluation = this.controlGroupsControllerHelper.convertActionViewToActionEvaluation(actionView);
		this.actionEvaluationService.saveActionEvaluation(actionEvaluation);
		return actionView;
	}
}
