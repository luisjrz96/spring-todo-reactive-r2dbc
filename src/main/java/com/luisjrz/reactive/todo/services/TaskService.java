package com.luisjrz.reactive.todo.services;

import com.luisjrz.reactive.todo.model.Task;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
	
	public Flux<Task> findAll();
	public Mono<Task> findById(Long id);
	public Mono<Task> save(Task task);
	public Mono<Void> delete(Task task);
	
}
