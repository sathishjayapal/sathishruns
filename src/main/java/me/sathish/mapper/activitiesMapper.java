package me.sathish.mapper;

import java.util.List;
import me.sathish.entities.activities;
import me.sathish.model.request.activitiesRequest;
import me.sathish.model.response.activitiesResponse;
import org.springframework.stereotype.Service;

@Service
public class activitiesMapper {

    public activities toEntity(activitiesRequest activitiesRequest) {
        activities activities = new activities();
        activities.setText(activitiesRequest.text());
        return activities;
    }

    public void mapactivitiesWithRequest(activities activities, activitiesRequest activitiesRequest) {
        activities.setText(activitiesRequest.text());
    }

    public activitiesResponse toResponse(activities activities) {
        return new activitiesResponse(activities.getId(), activities.getText());
    }

    public List<activitiesResponse> toResponseList(List<activities> activitiesList) {
        return activitiesList.stream().map(this::toResponse).toList();
    }
}
