package de.kawumtech.app.controlgroups.service.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import de.kawumtech.app.controlgroups.model.action.executor.IActionExecutor;

@Component
public class ActionExecutorHelper
{
	private Map<String, Class<? extends IActionExecutor>> availableActionExecutors = new HashMap<String, Class<? extends IActionExecutor>>();
		
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() throws ClassNotFoundException
	{
		ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false);
		classPathScanningCandidateComponentProvider.addIncludeFilter(new AssignableTypeFilter(IActionExecutor.class));
		Set<BeanDefinition> beanDefinitions = classPathScanningCandidateComponentProvider.findCandidateComponents("de.kawumtech.app.controlgroups.model");
		for (BeanDefinition beanDefinition : beanDefinitions)
		{
			String registrationName = beanDefinition.getBeanClassName().substring(beanDefinition.getBeanClassName().lastIndexOf('.') + 1);
			LoggerFactory.getLogger(this.getClass()).info("*** ACTION EXECUTOR HELPER INIT - REGISTERING: " + beanDefinition.getBeanClassName() + " AS " + registrationName);
			Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
			availableActionExecutors.put(registrationName, (Class<? extends IActionExecutor>) clazz);
		}
		
	}
	
	public List<String> getAvailableActionExecutors()
	{
		return new ArrayList<String>(this.availableActionExecutors.keySet());
	}
	
	public IActionExecutor getActionExecutorByRegistrationName(final String registrationName)
	{
		IActionExecutor actionExecutor = null;
		try
		{
			if(this.availableActionExecutors.containsKey(registrationName))
			{				
				actionExecutor = this.availableActionExecutors.get(registrationName).newInstance();
			}
		} 
		catch (InstantiationException | IllegalAccessException e)
		{
			LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
		}
		return actionExecutor;
	}
}
