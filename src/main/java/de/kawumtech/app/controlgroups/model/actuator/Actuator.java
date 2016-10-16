package de.kawumtech.app.controlgroups.model.actuator;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Actuator
{
	@Id
	private String id;
	
	private String name;
	
	private String endpoint;
	
	private String description;
	
	private List<String> controlGroupIds;
}
