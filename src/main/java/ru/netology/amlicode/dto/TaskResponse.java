package ru.netology.amlicode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
