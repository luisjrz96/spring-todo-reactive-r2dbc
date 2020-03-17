package com.luisjrz.reactive.todo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.luisjrz.reactive.todo.handler.TaskHandler;
import com.luisjrz.reactive.todo.model.Task;
import com.luisjrz.reactive.todo.repository.TaskRepository;
import com.luisjrz.reactive.todo.router.TaskRouter;
import com.luisjrz.reactive.todo.services.TaskServiceImpl;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers =  { TaskRouter.class, TaskHandler.class, TaskRepository.class, TaskServiceImpl.class})
@Import(TaskHandler.class)
public class SpringReactiveTodoApplicationTests {

	@MockBean
	private TaskRepository taskRepository;
	
	@Autowired
	private WebTestClient webClient;
	
	@Test
	public void saveTaskTest() {
		Task task = Task.builder()
				.id(3L)
				.title("Title test")
				.description("Description test")
				.expectedDate(LocalDate.of(2020, 03, 13))
				.completed(false)
				.build();
		when(taskRepository.save(task)).thenReturn(Mono.just(task));
		
		webClient.post()
			.uri("/tasks")
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(task))
			.exchange().expectStatus().isCreated();
		
		verify(taskRepository, times(1)).save(task);
	}
	
	@Test
	public void findTaskByIdTest() {
		Task task = new Task(4L, "FindById test", "Description test", LocalDate.of(2019, 1, 1), true);
		when(taskRepository.findById(task.getId())).thenReturn(Mono.just(task));
		
		webClient.get()
		.uri("/tasks/".concat(String.valueOf(task.getId())))
		.exchange()
		.expectStatus().isOk();
		
		verify(taskRepository, times(1)).findById(task.getId());
		
	}
	
	
	
	

}
