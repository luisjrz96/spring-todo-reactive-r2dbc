package com.luisjrz.reactive.todo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("tasks")
@Data @NoArgsConstructor @AllArgsConstructor
public class Task {

	@Id
	private Long id;
	private String title;
	private String description;
	private Date expectedDate;
	private boolean completed;
	
	
}
