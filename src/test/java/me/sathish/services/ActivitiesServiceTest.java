package me.sathish.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Optional;
import me.sathish.entities.Activities;
import me.sathish.mapper.activitiesMapper;
import me.sathish.model.response.ActivitiesResponse;
import me.sathish.repositories.activitiesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActivitiesServiceTest {

    @Mock
    private activitiesRepository activitiesRepository;

    @Mock
    private activitiesMapper activitiesMapper;

    @InjectMocks
    private ActivitiesService activitiesService;

    @Test
    void findactivitiesById() {
        // given
        given(activitiesRepository.findById(1L)).willReturn(Optional.of(getactivities()));
        given(activitiesMapper.toResponse(any(Activities.class))).willReturn(getactivitiesResponse());
        // when
        Optional<ActivitiesResponse> optionalactivities = activitiesService.findactivitiesById(1L);
        // then
        assertThat(optionalactivities).isPresent();
        ActivitiesResponse activities = optionalactivities.get();
        assertThat(activities.id()).isEqualTo(1L);
        assertThat(activities.activityID()).isEqualTo("junitTest");
    }

    @Test
    void deleteactivitiesById() {
        // given
        willDoNothing().given(activitiesRepository).deleteById(1L);
        // when
        activitiesService.deleteactivitiesById(1L);
        // then
        verify(activitiesRepository, times(1)).deleteById(1L);
    }

    private Activities getactivities() {
        Activities activities = new Activities();
        activities.setId(1L);
        activities.setActivityID("11146355759");
        activities.setActivityDate("2021-06-01");
        activities.setActivityType("Running");
        activities.setActivityDescription("Running");
        activities.setElapsedTime("00:00:00");
        activities.setDistance("0.0");
        activities.setCalories("0");
        activities.setMaxHeartRate("0");
        activities.setActivityName("junitTest");
        return activities;
    }

    private ActivitiesResponse getactivitiesResponse() {
        return new ActivitiesResponse(
                1L, "11146355759", "2021-06-01", "Running", "Running", "00:00:00", "0.0", "0", "0", "junitTest");
    }
}
