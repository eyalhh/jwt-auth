package com.auth.example.services;

import com.auth.example.models.Task;
import com.auth.example.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepo;

    public Task createTask(Task task) {
        return taskRepo.save(task);
    }
}
