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

import reactor.core.publisher.Mono;

@Component
public class TaskHandler {

	@Autowired
	private TaskService taskService;

	public Mono<ServerResponse> findAllTasks(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(taskService.findAll(), Task.class);
	}

	public Mono<ServerResponse> findTaskById(ServerRequest request) {
		String id = request.pathVariable("id");
		Long taskId = -1L;
		try {
			taskId = Long.parseLong(id);
		} catch (Exception e) {
			return ServerResponse.notFound().build();
		}
		return taskService.findById(taskId).flatMap(task -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(task)).switchIfEmpty(ServerResponse.notFound().build()));
	}

	public Mono<ServerResponse> saveTask(ServerRequest request) {
		Mono<Task> taskToSave = request.bodyToMono(Task.class);
		return taskToSave.flatMap(task -> {
			return taskService.save(task)
					.then(ServerResponse.
							created(URI.create("/tasks/".concat(String.valueOf(task.getId()))))
							.body(BodyInserters.fromObject(taskToSave), Task.class))
					.switchIfEmpty(ServerResponse.badRequest().build());
		});
	}
}
