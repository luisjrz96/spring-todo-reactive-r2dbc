package com.luisjrz.reactive.todo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ValidatorService {

	@Autowired
	private Validator validator;
	
	public boolean hasErrors(Object target, String objectName) {
		Errors errors = new BeanPropertyBindingResult(target, objectName);
		validator.validate(target, errors);
		return errors.hasErrors();
	}
	
	public List<String> getErrors(Object target, String objectName) {
		Errors errors = new BeanPropertyBindingResult(target, objectName);
		validator.validate(target, errors);
		return errors.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
	}
	
}
