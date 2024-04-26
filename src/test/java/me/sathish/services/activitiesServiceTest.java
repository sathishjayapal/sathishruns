package me.sathish.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Optional;
import me.sathish.entities.activities;
import me.sathish.mapper.activitiesMapper;
import me.sathish.model.response.activitiesResponse;
import me.sathish.repositories.activitiesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class activitiesServiceTest {

    @Mock
    private activitiesRepository activitiesRepository;

    @Mock
    private activitiesMapper activitiesMapper;

    @InjectMocks
    private activitiesService activitiesService;

    @Test
    void findactivitiesById() {
        // given
        given(activitiesRepository.findById(1L)).willReturn(Optional.of(getactivities()));
        given(activitiesMapper.toResponse(any(activities.class))).willReturn(getactivitiesResponse());
        // when
        Optional<activitiesResponse> optionalactivities = activitiesService.findactivitiesById(1L);
        // then
        assertThat(optionalactivities).isPresent();
        activitiesResponse activities = optionalactivities.get();
        assertThat(activities.id()).isEqualTo(1L);
        assertThat(activities.text()).isEqualTo("junitTest");
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

    private activities getactivities() {
        activities activities = new activities();
        activities.setId(1L);
        activities.setText("junitTest");
        return activities;
    }

    private activitiesResponse getactivitiesResponse() {
        return new activitiesResponse(1L, "junitTest");
    }
}
