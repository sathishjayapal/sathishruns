package me.sathish.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sathish.exception.activitiesNotFoundException;
import me.sathish.model.query.FindactivitiessQuery;
import me.sathish.model.request.ActivitiesRequest;
import me.sathish.model.response.ActivitiesResponse;
import me.sathish.model.response.PagedResult;
import me.sathish.services.ActivitiesService;
import me.sathish.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/activities")
@Slf4j
@RequiredArgsConstructor
public class ActivitiesController {

    private final ActivitiesService activitiesService;

    @GetMapping // /api/activities?pageNo=0&pageSize=10&sortBy=id&sortDir=asc
    public PagedResult<ActivitiesResponse> getAllactivitiess(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)
                    int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false)
                    int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)
                    String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)
                    String sortDir) {
        FindactivitiessQuery findactivitiessQuery = new FindactivitiessQuery(pageNo, pageSize, sortBy, sortDir);
        return activitiesService.findAllactivitiess(findactivitiessQuery);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivitiesResponse> getactivitiesById(@PathVariable Long id) {
        return activitiesService
                .findactivitiesById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new activitiesNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<ActivitiesResponse> createactivities(
            @RequestBody @Validated ActivitiesRequest activitiesRequest) {
        ActivitiesResponse response = activitiesService.saveactivities(activitiesRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/api/Activities/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivitiesResponse> updateactivities(
            @PathVariable Long id, @RequestBody @Valid ActivitiesRequest activitiesRequest) {
        return ResponseEntity.ok(activitiesService.updateactivities(id, activitiesRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActivitiesResponse> deleteactivities(@PathVariable Long id) {
        return activitiesService
                .findactivitiesById(id)
                .map(activities -> {
                    activitiesService.deleteactivitiesById(id);
                    return ResponseEntity.ok(activities);
                })
                .orElseThrow(() -> new activitiesNotFoundException(id));
    }
}
