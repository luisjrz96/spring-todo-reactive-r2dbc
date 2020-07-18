package com.luisjrz.reactive.todo.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;


@Table("tasks")
@Generated
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {

	@Id
	private Long id;
	@NotBlank(message = "title is required")
	private String title;
	@NotBlank(message = "description is required")
	private String description;
	@NotNull(message = "date_to_complete must not be null")
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(value = "date_to_complete")
	private LocalDateTime dateToComplete;
	private boolean completed;

	
	
}
