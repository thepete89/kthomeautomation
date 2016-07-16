package de.kawumtech.app.controlgroups.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.kawumtech.app.controlgroups.model.action.ActionEvaluation;

public interface ActionEvaluationRepository extends MongoRepository<ActionEvaluation, String>
{
	List<ActionEvaluation> findByLinked(Boolean linked);
}
