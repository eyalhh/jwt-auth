package com.auth.example.models;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskResponse {
    private String name;
    private String description;
    private OffsetDateTime date;
    private String email;
}
