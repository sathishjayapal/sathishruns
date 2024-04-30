package me.sathish.web.controllers;

import static me.sathish.utils.AppConstants.PROFILE_TEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.sathish.entities.Activities;
import me.sathish.exception.activitiesNotFoundException;
import me.sathish.model.query.FindactivitiessQuery;
import me.sathish.model.request.ActivitiesRequest;
import me.sathish.model.response.ActivitiesResponse;
import me.sathish.model.response.PagedResult;
import me.sathish.services.ActivitiesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = activitiesController.class)
@ActiveProfiles(PROFILE_TEST)
class ActivitiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivitiesService activitiesService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Activities> activitiesList;

    @BeforeEach
    void setUp() {
        this.activitiesList = new ArrayList<>();
        activitiesList.add(new Activities(
                1L, "11146355759", "2021-06-01", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest1"));
        activitiesList.add(new Activities(
                2L, "11146373918", "2021-06-02", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest2"));
        activitiesList.add(new Activities(
                3L, "11146373919", "2021-06-03", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest3"));
    }

    @Test
    void shouldFetchAllactivitiess() throws Exception {

        Page<Activities> page = new PageImpl<>(activitiesList);
        PagedResult<ActivitiesResponse> activitiesPagedResult = new PagedResult<>(page, getactivitiesResponseList());
        FindactivitiessQuery findactivitiessQuery = new FindactivitiessQuery(0, 10, "id", "asc");
        given(activitiesService.findAllactivitiess(findactivitiessQuery)).willReturn(activitiesPagedResult);

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
        Long activitiesId = 1L;
        ActivitiesResponse activities = new ActivitiesResponse(
                activitiesId,
                "11146355759",
                "2021-06-01",
                "Running",
                "Running",
                "1:00:00",
                "10",
                "100",
                "200",
                "junitTest1");
        given(activitiesService.findactivitiesById(activitiesId)).willReturn(Optional.of(activities));
        this.mockMvc
                .perform(get("/api/activities/{id}", activitiesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(activities.activityDescription())));
    }

    @Test
    void shouldReturn404WhenFetchingNonExistingactivities() throws Exception {
        Long activitiesId = 1L;
        given(activitiesService.findactivitiesById(activitiesId)).willReturn(Optional.empty());
        this.mockMvc
                .perform(get("/api/activities/{id}", activitiesId))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", is(MediaType.APPLICATION_PROBLEM_JSON_VALUE)))
                .andExpect(jsonPath("$.type", is("http://api.sathishruns.com/errors/not-found")))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail").value("Activities with Id '%d' not found".formatted(activitiesId)));
    }

    @Test
    void shouldCreateNewactivities() throws Exception {
        ActivitiesResponse activities = new ActivitiesResponse(
                1L, "11146355759", "2021-06-01", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest1");
        ActivitiesRequest activitiesRequest = new ActivitiesRequest(
                "11146355759", "2021-06-01", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest1");
        given(activitiesService.saveactivities(any(ActivitiesRequest.class))).willReturn(activities);

        this.mockMvc
                .perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(activities.activityDescription())));
    }

    @Test
    void shouldReturn400WhenCreateNewactivitiesWithoutText() throws Exception {
        ActivitiesRequest activitiesRequest =
                new ActivitiesRequest(null, null, null, null, null, null, null, null, null);
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
                .andExpect(jsonPath("$.instance", is("/api/Activities")))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("text")))
                .andExpect(jsonPath("$.violations[0].message", is("Text cannot be empty")))
                .andReturn();
    }

    @Test
    void shouldUpdateactivities() throws Exception {
        Long activitiesId = 1L;
        ActivitiesResponse activities = new ActivitiesResponse(
                activitiesId,
                "11146355759",
                "2021-06-01",
                "Running",
                "Running",
                "1:00:00",
                "10",
                "100",
                "200",
                "junitTest1");
        ActivitiesRequest activitiesRequest = new ActivitiesRequest(
                "11146355759", "2021-06-01", "Running", "Running", "1:00:00", "10", "100", "200", "junitTest14");
        given(activitiesService.updateactivities(eq(activitiesId), any(ActivitiesRequest.class)))
                .willReturn(activities);
        this.mockMvc
                .perform(put("/api/activities/{id}", activitiesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activitiesId), Long.class))
                .andExpect(jsonPath("$.text", is(activities.activityDescription())));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingactivities() throws Exception {
        Long activitiesId = 1L;
        ActivitiesRequest activitiesRequest =
                new ActivitiesRequest("11146355759", null, null, null, null, null, null, null, null);
        given(activitiesService.updateactivities(eq(activitiesId), any(ActivitiesRequest.class)))
                .willThrow(new activitiesNotFoundException(activitiesId));

        this.mockMvc
                .perform(put("/api/activities/{id}", activitiesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", is(MediaType.APPLICATION_PROBLEM_JSON_VALUE)))
                .andExpect(jsonPath("$.type", is("http://api.sathishruns.com/errors/not-found")))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail").value("Activities with Id '%d' not found".formatted(activitiesId)));
    }

    @Test
    void shouldDeleteactivities() throws Exception {
        Long activitiesId = 1L;
        ActivitiesResponse activities = new ActivitiesResponse(
                activitiesId,
                "11146355759",
                "2021-06-01",
                "Running",
                "Running",
                "1:00:00",
                "10",
                "100",
                "200",
                "junitTest1");
        given(activitiesService.findactivitiesById(activitiesId)).willReturn(Optional.of(activities));
        doNothing().when(activitiesService).deleteactivitiesById(activitiesId);

        this.mockMvc
                .perform(delete("/api/activities/{id}", activitiesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(activities.activityName())));
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingactivities() throws Exception {
        Long activitiesId = 1L;
        given(activitiesService.findactivitiesById(activitiesId)).willReturn(Optional.empty());

        this.mockMvc
                .perform(delete("/api/activities/{id}", activitiesId))
                .andExpect(header().string("Content-Type", is(MediaType.APPLICATION_PROBLEM_JSON_VALUE)))
                .andExpect(jsonPath("$.type", is("http://api.sathishruns.com/errors/not-found")))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail").value("Activities with Id '%d' not found".formatted(activitiesId)));
    }

    List<ActivitiesResponse> getactivitiesResponseList() {
        return activitiesList.stream()
                .map(activities -> new ActivitiesResponse(
                        activities.getId(),
                        activities.getActivityName(),
                        activities.getActivityDate(),
                        activities.getActivityType(),
                        activities.getActivityDescription(),
                        activities.getElapsedTime(),
                        activities.getDistance(),
                        activities.getCalories(),
                        activities.getMaxHeartRate(),
                        activities.getActivityName()))
                .toList();
    }
}
