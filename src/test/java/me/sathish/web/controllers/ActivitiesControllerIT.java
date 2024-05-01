package me.sathish.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import me.sathish.common.AbstractIntegrationTest;
import me.sathish.entities.Activities;
import me.sathish.model.request.ActivitiesRequest;
import me.sathish.repositories.ActivitiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class ActivitiesControllerIT extends AbstractIntegrationTest {

    @Autowired
    private ActivitiesRepository activitiesRepository;

    private List<Activities> activitiesList = null;

    @BeforeEach
    void setUp() {
        activitiesRepository.deleteAllInBatch();

        activitiesList = new ArrayList<>();
        activitiesList.add(new Activities(
                null, new BigInteger("11146355759"), "2021-06-01", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest1"));
        activitiesList.add(new Activities(
                null,  new BigInteger("11146373918"), "2021-06-02", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest2"));
        activitiesList.add(new Activities(
                null,  new BigInteger("11146373919"), "2021-06-03", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest3"));
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
        Activities activities = activitiesList.get(0);
        Long activitiesId = activities.getId();

        this.mockMvc
                .perform(get("/api/activities/{id}", activitiesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activities.getId()), Long.class))
                .andExpect(jsonPath("$.activityName", is(activities.getActivityName())));
    }

    @Test
    void shouldCreateNewactivities() throws Exception {
        ActivitiesRequest activitiesRequest = new ActivitiesRequest(
                "11146373915", "2021-06-04", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest5");
        this.mockMvc
                .perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.activityName", is(activitiesRequest.activityName())));
    }

    @Test
    void shouldReturn400WhenCreateNewactivitiesWithoutText() throws Exception {
        ActivitiesRequest activitiesRequest = new ActivitiesRequest(
                null, "2021-06-04", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest5");
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
                .andExpect(jsonPath("$.violations[0].field", is("activityID")))
                .andExpect(jsonPath("$.violations[0].message", is("ActivityID cannot be empty")))
                .andReturn();
    }

    @Test
    void shouldUpdateactivities() throws Exception {
        Long activitiesId = activitiesList.get(0).getId();
        ActivitiesRequest activitiesRequest = new ActivitiesRequest(
                "11146355740", "2021-06-01", "Running", "Updated Running", "1:00:00", "10", "100", "200", "junitTest1");
        this.mockMvc
                .perform(put("/api/activities/{id}", activitiesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activitiesId), Long.class))
                .andExpect(jsonPath("$.activityName", is(activitiesRequest.activityName())));
    }

    @Test
    void shouldDeleteactivities() throws Exception {
        Activities activities = activitiesList.get(0);

        this.mockMvc
                .perform(delete("/api/activities/{id}", activities.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activities.getId()), Long.class))
                .andExpect(jsonPath("$.activityName", is(activities.getActivityName())));
    }
}
