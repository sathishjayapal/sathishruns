package me.sathish.services;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.sathish.entities.Activities;
import me.sathish.exception.activitiesNotFoundException;
import me.sathish.mapper.ActivitiesMapper;
import me.sathish.model.query.FindactivitiessQuery;
import me.sathish.model.request.ActivitiesRequest;
import me.sathish.model.response.ActivitiesResponse;
import me.sathish.model.response.PagedResult;
import me.sathish.repositories.ActivitiesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivitiesService {

    private final ActivitiesRepository activitiesRepository;
    private final ActivitiesMapper activitiesMapper;

    public PagedResult<ActivitiesResponse> findAllactivitiess(FindactivitiessQuery findactivitiessQuery) {

        // create Pageable instance
        Pageable pageable = createPageable(findactivitiessQuery);

        Page<Activities> activitiessPage = activitiesRepository.findAll(pageable);

        List<ActivitiesResponse> activitiesResponseList = activitiesMapper.toResponseList(activitiessPage.getContent());

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

    public Optional<ActivitiesResponse> findactivitiesById(Long id) {
        return activitiesRepository.findById(id).map(activitiesMapper::toResponse);
    }

    @Transactional
    public ActivitiesResponse saveactivities(ActivitiesRequest activitiesRequest) {
        Activities activities = activitiesMapper.toEntity(activitiesRequest);
        Activities savedactivities = activitiesRepository.save(activities);
        return activitiesMapper.toResponse(savedactivities);
    }

    @Transactional
    public ActivitiesResponse updateactivities(Long id, ActivitiesRequest activitiesRequest) {
        Activities activities =
                activitiesRepository.findById(id).orElseThrow(() -> new activitiesNotFoundException(id));

        // Update the Activities object with data from ActivitiesRequest
        activitiesMapper.mapactivitiesWithRequest(activities, activitiesRequest);

        // Save the updated Activities object
        Activities updatedactivities = activitiesRepository.save(activities);

        return activitiesMapper.toResponse(updatedactivities);
    }

    @Transactional
    public void deleteactivitiesById(Long id) {
        activitiesRepository.deleteById(id);
    }
}
