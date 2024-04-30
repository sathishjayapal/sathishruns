package me.sathish.web.controllers;

import jakarta.validation.Valid;
import java.net.URI;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/activities")
@Slf4j
@RequiredArgsConstructor
public class activitiesController {

    private final ActivitiesService activitiesService;

    @GetMapping
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
