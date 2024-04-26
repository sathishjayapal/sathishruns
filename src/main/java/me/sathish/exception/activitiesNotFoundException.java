package me.sathish.exception;

public class activitiesNotFoundException extends ResourceNotFoundException {

    public activitiesNotFoundException(Long id) {
        super("activities with Id '%d' not found".formatted(id));
    }
}
