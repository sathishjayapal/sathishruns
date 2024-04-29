package me.sathish.mapper;

import me.sathish.entities.Activities;
import me.sathish.model.request.ActivitiesRequest;
import me.sathish.model.response.ActivitiesResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class activitiesMapper {
    public Activities toEntity(ActivitiesRequest activitiesRequest) {
        Activities activities = new Activities();
        updateIfDifferent(activities.getActivityID(), activitiesRequest.activityID(), activities::setActivityID);
        updateIfDifferent(activities.getActivityDate(), activitiesRequest.activityDate(), activities::setActivityDate);
        updateIfDifferent(activities.getActivityType(), activitiesRequest.activityType(), activities::setActivityType);
        updateIfDifferent(
                activities.getActivityDescription(),
                activitiesRequest.activityDescription(),
                activities::setActivityDescription);
        checkMapperData(activities, activitiesRequest);

        return activities;
    }

    private <T> void updateIfDifferent(T currentValue, T newValue, Consumer<T> setter) {
        if (!Objects.equals(currentValue, newValue)) {
            setter.accept(newValue);
        }
    }

    public void mapactivitiesWithRequest(Activities activities, ActivitiesRequest activitiesRequest) {
        updateIfDifferent(activities.getActivityName(), activitiesRequest.activityName(), activities::setActivityName);
        updateIfDifferent(activities.getActivityType(), activitiesRequest.activityType(), activities::setActivityType);
        updateIfDifferent(
                activities.getActivityDescription(),
                activitiesRequest.activityDescription(),
                activities::setActivityDescription);
        updateIfDifferent(activities.getActivityDate(), activitiesRequest.activityDate(), activities::setActivityDate);
        checkMapperData(activities, activitiesRequest);
    }

    private void checkMapperData(Activities activities, ActivitiesRequest activitiesRequest) {
        updateIfDifferent(activities.getElapsedTime(), activitiesRequest.elapsedTime(), activities::setElapsedTime);
        String distance = activitiesRequest.distance();
        if (distance != null) {
            double distanceInKm = Double.parseDouble(distance) * 1.6;
            updateIfDifferent(activities.getDistance(), String.valueOf(distanceInKm), activities::setDistance);
        }
        updateIfDifferent(activities.getCalories(), activitiesRequest.calories(), activities::setCalories);
        updateIfDifferent(activities.getMaxHeartRate(), activitiesRequest.maxHeartRate(), activities::setMaxHeartRate);
        updateIfDifferent(activities.getActivityName(), activitiesRequest.activityName(), activities::setActivityName);
    }

    public ActivitiesResponse toResponse(Activities activities) {
        return new ActivitiesResponse(activities.getId(), activities.getActivityID(), activities.getActivityDate(),
                activities.getActivityType(), activities.getActivityDescription(), activities.getElapsedTime(),
                activities.getDistance(), activities.getCalories(), activities.getMaxHeartRate(),
                activities.getActivityName());
    }

    public List<ActivitiesResponse> toResponseList(List<Activities> activitiesList) {
        return activitiesList.stream().map(this::toResponse).toList();
    }
}
