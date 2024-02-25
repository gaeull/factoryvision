package webproject.factoryvision.domain.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import webproject.factoryvision.domain.user.entity.Role;

@Getter
@Builder
public class SignUpResponse {
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private String userId;
    private String profilePhoto;
    @Enumerated(EnumType.STRING)
    private Role role;
}
