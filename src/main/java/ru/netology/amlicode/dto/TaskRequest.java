package ru.netology.amlicode.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotBlank(message = "Название задачи обязательно")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String title;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    private boolean completed;
}
