package me.sathish.services;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.sathish.entities.activities;
import me.sathish.exception.activitiesNotFoundException;
import me.sathish.mapper.activitiesMapper;
import me.sathish.model.query.FindactivitiessQuery;
import me.sathish.model.request.activitiesRequest;
import me.sathish.model.response.PagedResult;
import me.sathish.model.response.activitiesResponse;
import me.sathish.repositories.activitiesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class activitiesService {

    private final activitiesRepository activitiesRepository;
    private final activitiesMapper activitiesMapper;

    public PagedResult<activitiesResponse> findAllactivitiess(FindactivitiessQuery findactivitiessQuery) {

        // create Pageable instance
        Pageable pageable = createPageable(findactivitiessQuery);

        Page<activities> activitiessPage = activitiesRepository.findAll(pageable);

        List<activitiesResponse> activitiesResponseList = activitiesMapper.toResponseList(activitiessPage.getContent());

        return new PagedResult<>(activitiessPage, activitiesResponseList);
    }

    private Pageable createPageable(FindactivitiessQuery findactivitiessQuery) {
        int pageNo = Math.max(findactivitiessQuery.pageNo() - 1, 0);
        Sort sort = Sort.by(
                findactivitiessQuery.sortDir().equalsIgnoreCase(Sort.Direction.ASC.name())
                        ? Sort.Order.asc(findactivitiessQuery.sortBy())
                        : Sort.Order.desc(findactivitiessQuery.sortBy()));
        return PageRequest.of(pageNo, findactivitiessQuery.pageSize(), sort);
    }

    public Optional<activitiesResponse> findactivitiesById(Long id) {
        return activitiesRepository.findById(id).map(activitiesMapper::toResponse);
    }

    @Transactional
    public activitiesResponse saveactivities(activitiesRequest activitiesRequest) {
        activities activities = activitiesMapper.toEntity(activitiesRequest);
        activities savedactivities = activitiesRepository.save(activities);
        return activitiesMapper.toResponse(savedactivities);
    }

    @Transactional
    public activitiesResponse updateactivities(Long id, activitiesRequest activitiesRequest) {
        activities activities =
                activitiesRepository.findById(id).orElseThrow(() -> new activitiesNotFoundException(id));

        // Update the activities object with data from activitiesRequest
        activitiesMapper.mapactivitiesWithRequest(activities, activitiesRequest);

        // Save the updated activities object
        activities updatedactivities = activitiesRepository.save(activities);

        return activitiesMapper.toResponse(updatedactivities);
    }

    @Transactional
    public void deleteactivitiesById(Long id) {
        activitiesRepository.deleteById(id);
    }
}
