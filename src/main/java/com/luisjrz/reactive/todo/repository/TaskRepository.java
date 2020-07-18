package com.luisjrz.reactive.todo.repository;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.luisjrz.reactive.todo.model.Task;

import reactor.core.publisher.Flux;

public interface TaskRepository extends R2dbcRepository<Task, Long> {
	
	@Query("SELECT * FROM tasks t WHERE t.date_to_complete BETWEEN :startDate AND :endDate")
	Flux<Task> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

}
