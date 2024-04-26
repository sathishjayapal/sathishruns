package me.sathish.model.request;

import jakarta.validation.constraints.NotEmpty;

public record activitiesRequest(@NotEmpty(message = "Text cannot be empty") String text) {}
