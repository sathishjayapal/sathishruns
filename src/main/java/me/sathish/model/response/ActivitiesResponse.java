package me.sathish.model.response;

public record ActivitiesResponse(
        Long id,
        String activityID,
        String activityDate,
        String activityType,
        String activityDescription,
        String elapsedTime,
        String distance,
        String calories,
        String maxHeartRate,
        String activityName) {}
