package com.luisjrz.reactive.todo.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.luisjrz.reactive.todo.handler.TaskHandler;

@Configuration
public class TaskRouter {

	@Bean
	public RouterFunction<ServerResponse> taskRoutes(TaskHandler taskHanlder){
		return RouterFunctions.route(RequestPredicates.GET("/tasks"), taskHanlder::findAllTasks)
				.andRoute(RequestPredicates.GET("/tasks/{id}"), taskHanlder::findTaskById)
				.andRoute(RequestPredicates.POST("/tasks"), taskHanlder::saveTask);
	}
}
