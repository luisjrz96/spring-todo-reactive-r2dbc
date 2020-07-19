package com.luisjrz.reactive.todo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Arrays;

import java.time.LocalDateTime;

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
import com.luisjrz.reactive.todo.services.ValidatorService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = { TaskRouter.class, TaskHandler.class, TaskRepository.class, TaskServiceImpl.class,
		ValidatorService.class })
@Import(TaskHandler.class)
public class SpringReactiveTodoApplicationTests {

	@MockBean
	private TaskRepository taskRepository;

	@Autowired
	private WebTestClient webClient;

	@Test
	public void saveTaskTest() {
		Task task = Task.builder().id(3L).title("Title test").description("Description test")
				.user("Foo")
				.dateToComplete(LocalDateTime.of(2020, 03, 13, 0, 0, 0)).completed(false).build();

		when(taskRepository.save(task)).thenReturn(Mono.just(task));

		webClient.post().uri("/tasks").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)

				.body(BodyInserters.fromValue(task)).exchange().expectStatus().isCreated().expectBody(Task.class);

		verify(taskRepository, times(1)).save(task);
	}

	@Test
	public void saveTaskConstraintTest() {
		Task task = Task.builder().id(3L).title("").description("Description test")
				.dateToComplete(LocalDateTime.of(2020, 03, 13, 0, 0, 0)).completed(false).build();

		webClient.post().uri("/tasks").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(task)).exchange().expectStatus().isBadRequest();

	}

	@Test
	public void updateTaskTest() {
		Task task = Task.builder().id(10L).title("Title test").description("Description test")
				.user("Foo")
				.dateToComplete(LocalDateTime.of(2020, 03, 13, 0, 0, 0)).completed(false).build();

		Task taskToUpdate = Task.builder().id(10L).title("Title test").description("Description test")
				.user("Foo")
				.dateToComplete(LocalDateTime.of(2020, 03, 13, 0, 0, 0)).completed(true).build();

		when(taskRepository.findById(task.getId())).thenReturn(Mono.just(task));
		when(taskRepository.save(taskToUpdate)).thenReturn(Mono.just(taskToUpdate));

		webClient.put().uri("/tasks/".concat(task.getId().toString())).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)

				.body(BodyInserters.fromValue(taskToUpdate)).exchange().expectStatus().isCreated()
				.expectBody(Task.class);

		verify(taskRepository, times(1)).findById(task.getId());
		verify(taskRepository, times(1)).save(taskToUpdate);

	}

	@Test
	public void updateTaskConstraintsTest() {
		Task task = Task.builder().id(3L).title("Title test").description("Description test")
				.dateToComplete(LocalDateTime.of(2020, 03, 13, 0, 0, 0)).completed(false).build();

		Task taskToUpdate = Task.builder().id(3L).title("").description("Description test")
				.dateToComplete(LocalDateTime.of(2020, 03, 13, 0, 0, 0)).completed(true).build();

		when(taskRepository.findById(task.getId())).thenReturn(Mono.just(task));

		webClient.put().uri("/tasks/".concat(task.getId().toString())).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(taskToUpdate)).exchange()
				.expectStatus().isBadRequest();

		verify(taskRepository, times(1)).findById(task.getId());

	}

	@Test
	public void updateTaskInvalidIdTest() {
		String INVALID_ID = "INVALID_ID";
		Task taskToUpdate = Task.builder().id(3L).title("").description("Description test")
				.dateToComplete(LocalDateTime.of(2020, 03, 13, 0, 0, 0)).completed(true).build();

		webClient.put().uri("/tasks/".concat(INVALID_ID)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(taskToUpdate)).exchange()
				.expectStatus().isBadRequest();

	}

	@Test
	public void findTaskByIdTest() {
		Task task = new Task(4L, "Foo", "FindById test", "Description test", LocalDateTime.of(2019, 1, 1, 0, 0, 0), true);
		when(taskRepository.findById(task.getId())).thenReturn(Mono.just(task));

		webClient.get().uri("/tasks/".concat(String.valueOf(task.getId()))).exchange().expectStatus().isOk();

		verify(taskRepository, times(1)).findById(task.getId());

	}

	@Test
	public void findAllTasks() {
		Task task1 = new Task(1L, "Foo", "FindById test 1", "Description test 1", LocalDateTime.of(2019, 1, 1, 0, 0, 0), true);
		Task task2 = new Task(2L, "Bar", "FindById test 2", "Description test 2", LocalDateTime.of(2019, 1, 2, 0, 0, 0), true);
		Task task3 = new Task(3L, "Do", "FindById test 3", "Description test 3", LocalDateTime.of(2019, 1, 3, 0, 0, 0), true);
		List<Task> tasks = Arrays.asList(task1, task2, task3);
		when(taskRepository.findAll()).thenReturn(Flux.fromIterable(tasks));

		webClient.get().uri("/tasks").exchange().expectStatus().isOk();

		verify(taskRepository, times(1)).findAll();

	}

	@Test
	public void deleteTaskTest() {
		Task taskToDelete = new Task();
		taskToDelete.setId(5L);
		taskToDelete.setTitle("Test title");
		taskToDelete.setDescription("Test description");
		taskToDelete.setDateToComplete(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
		taskToDelete.setCompleted(true);
		Mono<Void> monoVoid = Mono.empty();

		when(taskRepository.findById(taskToDelete.getId())).thenReturn(Mono.just(taskToDelete));

		when(taskRepository.delete(taskToDelete)).thenReturn(monoVoid);

		webClient.delete().uri("/tasks/".concat(String.valueOf(taskToDelete.getId()))).exchange().expectStatus().isOk();
		verify(taskRepository, times(1)).findById(taskToDelete.getId());
		verify(taskRepository, times(1)).delete(taskToDelete);

	}

	@Test
	public void deleteTaskTestBadRequest() {
		webClient.delete().uri("/tasks/".concat("INVALID_ID")).exchange().expectStatus().isBadRequest();
	}

	@Test
	public void findTaskTestBadRequest() {
		webClient.get().uri("/tasks/".concat("INVALID_ID")).exchange().expectStatus().isBadRequest();
	}

	@Test
	public void findTaskTestNotFound() {
		Long taskId = 10L;

		when(taskRepository.findById(taskId)).thenReturn(Mono.empty());
		webClient.get().uri("/tasks/".concat(taskId.toString())).exchange().expectStatus().isNotFound();

		verify(taskRepository, times(1)).findById(taskId);
	}

	@Test
	public void deleteTaskTestNotFound() {
		Long taskId = 10L;
		when(taskRepository.findById(taskId)).thenReturn(Mono.empty());
		webClient.delete().uri("/tasks/".concat(taskId.toString())).exchange().expectStatus().isNotFound();

		verify(taskRepository, times(1)).findById(taskId);
	}

	@Test
	public void findAllTaskBetweenDates() {
		Task task1 = new Task(1L, "Foo", "FindById test 1", "Description test 1", LocalDateTime.of(2019, 1, 1, 1, 15, 0),
				true);
		Task task2 = new Task(2L, "Bar", "FindById test 2", "Description test 2", LocalDateTime.of(2019, 1, 2, 1, 30, 0),
				true);
		Task task3 = new Task(3L, "Do", "FindById test 3", "Description test 3", LocalDateTime.of(2019, 1, 3, 1, 45, 0),
				true);
		List<Task> tasks = Arrays.asList(task1, task2, task3);
		LocalDateTime startDate = LocalDateTime.of(2019, 1, 1, 1, 0);
		LocalDateTime endDate = LocalDateTime.of(2019, 1, 1, 2, 0);
		when(taskRepository.findBetweenDates(startDate, endDate)).thenReturn(Flux.fromIterable(tasks));

		String uriToTest = String.format(
				"?startYear=%d&startMonth=%d&startDay=%d&startHour=%d&startMinute=%d"
						+ "&endYear=%d&endMonth=%d&endDay=%d&endHour=%d&endMinute=%d",
				startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), startDate.getHour(),
				startDate.getMinute(), endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth(),
				endDate.getHour(), endDate.getMinute());

		webClient.get().uri("/tasks/date".concat(uriToTest)).exchange().expectStatus().isOk();

		verify(taskRepository, times(1)).findBetweenDates(startDate, endDate);

	}

	@Test
	public void findAllTaskBetweenDatesOnlyYear() {
		Task task1 = new Task(1L, "Foo", "FindById test 1", "Description test 1", LocalDateTime.of(2018, 1, 1, 0, 0, 0), true);
		Task task2 = new Task(2L, "Bar", "FindById test 2", "Description test 2", LocalDateTime.of(2019, 1, 1, 0, 0, 0), true);
		Task task3 = new Task(3L, "Do", "FindById test 3", "Description test 3", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
				false);
		List<Task> tasks = Arrays.asList(task1, task2, task3);
		LocalDateTime startDate = LocalDateTime.of(2018, 1, 1, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, 1, 1, 0, 0);
		when(taskRepository.findBetweenDates(startDate, endDate)).thenReturn(Flux.fromIterable(tasks));

		String uriToTest = String.format("?startYear=%d&endYear=%d", startDate.getYear(), endDate.getYear());

		webClient.get().uri("/tasks/date".concat(uriToTest)).exchange().expectStatus().isOk();
		verify(taskRepository, times(1)).findBetweenDates(startDate, endDate);

	}

}
