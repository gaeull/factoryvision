package webproject.factoryvision.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private String userId;
    private String profilePhoto;

}

