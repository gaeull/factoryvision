package webproject.factoryvision.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserInfoResponse {
    private String name;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String userId;
    private String profilePhoto;
}
