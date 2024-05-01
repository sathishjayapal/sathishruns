package me.sathish.mapper;

import me.sathish.entities.Activities;
import me.sathish.entities.RawActivities;
import me.sathish.model.request.ActivitiesRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class RawActivitiesMapper {
    public void mapRawActivitiesWithRequest(RawActivities rawActivities, ActivitiesRequest activitiesRequest) {
        rawActivities.setActivityID(activitiesRequest.activityID());
        rawActivities.setActivityDate(activitiesRequest.activityDate());
        rawActivities.setActivityType(activitiesRequest.activityType());
        rawActivities.setActivityDescription(activitiesRequest.activityDescription());
        rawActivities.setElapsedTime(activitiesRequest.elapsedTime());
        rawActivities.setDistance(activitiesRequest.distance());
        rawActivities.setCalories(activitiesRequest.calories());
        rawActivities.setMaxHeartRate(activitiesRequest.maxHeartRate());
        rawActivities.setActivityName(activitiesRequest.activityName());
    }

    public Activities toEntity(RawActivities rawActivities) {
        Activities activities = new Activities();
        activities.setActivityID(new BigInteger(rawActivities.getActivityID()));
        activities.setActivityDate(rawActivities.getActivityDate());
        activities.setActivityType(rawActivities.getActivityType());
        activities.setActivityDescription(rawActivities.getActivityDescription());
        activities.setElapsedTime(rawActivities.getElapsedTime());
        activities.setDistance(rawActivities.getDistance());
        activities.setCalories(rawActivities.getCalories());
        activities.setMaxHeartRate(rawActivities.getMaxHeartRate());
        activities.setActivityName(rawActivities.getActivityName());
        return activities;
    }
}
