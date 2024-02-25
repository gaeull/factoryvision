package webproject.factoryvision.domain.user.dto;

import lombok.*;
import webproject.factoryvision.domain.user.entity.Role;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private String name;
//    Role role;
    String token;
//    private String token;
}
