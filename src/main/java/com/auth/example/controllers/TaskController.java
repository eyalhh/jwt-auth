package com.auth.example.controllers;

import com.auth.example.models.*;
import com.auth.example.services.TaskService;
import com.auth.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;

    @PostMapping("/create-task")
    public CreateTaskResponse securedResource(@RequestBody CreateTaskRequest task) {
        MyUserDetails userDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        Task createdTask = Task.builder()
                .name(task.getName())
                .description(task.getDescription())
                .date(OffsetDateTime.now(ZoneOffset.UTC).withNano(0))
                .creator(user)
                .build();

        taskService.createTask(createdTask);

        return CreateTaskResponse.builder()
                .name(task.getName())
                .description(task.getDescription())
                .date(createdTask.getDate())
                .email(user.getEmail())
                .build();

    }

    @GetMapping("/tasks")
    public List<CreateTaskResponse> getAllTasks() {
        MyUserDetails userDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        return userService.getUserTasks(user).stream()
                .map(task -> CreateTaskResponse.builder()
                    .name(task.getName())
                    .description(task.getDescription())
                    .date(task.getDate())
                    .email(task.getCreator().getEmail())
                    .build())
                .collect(Collectors.toList());

    }
}
