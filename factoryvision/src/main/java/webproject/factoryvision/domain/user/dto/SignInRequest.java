package webproject.factoryvision.domain.user.dto;

import lombok.Data;

@Data
public class
SignInRequest {
    private String userId;
    private String password;
}
