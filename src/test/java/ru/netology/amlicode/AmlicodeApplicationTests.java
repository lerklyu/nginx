
package ru.netology.amlicode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AmlicodeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    // Создание задачи
    @Test
    void shouldCreateTask() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Купить молоко",
                                    "description": "Купить молоко в магазине",
                                    "completed": false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Купить молоко"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    // Получение всех задач
    @Test
    void shouldGetAllTasks() throws Exception {
        createTask("Тест получения списка");

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("Тест получения списка")
                ));
    }

    // Получение задачи по ID
    @Test
    void shouldGetTaskById() throws Exception {
        Long id = createTask("Тест получения по ID");

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Тест получения по ID"));
    }

    // Обновление задачи
    @Test
    void shouldUpdateTask() throws Exception {
        Long id = createTask("Старое название");

        mockMvc.perform(put("/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Новое название",
                                    "description": "Обновленное описание",
                                    "completed": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Новое название"))
                .andExpect(jsonPath("$.description").value("Обновленное описание"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    // Удаление задачи
    @Test
    void shouldDeleteTask() throws Exception {
        Long id = createTask("Задача для удаления");

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isNotFound());
    }

    // Проверка ошибки при отсутствии задачи
    @Test
    void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    // Проверка валидации
    @Test
    void shouldReturnBadRequestWhenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "   ",
                                    "description": "Описание",
                                    "completed": false
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ошибка валидации"));
    }

    // Вспомогательный метод для создания тестовых задач
    private Long createTask(String title) throws Exception {
        MvcResult result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "%s",
                                    "description": "Тестовое описание",
                                    "completed": false
                                }
                                """.formatted(title)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Matcher matcher = Pattern.compile("\"id\":(\\d+)")
                .matcher(response);

        if (!matcher.find()) {
            throw new IllegalStateException("ID задачи не найден в ответе");
        }

        return Long.parseLong(matcher.group(1));
    }
}
