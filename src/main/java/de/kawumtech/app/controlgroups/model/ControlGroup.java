package de.kawumtech.app.controlgroups.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class ControlGroup
{
	@Id
	private String id;
			
	private String controlGroupName;
	
	private String controlGroupScript;
	
}
