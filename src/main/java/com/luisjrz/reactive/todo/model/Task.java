package com.luisjrz.reactive.todo.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("tasks")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {

	@Id
	private Long id;
	@NotBlank(message = "title is required")
	private String title;
	@NotBlank(message = "description is required")
	private String description;
	@NotBlank(message = "date is required")
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private LocalDate expectedDate;
	private boolean completed=false;
	
	
}
