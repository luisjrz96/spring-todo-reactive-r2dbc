package com.luisjrz.reactive.todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luisjrz.reactive.todo.model.Task;
import com.luisjrz.reactive.todo.repository.TaskRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public Flux<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public Mono<Task> findById(Long id) {
		return taskRepository.findById(id);
	}

	@Override
	public Mono<Task> save(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public Mono<Void> delete(Task task) {
		return taskRepository.delete(task);
	}
	
	
}
