package webproject.factoryvision.domain.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String nickname;
    private String email;
    private String token;
}
