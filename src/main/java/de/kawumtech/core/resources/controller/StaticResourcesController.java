package de.kawumtech.core.resources.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StaticResourcesController
{
	@RequestMapping(value = "/static/**", method = RequestMethod.GET)
	public ResponseEntity<ClassPathResource> getFile(final HttpServletRequest request)
	{
		String requestUrl = request.getRequestURI();
		ResponseEntity<ClassPathResource> responseEntity = null;
		try
		{
			responseEntity = new ResponseEntity<ClassPathResource>(new ClassPathResource(requestUrl), HttpStatus.OK);
		} catch (Exception e)
		{
			LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
			responseEntity = new ResponseEntity<ClassPathResource>(HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
}
