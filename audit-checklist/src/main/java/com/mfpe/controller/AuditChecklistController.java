package com.mfpe.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfpe.exception.ElementNotFound;
import com.mfpe.exception.EmptyInputException;
import com.mfpe.exception.FeignClientException;
import com.mfpe.model.AuditType;
import com.mfpe.model.Question;
import com.mfpe.service.AuthorizationService;
import com.mfpe.service.QuestionService;

@RestController
@RequestMapping("/checklist")
@CrossOrigin(origins = "*")
public class AuditChecklistController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private AuthorizationService authorizationService;

	Logger logger = LoggerFactory.getLogger("Checklist-Controller-Logger");

	@PostMapping(value = "/checklistquestions")
	public List<Question> auditCheckListQuestions(@RequestHeader("Authorization") String jwt,
			@RequestBody AuditType auditType) {
		List<Question> questions = new ArrayList<Question>();

		logger.info("from header JWT :: " + jwt);

		if (auditType.getAuditType() == null || auditType.getAuditType().length() <= 0) {
			throw new EmptyInputException("Give Inputs in proper-format");
		}

		// checking if the jwt is valid or not
		try {
			if (authorizationService.validateJwt(jwt)) {
				questions = questionService.getQuestionsByAuditType(auditType);
			}
		} catch (Exception e) {
			System.out.println("--------" + e.getMessage());
			String[] arrOfStr = e.getMessage().split(":");
			throw new FeignClientException(arrOfStr[arrOfStr.length - 1]);

		}

		if (questions.size() == 0) {
			throw new ElementNotFound("No such element available in the database!");
		}

		return questions;
	}

	@GetMapping("health-check")
	public String healthCheck() {
		return "Audit Checklist Microservice is Active";
	}
}
