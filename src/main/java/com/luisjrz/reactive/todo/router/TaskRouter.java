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
	public RouterFunction<ServerResponse> taskRoutes(TaskHandler taskHandler){
		return RouterFunctions.route(RequestPredicates.GET("/tasks"), taskHandler::findAllTasks)
                .andRoute(RequestPredicates.GET("/tasks/date"), taskHandler::findAllTasksBetweenDates)
				.andRoute(RequestPredicates.POST("/tasks"), taskHandler::saveTask)
				.andRoute(RequestPredicates.PUT("/tasks/{id}"), taskHandler::updateTask)
				.andRoute(RequestPredicates.GET("/tasks/{id}"), taskHandler::findTaskById)
				.andRoute(RequestPredicates.DELETE("/tasks/{id}"), taskHandler::delete);
	}
}
