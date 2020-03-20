package com.luisjrz.reactive.todo.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.luisjrz.reactive.todo.model.Task;
import com.luisjrz.reactive.todo.services.TaskService;
import com.luisjrz.reactive.todo.services.ValidatorService;

import reactor.core.publisher.Mono;

@Component
public class TaskHandler {

	@Autowired
	private TaskService taskService;

	@Autowired
	private ValidatorService validator;

	public Mono<ServerResponse> findAllTasks(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(taskService.findAll(), Task.class);
	}

	public Mono<ServerResponse> findTaskById(ServerRequest request) {
		Long taskId = -1L;
		try {
			String id = request.pathVariable("id");
			taskId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			return ServerResponse.badRequest().build();
		}
		return taskService.findById(taskId).flatMap(
				task -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(task)))
				.switchIfEmpty(ServerResponse.notFound().build());

	}

	public Mono<ServerResponse> saveTask(ServerRequest request) {
		Mono<Task> taskToSave = request.bodyToMono(Task.class);

		return taskToSave.flatMap(task -> {
			if (validator.hasErrors(task, Task.class.getName())) {
				return ServerResponse.badRequest().body(BodyInserters.fromValue(validator.getErrors(task, Task.class.getName())));
			}
			return taskService.save(task)
					.flatMap(t -> ServerResponse.created(URI.create("/tasks/".concat(t.getId().toString())))
							.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(t)));

		});
	}

	public Mono<ServerResponse> updateTask(ServerRequest request) {
		Mono<Task> taskDetails = request.bodyToMono(Task.class);
		Long taskId = -1L;
		try {
			String id = request.pathVariable("id");
			taskId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			return ServerResponse.badRequest().build();
		}
		return taskService.findById(taskId).zipWith(taskDetails, (db, req) -> {
			db.setTitle(req.getTitle());
			db.setDescription(req.getDescription());
			db.setExpectedDate(req.getExpectedDate());
			db.setCompleted(req.isCompleted());
			return db;
		}).flatMap(task -> {
			if (validator.hasErrors(task, Task.class.getName())) {
				return ServerResponse.badRequest().body(BodyInserters.fromValue(validator.getErrors(task, Task.class.getName())));
			}
			return ServerResponse.created(URI.create("/tasks/".concat(String.valueOf(task.getId()))))
					.contentType(MediaType.APPLICATION_JSON).body(taskService.save(task), Task.class);
		}).switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> delete(ServerRequest request) {
		Long taskId = -1L;
		try {
			String id = request.pathVariable("id");
			taskId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			return ServerResponse.badRequest().build();
		}
		return taskService.findById(taskId).flatMap(task -> {
			return taskService.delete(task).then(ServerResponse.ok().build());
		}).switchIfEmpty(ServerResponse.notFound().build());

	}
}
