package de.kawumtech.app.controlgroups.model.action;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import de.kawumtech.app.controlgroups.model.action.executor.IActionExecutor;
import de.kawumtech.app.controlgroups.model.sensor.Sensor;

@Document
public class ActionEvaluation
{
	@Id
	private String id;
	
	private String actionEvaluationName;
	
	private IActionExecutor actionExecutor;
	
	private String evaluationScript;
	
	private Boolean linked;	
	
	public Boolean isLinked()
	{
		return linked;
	}

	public void setLinked(Boolean linked)
	{
		this.linked = linked;
	}

	public void setActionExecutor(IActionExecutor actionExecutor)
	{
		this.actionExecutor = actionExecutor;
	}

	public IActionExecutor getActionExecutor()
	{
		return actionExecutor;
	}
	
	public String getEvaluationScript()
	{
		return evaluationScript;
	}
	
	public void setEvaluationScript(String evaluationScript)
	{
		this.evaluationScript = evaluationScript;
	}	
	
	public void evaluate(List<Sensor> sensors)
	{
		ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
		for (Sensor sensor : sensors)
		{
			scriptEngine.put(sensor.getSensorName(), sensor.getSensorValue());
		}
		try
		{
			Boolean result  = (Boolean) scriptEngine.eval(this.evaluationScript);
			if(result && this.actionExecutor != null)
			{
				this.actionExecutor.execute();
			}
		} catch (ScriptException e)
		{
			LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
		}
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getActionEvaluationName()
	{
		return actionEvaluationName;
	}

	public void setActionEvaluationName(String actionEvaluationName)
	{
		this.actionEvaluationName = actionEvaluationName;
	}
}
