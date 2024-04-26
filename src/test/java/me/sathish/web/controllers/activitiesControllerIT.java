package me.sathish.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import me.sathish.common.AbstractIntegrationTest;
import me.sathish.entities.activities;
import me.sathish.model.request.activitiesRequest;
import me.sathish.repositories.activitiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class activitiesControllerIT extends AbstractIntegrationTest {

    @Autowired
    private activitiesRepository activitiesRepository;

    private List<activities> activitiesList = null;

    @BeforeEach
    void setUp() {
        activitiesRepository.deleteAllInBatch();

        activitiesList = new ArrayList<>();
        activitiesList.add(new activities(null, "First activities"));
        activitiesList.add(new activities(null, "Second activities"));
        activitiesList.add(new activities(null, "Third activities"));
        activitiesList = activitiesRepository.saveAll(activitiesList);
    }

    @Test
    void shouldFetchAllactivitiess() throws Exception {
        this.mockMvc
                .perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()", is(activitiesList.size())))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.isFirst", is(true)))
                .andExpect(jsonPath("$.isLast", is(true)))
                .andExpect(jsonPath("$.hasNext", is(false)))
                .andExpect(jsonPath("$.hasPrevious", is(false)));
    }

    @Test
    void shouldFindactivitiesById() throws Exception {
        activities activities = activitiesList.get(0);
        Long activitiesId = activities.getId();

        this.mockMvc
                .perform(get("/api/activities/{id}", activitiesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activities.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(activities.getText())));
    }

    @Test
    void shouldCreateNewactivities() throws Exception {
        activitiesRequest activitiesRequest = new activitiesRequest("New activities");
        this.mockMvc
                .perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(activitiesRequest.text())));
    }

    @Test
    void shouldReturn400WhenCreateNewactivitiesWithoutText() throws Exception {
        activitiesRequest activitiesRequest = new activitiesRequest(null);

        this.mockMvc
                .perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(jsonPath("$.type", is("about:blank")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Invalid request content.")))
                .andExpect(jsonPath("$.instance", is("/api/activities")))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("text")))
                .andExpect(jsonPath("$.violations[0].message", is("Text cannot be empty")))
                .andReturn();
    }

    @Test
    void shouldUpdateactivities() throws Exception {
        Long activitiesId = activitiesList.get(0).getId();
        activitiesRequest activitiesRequest = new activitiesRequest("Updated activities");

        this.mockMvc
                .perform(put("/api/activities/{id}", activitiesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activitiesId), Long.class))
                .andExpect(jsonPath("$.text", is(activitiesRequest.text())));
    }

    @Test
    void shouldDeleteactivities() throws Exception {
        activities activities = activitiesList.get(0);

        this.mockMvc
                .perform(delete("/api/activities/{id}", activities.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activities.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(activities.getText())));
    }
}
