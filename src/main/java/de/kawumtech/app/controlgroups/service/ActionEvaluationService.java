package de.kawumtech.app.controlgroups.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.kawumtech.app.controlgroups.model.action.ActionEvaluation;
import de.kawumtech.app.controlgroups.model.action.executor.IActionExecutor;
import de.kawumtech.app.controlgroups.repository.ActionEvaluationRepository;
import de.kawumtech.app.controlgroups.service.helper.ActionExecutorHelper;

@Service
public class ActionEvaluationService
{
	@Autowired
	private ActionEvaluationRepository actionEvaluationRepository;
	
	@Autowired
	private ActionExecutorHelper actionExecutorHelper;
	
	public List<ActionEvaluation> findActionEvaluationsByIds(List<String> actionEvaluationIds)
	{
		List<ActionEvaluation> actionEvaluations = new ArrayList<ActionEvaluation>();
		for (String actionEvaluationId : actionEvaluationIds)
		{
			if(this.actionEvaluationRepository.exists(actionEvaluationId))
			{
				actionEvaluations.add(this.actionEvaluationRepository.findOne(actionEvaluationId));
			}
		}
		return actionEvaluations;
	}
	
	public List<ActionEvaluation> findActionEvaluationsByLinked(Boolean linked)
	{
		return this.actionEvaluationRepository.findByLinked(linked);
	}
	
	public List<ActionEvaluation> findAllActionEvaluations()
	{
		return this.actionEvaluationRepository.findAll();
	}
	
	public List<String> getAvailableActionExecutors()
	{
		return this.actionExecutorHelper.getAvailableActionExecutors();
	}
	
	public IActionExecutor getActionExecutorByRegistrationName(final String registrationName)
	{
		return this.actionExecutorHelper.getActionExecutorByRegistrationName(registrationName);
	}
	
	public ActionEvaluation saveActionEvaluation(final ActionEvaluation actionEvaluation)
	{
		ActionEvaluation actionEvaluationToSave = actionEvaluation;
		if(!StringUtils.isEmpty(actionEvaluationToSave.getId()))
		{
			ActionEvaluation savedActionEvaluation = this.actionEvaluationRepository.findOne(actionEvaluationToSave.getId());
			this.setEditedFields(savedActionEvaluation, actionEvaluationToSave);
			actionEvaluationToSave = savedActionEvaluation;
		}
		return this.actionEvaluationRepository.save(actionEvaluationToSave);
	}
	
	public ActionEvaluation findActionEvaluationById(String actionEvaluationId)
	{
		return this.actionEvaluationRepository.findOne(actionEvaluationId);
	}
	
	private void setEditedFields(ActionEvaluation savedActionEvaluation, ActionEvaluation actionEvaluationToSave)
	{
		savedActionEvaluation.setActionExecutor(actionEvaluationToSave.getActionExecutor());
		savedActionEvaluation.setEvaluationScript(actionEvaluationToSave.getEvaluationScript());
		savedActionEvaluation.setLinked(actionEvaluationToSave.isLinked());
		savedActionEvaluation.setActionEvaluationName(actionEvaluationToSave.getActionEvaluationName());
	}

	public void deleteAction(String id)
	{
		this.actionEvaluationRepository.delete(id);
	}
}
