package com.auth.example.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTaskRequest {
    @NotEmpty(message = "task name is required")
    @Size(max = 255, message = "name must be at max 255 chars")
    private String name;

    @Size(max = 255, message = "description must be at max 255 chars")
    private String description;
}
