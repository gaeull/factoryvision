package webproject.factoryvision.domain.user.dto;

import lombok.*;
import webproject.factoryvision.domain.user.entity.Role;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private String name;
    private String userId;
    Role role;
//    String token;
//    private String token;

    public SignInResponse() {

    }
}
