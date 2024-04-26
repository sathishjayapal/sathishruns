package me.sathish.web.controllers;

import static me.sathish.utils.AppConstants.PROFILE_TEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.sathish.entities.activities;
import me.sathish.exception.activitiesNotFoundException;
import me.sathish.model.query.FindactivitiessQuery;
import me.sathish.model.request.activitiesRequest;
import me.sathish.model.response.PagedResult;
import me.sathish.model.response.activitiesResponse;
import me.sathish.services.activitiesService;
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
class activitiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private activitiesService activitiesService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<activities> activitiesList;

    @BeforeEach
    void setUp() {
        this.activitiesList = new ArrayList<>();
        this.activitiesList.add(new activities(1L, "text 1"));
        this.activitiesList.add(new activities(2L, "text 2"));
        this.activitiesList.add(new activities(3L, "text 3"));
    }

    @Test
    void shouldFetchAllactivitiess() throws Exception {

        Page<activities> page = new PageImpl<>(activitiesList);
        PagedResult<activitiesResponse> activitiesPagedResult = new PagedResult<>(page, getactivitiesResponseList());
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
        activitiesResponse activities = new activitiesResponse(activitiesId, "text 1");
        given(activitiesService.findactivitiesById(activitiesId)).willReturn(Optional.of(activities));

        this.mockMvc
                .perform(get("/api/activities/{id}", activitiesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(activities.text())));
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
                .andExpect(jsonPath("$.detail").value("activities with Id '%d' not found".formatted(activitiesId)));
    }

    @Test
    void shouldCreateNewactivities() throws Exception {

        activitiesResponse activities = new activitiesResponse(1L, "some text");
        activitiesRequest activitiesRequest = new activitiesRequest("some text");
        given(activitiesService.saveactivities(any(activitiesRequest.class))).willReturn(activities);

        this.mockMvc
                .perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(activities.text())));
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
        Long activitiesId = 1L;
        activitiesResponse activities = new activitiesResponse(activitiesId, "Updated text");
        activitiesRequest activitiesRequest = new activitiesRequest("Updated text");
        given(activitiesService.updateactivities(eq(activitiesId), any(activitiesRequest.class)))
                .willReturn(activities);

        this.mockMvc
                .perform(put("/api/activities/{id}", activitiesId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activitiesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activitiesId), Long.class))
                .andExpect(jsonPath("$.text", is(activities.text())));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingactivities() throws Exception {
        Long activitiesId = 1L;
        activitiesRequest activitiesRequest = new activitiesRequest("Updated text");
        given(activitiesService.updateactivities(eq(activitiesId), any(activitiesRequest.class)))
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
                .andExpect(jsonPath("$.detail").value("activities with Id '%d' not found".formatted(activitiesId)));
    }

    @Test
    void shouldDeleteactivities() throws Exception {
        Long activitiesId = 1L;
        activitiesResponse activities = new activitiesResponse(activitiesId, "Some text");
        given(activitiesService.findactivitiesById(activitiesId)).willReturn(Optional.of(activities));
        doNothing().when(activitiesService).deleteactivitiesById(activitiesId);

        this.mockMvc
                .perform(delete("/api/activities/{id}", activitiesId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(activities.text())));
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
                .andExpect(jsonPath("$.detail").value("activities with Id '%d' not found".formatted(activitiesId)));
    }

    List<activitiesResponse> getactivitiesResponseList() {
        return activitiesList.stream()
                .map(activities -> new activitiesResponse(activities.getId(), activities.getText()))
                .toList();
    }
}
