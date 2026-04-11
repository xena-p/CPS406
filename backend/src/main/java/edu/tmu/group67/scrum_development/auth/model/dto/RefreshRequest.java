package edu.tmu.group67.scrum_development.auth.model.dto;

//already includes privete final fields, a constructor, getter and toString equal and hashCode methods, so we can use it to create immutable data 
// transfer objects (DTOs) for our authentication requests and responses. The RefreshRequest record is a simple DTO that contains a single field for 
// the refresh token, which can be used to request a new access token when the current one expires.

public record RefreshRequest(String refreshToken) {}