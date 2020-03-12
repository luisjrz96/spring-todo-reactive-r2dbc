package com.luisjrz.reactive.todo.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.luisjrz.reactive.todo.model.Task;


public interface TaskRepository extends R2dbcRepository<Task, Long>{

}
