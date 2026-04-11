package edu.tmu.group67.scrum_development.auth.model.dto;
//change to record
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}